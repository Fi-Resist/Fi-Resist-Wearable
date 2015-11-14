package org.firesist;


import android.app.Activity;
import android.os.Vibrator;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.view.View;


import java.net.URISyntaxException;
import org.json.JSONException;
import org.firesist.receiver.FiReceiver;
import org.firesist.sockethandler.FiSocketHandler;
import org.firesist.biometric.BiometricReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.R.*;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;

import org.json.JSONObject;

public class FiResistActivity extends Activity {
	
	private SharedPreferences settings;
	private FiReceiver receiver;
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	private BiometricReader biometricReader;
	private final int INTERVAL = 1000;
	private TextView nameTextView;
	private String firefighterName;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firesist_layout);
		this.settings = getPreferences(MODE_PRIVATE);

		nameTextView = (TextView) findViewById(R.id.firefighter_name);

		// Set the firefighter name if it's been done before
		firefighterName = settings.getString("FIREFIGHTER_NAME", null);
		if (firefighterName == null) {
			// Launch initial wizard
			setName();
		}
		else {
			nameTextView.setText(firefighterName);
		}

		//initialize biometric reader
		biometricReader = new BiometricReader();


		// Initialize socket connection
		FiSocketHandler.getInstance()
			.initSocket(getString(R.string.host));

		// Set up socket background task
		Intent socketIntent = new Intent(this, FiReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, socketIntent, 0);
		
	}


	@Override
	public void onStop() {
		super.onStop();

		// Save firefighter name
		SharedPreferences.Editor editor = this.settings.edit();
		editor.putString("FIREFIGHTER_NAME", firefighterName);
		editor.commit();

		// Disconnect Socket
		if (manager != null) {
			manager.cancel(pendingIntent);
		}
		try {
			FiSocketHandler.getInstance()
				.disconnect();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts monitoring devices on an interval
	 */
	public void startAlarm(View view) {

			FiSocketHandler.getInstance()
				.connect();

			try {
				biometricReader.startReading();
			} catch (RuntimeException e) {
				Toast.makeText(this, "No BioHarness found", Toast.LENGTH_SHORT).show();
			}

			//Vibrate indicating connection success
			Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(500);

			manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
	}

	/**
	 * Turns off monitor, disconnects socket
	 */
	public void cancelAlarm(View view) {
		biometricReader.stopReading();

		if (manager != null) {
			manager.cancel(pendingIntent);
			try {
				FiSocketHandler.getInstance()
					.disconnect();
			} catch (JSONException e) {
				e.printStackTrace();
			}


			Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	  * Prompts for and sets name
	  */
	private void setName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter your name");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//This gets overriden below (to add a close condition)
				// (Dialogs w/ conditions are funky)
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE)
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					firefighterName = input.getText().toString().trim();
							//Set the text view
					if (!firefighterName.isEmpty()) {
						nameTextView.setText(firefighterName);
								//Set the firefighter name for the socket

						FiSocketHandler.getInstance()
							.setName(firefighterName);
						//Close the dialog
						dialog.dismiss();
					}
				}
			});
	}

	/**
	  * Click Listener wrapper for name
	  */
	public void setName(View v) {
		setName();
	}


}
