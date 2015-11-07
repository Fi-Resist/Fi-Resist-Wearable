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

public class FiResistActivity extends Activity {
	private FiReceiver receiver;
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	private final int INTERVAL = 1000;
	private EditText nameInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firesist_layout);
		nameInput = (EditText) findViewById(R.id.edit_name);


		FiSocketHandler.getInstance()
			.initSocket(getString(R.string.host));

		// Set up socket background task
		Intent socketIntent = new Intent(this, FiReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, socketIntent, 0);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();

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
		String name = nameInput.getText()
			.toString()
			.trim();

		if (name.isEmpty()) {
			Toast.makeText(this, "Please input a name", Toast.LENGTH_SHORT).show();
		}
		else {

			FiSocketHandler.getInstance()
				.setName(name);
			FiSocketHandler.getInstance()
				.connect();

			//Vibrate indicating connection success
			Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(500);

			manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
		}
	}

	/**
	 * Turns off monitor, disconnects socket
	 */
	public void cancelAlarm(View view) {
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

}
