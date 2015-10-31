package org.firesist.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Runs in background, grabbing data from devices and emitting it
 */
public class FiReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Running", Toast.LENGTH_SHORT)
			.show();
	}
}


