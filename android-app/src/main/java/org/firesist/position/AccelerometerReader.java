package org.firesist.position;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.goatstone.util.SensorFusion;
import android.os.Vibrator;

import org.firesist.sockethandler.FiSocketHandler;

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
import java.util.ArrayList;
import java.util.Collections;

public class AccelerometerReader implements SensorEventListener {

	private SensorManager sensorManager;
	private Sensor accelSensor;
	private float[] accelValues = new float[3];
	private ArrayList<Float> velocityList;
	private ArrayList<Float> accelerationList;
	private ArrayList<Float> positionList;
	private ArrayList<Float> azimuthList;
	private float lastAccel;
	private Float prevOrientation;
	private final String UPDATE_POSITION = "update-position";
	private SensorFusion sensorFusion;
	private DistanceCalculator distanceCalculator;
	private Vibrator vibrator;
	private boolean initialHasSent; 
	private long lastTime;

	public AccelerometerReader(Context context)
	{
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		lastAccel = 0;

		velocityList = new ArrayList<>();
		accelerationList = new ArrayList<>();
		positionList = new ArrayList<>();
		azimuthList = new ArrayList<>();

		velocityList.add(new Float((float)0));
		positionList.add(new Float((float)0));
		accelerationList.add(new Float((float)0));

		sensorFusion = new SensorFusion();
		sensorFusion.setMode(SensorFusion.Mode.ACC_MAG);
		distanceCalculator = new DistanceCalculator();


		if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()>0) {
			accelSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		}

	}


	public void startListening() {
		initialHasSent = false;
		lastTime = System.currentTimeMillis();
		distanceCalculator.clearStoredDisplacement();
		sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this,
					   	sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void stopListening() {
		distanceCalculator.clearStoredDisplacement();
		sensorManager.unregisterListener(this);
	}

	private Float getMedian(List<Float> l) {
			Collections.sort(l);
			int middle = l.size() / 2;
			return l.get(middle);
	}

	private Float getMean(List<Float> l) {
		Float mean = 0f;
		for (Float f : l) {
			mean += f;
		}

		return mean / l.size();
	}

	@Override 
	public void onSensorChanged(SensorEvent event)
	{
		Sensor mySensor = event.sensor;

		if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			final float alpha = (float)0.8;

			float gravity[] = new float[3];
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


			sensorFusion.setAccel(event.values);
			sensorFusion.calculateAccMagOrientation();

			accelerationList.add(new Float(event.values[1] - gravity[1]));
			azimuthList.add(new Float(sensorFusion.getAzimuth()));
			if (!initialHasSent && (getMean(azimuthList) != 0)) {
				try {
					JSONObject initial = new JSONObject();
					initial.put("pos", 0);
					initial.put("orientation", sensorFusion.getAzimuth());
					FiSocketHandler.getInstance().sendUpdate(UPDATE_POSITION, initial);
					initialHasSent = true;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			accelValues[0] = new Float(event.values[0] - gravity[0]);
			accelValues[1] = new Float(event.values[1] - gravity[1]);
			accelValues[2] = new Float(event.values[2] - gravity[2]);

//			Log.d("TIMECHECK", String.format("%d", (System.currentTimeMillis() - lastTime))); 
			if ((System.currentTimeMillis() - lastTime) >= 5000 ) {
				try {
					lastTime = System.currentTimeMillis();
					JSONObject json = new JSONObject();
					Float orientation = getMean(azimuthList);
					if (prevOrientation == null) {
						prevOrientation = orientation;
					}


					double distance = distanceCalculator.calculateDistance(accelerationList, 36.67);
					//Turn detection
					if (Math.abs(orientation - prevOrientation) > 30 ) {
						distanceCalculator.clearStoredDisplacement();
					}


					// 2 meters ~= 6.5 ft
//					if (distance >= 2) {
						json.put("pos", distance * 2);
						json.put("orientation", orientation);

						FiSocketHandler.getInstance().sendUpdate(UPDATE_POSITION, json);
						FiSocketHandler.getInstance().storedDistance = distance * 2;
						prevOrientation = orientation;
						distanceCalculator.clearStoredDisplacement();
//					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


				accelerationList.clear();

			}
		}
		if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				sensorFusion.setMagnet(event.values);
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

