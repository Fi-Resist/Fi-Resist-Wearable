package org.firesist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.view.View;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import org.firesist.receiver.FiReceiver;

public class FiResistActivity extends Activity {
	private FiReceiver receiver;
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	private final int INTERVAL = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firesist_layout);

		// Set up socket background task
		Intent socketIntent = new Intent(this, FiReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, socketIntent, 0);


    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Starts monitoring devices on an interval
	 */
	public void startAlarm(View view) {
		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
	}

	/**
	 * Turns off monitor
	 */
	public void cancelAlarm(View view) {
		if (manager != null) {
			manager.cancel(pendingIntent);
			Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
		}
	}

}
