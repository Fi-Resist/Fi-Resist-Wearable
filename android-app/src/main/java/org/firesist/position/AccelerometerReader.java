package org.firesist.position;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import org.json.*;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AccelerometerReader implements SensorEventListener {

	private SensorManager sensorManager;
	private Sensor accelSensor;
	private float[] accelValues = new float[3];

	public AccelerometerReader(Context context)
	{
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()>0)
			accelSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
	}

	@Override 
	public void onSensorChanged(SensorEvent event)
	{
		Sensor mySensor = event.sensor;
		Log.d("FIRESIST", String.format("Accel: %f, %f, %f", event.values[0], event.values[1], event.values[2]));

		if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			accelValues = event.values.clone();
		}
	}	

	@Override
	public void onAccuracyChanged(Sensor sensor, int acc)
	{

	}

	protected void onPause()
	{
		sensorManager.unregisterListener(this);
	}	

	protected void onResume()
	{
		sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}	
	
	public float readZ() {
			return accelValues[2];
	}

	public JSONObject readAccelerometer() throws JSONException {
	               JSONObject json = new JSONObject();
		       json.put("x", accelValues[0]);
		       json.put("y", accelValues[1]);
		       json.put("z", accelValues[2]);
		       return json;
	}
}

