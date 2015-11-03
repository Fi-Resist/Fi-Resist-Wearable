package org.firesist.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.firesist.sockethandler.FiSocketHandler;

/**
 * Runs in background, grabbing data from devices and emitting it
 */
public class FiReceiver extends BroadcastReceiver {

	private final String UPDATE_BIOMETRICS = "update-biometrics";
	private final String UPDATE_POSITION = "update-position";

	public FiReceiver() {
	}

	/**
	  * Reads data from wearables and broadcasts it
	  */
	@Override
	public void onReceive(Context context, Intent intent) {
		// read data from devices here...
		System.out.println("Running receiver...");

		// send data through websocket
		//FiSocketHandler.getInstance()
//			.sendUpdate(data);


	}
}


