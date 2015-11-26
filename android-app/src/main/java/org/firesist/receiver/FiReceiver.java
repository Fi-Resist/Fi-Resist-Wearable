package org.firesist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.firesist.sockethandler.FiSocketHandler;
import org.firesist.position.AccelerometerReader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import zephyr.android.BioHarnessBT.*;

import org.json.*;


/**
 * Runs in background, grabbing data from devices and emitting it
 */
public class FiReceiver extends BroadcastReceiver {

    private final String UPDATE_BIOMETRICS = "update-biometrics";
    private final String UPDATE_POSITION = "update-position";
    private AccelerometerReader accel;

    public FiReceiver() {
    }

	/**
	  * Reads data from wearables and broadcasts it
	  */
	@Override
	public void onReceive(Context context, Intent intent) {
		// read data from devices here...
		System.out.println("Running receiver...");

		if (accel == null) {
	    	accel = new AccelerometerReader(context.getApplicationContext());
		}

		JSONObject json = new JSONObject();

		try{
			json = accel.readAccelerometer();
			// send data through websocket
			FiSocketHandler.getInstance().sendUpdate(UPDATE_POSITION, json);
		} catch(JSONException e) {
			e.printStackTrace();	
		}

	}
}


