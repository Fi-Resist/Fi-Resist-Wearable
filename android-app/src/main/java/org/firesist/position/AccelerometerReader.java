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
	private long pastTime;
	private long startTime;
	private float lastAccel;

	public AccelerometerReader(Context context)
	{
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		startTime = pastTime;
		lastAccel = 0;

		if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()>0) {
			accelSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		}

	}


	private float calcDistance(float newAccel, long timeElapsed) {
		float a = (timeElapsed) / (timeElapsed + (5));
		return (a * lastAccel) + (1 - a) * newAccel; 
	}

	public void startListening() {

		pastTime = System.currentTimeMillis();
		sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void stopListening() {
		sensorManager.unregisterListener(this);
	}

	@Override 
	public void onSensorChanged(SensorEvent event)
	{
		Sensor mySensor = event.sensor;

		if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			accelValues[0] = event.values[0];
			accelValues[1] = event.values[1];
			accelValues[2] = event.values[2];
			if (System.currentTimeMillis() >= (pastTime + (5 * 1000))) {
				pastTime = System.currentTimeMillis();
				Log.d("ACCEL", "5 seconds has passed");
				Log.d("ACCEL", String.format("%f", calcDistance(event.values[2], pastTime)));
				lastAccel = event.values[2];
			}


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

