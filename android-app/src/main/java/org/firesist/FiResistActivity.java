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
import org.firesist.position.AccelerometerReader;

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

import com.goatstone.util.SensorFusion;
import java.text.DecimalFormat;

public class FiResistActivity extends Activity 
	implements SensorEventListener, RadioGroup.OnCheckedChangeListener{
	
	private SharedPreferences settings;
	private FiReceiver receiver;
	private SensorFusion sensorFusion;
	private SensorManager sensorManager = null;
	private AlarmManager manager;
	private BiometricReader biometricReader;
	private AccelerometerReader accelerometerReader;
	private TextView nameTextView;
	private TextView azimuthText, pithText, rollText;
	private String firefighterName;

	private DecimalFormat d = new DecimalFormat("#.##");
	private RadioGroup setModeRadioGroup;



	//TODO: Move SensorFusion Data to appropriate Receiver and display/approximate movement on map
	
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

		//Get Sensor Service and register listeners
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		registerSensorManagerListeners();

		d.setMaximumFractionDigits(2);
		d.setMinimumFractionDigits(2);

		sensorFusion = new SensorFusion();
		sensorFusion.setMode(SensorFusion.Mode.ACC_MAG);

		setModeRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		azimuthText = (TextView) findViewById(R.id.azmuth);
		pithText = (TextView) findViewById(R.id.pitch);
		rollText = (TextView) findViewById(R.id.roll);
		setModeRadioGroup.setOnCheckedChangeListener(this);

		//initialize biometric reader
		biometricReader = new BiometricReader();

		//initialize accelerometer reader
		accelerometerReader = new AccelerometerReader(this);
		
		// Initialize socket connection
		FiSocketHandler.getInstance()
			.initSocket(getString(R.string.host));

		// Set up socket background task
		Intent socketIntent = new Intent(this, FiReceiver.class);
		
	}

	//Get sensor values and update view
	public void updateOrientationDisplay() {

       		double azimuthValue = sensorFusion.getAzimuth();
        	double rollValue =  sensorFusion.getRoll();
        	double pitchValue =  sensorFusion.getPitch();

        	azimuthText.setText(String.valueOf(d.format(azimuthValue)));
        	pithText.setText(String.valueOf(d.format(pitchValue)));
        	rollText.setText(String.valueOf(d.format(rollValue)));
    	}

	//Register listeners for all sensors
	public void registerSensorManagerListeners() {
        	sensorManager.registerListener(this,
                	sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                	SensorManager.SENSOR_DELAY_FASTEST);

        	sensorManager.registerListener(this,
                	sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                	SensorManager.SENSOR_DELAY_FASTEST);

        	sensorManager.registerListener(this,
                	sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                	SensorManager.SENSOR_DELAY_FASTEST);
   	}

	@Override
	public void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);

		// Save firefighter name
		SharedPreferences.Editor editor = this.settings.edit();
		editor.putString("FIREFIGHTER_NAME", firefighterName);
		editor.commit();

		try {
			FiSocketHandler.getInstance()
				.disconnect();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//New
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

    	@Override
    	public void onResume() {
    	    super.onResume();
        	registerSensorManagerListeners();
    	}

    	@Override
    		public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	}

	@Override
    	public void onSensorChanged(SensorEvent event) {
		//Update all sensors to current hardware readings
        	switch (event.sensor.getType()) {
            	case Sensor.TYPE_ACCELEROMETER:
                	sensorFusion.setAccel(event.values);
                	sensorFusion.calculateAccMagOrientation();
                	break;

            	case Sensor.TYPE_GYROSCOPE:
                	sensorFusion.gyroFunction(event);
                	break;

            	case Sensor.TYPE_MAGNETIC_FIELD:
                	sensorFusion.setMagnet(event.values);
               	 	break;
        	}
        	updateOrientationDisplay();
    	}

	@Override
    	public void onCheckedChanged(RadioGroup group, int checkedId) {
		//Show Selected mode values only
        	switch (checkedId) {
            	case R.id.radio0:
                	sensorFusion.setMode(SensorFusion.Mode.ACC_MAG);
                	break;
            	case R.id.radio1:
                	sensorFusion.setMode(SensorFusion.Mode.GYRO);
                	break;
            	case R.id.radio2:
                	sensorFusion.setMode(SensorFusion.Mode.FUSION);
                	break;
        	}
    	}
	//

	/**
	 * Starts monitoring devices on an interval
	 */
	public void startAlarm(View view) {

			FiSocketHandler.getInstance()
				.connect();

			// start listening
			accelerometerReader.startListening();

/*			try {
				biometricReader.startReading();
			} catch (RuntimeException e) {
				Toast.makeText(this, "No BioHarness found", Toast.LENGTH_SHORT).show();
			} */

			//Vibrate indicating connection success
			Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(500);


	}

	/**
	 * Turns off monitor, disconnects socket
	 */
	public void cancelAlarm(View view) {
		biometricReader.stopReading();
		accelerometerReader.stopListening();

		try {
			FiSocketHandler.getInstance()
				.disconnect();
		} catch(JSONException e) {
			e.printStackTrace();
		}

		Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
		
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
