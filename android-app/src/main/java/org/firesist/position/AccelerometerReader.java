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
	private long pastTime;
	private long startTime;
	private ArrayList<Float> velocityList;
	private ArrayList<Float> accelerationList;
	private ArrayList<Float> positionList;
	private ArrayList<Float> azimuthList;
	private int readCount;
	private float lastAccel;
	private Float prevOrientation;
	private final String UPDATE_POSITION = "update-position";
	private SensorFusion sensorFusion;
	private DistanceCalculator distanceCalculator;
	private Vibrator vibrator;

	public AccelerometerReader(Context context)
	{
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		pastTime = System.currentTimeMillis();
		startTime = pastTime;
		lastAccel = 0;
		readCount = 0;

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


	private float calcDistance(Float newAccel, long timeElapsed) {
		Float oldAccel = getMedian(accelerationList);
		Float oldPosition = positionList.get(positionList.size() - 1);
		Float oldVelocity = velocityList.get(velocityList.size() - 1);



		//calculate new velocity
		Float newVelocity = ((newAccel - oldAccel) );


		//calculate new position
		Float newPosition = ((newVelocity - oldVelocity));

		Log.d("ACCEL", String.format("Unfiltered position %f", newPosition));

		// add new values
		accelerationList.add(newAccel);
		positionList.add(newPosition);
		velocityList.add(newVelocity);


		//filter results
		Long aLong = (long) (245) / (245 + (5));
		Float a = aLong.floatValue();
		return Math.abs((a * oldPosition) + (1 - a) * newPosition); 
	}

	public void startListening() {
		distanceCalculator.clearStoredDisplacement();
		pastTime = System.currentTimeMillis();
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

	@Override 
	public void onSensorChanged(SensorEvent event)
	{
		Sensor mySensor = event.sensor;

		if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{

			sensorFusion.setAccel(event.values);
			sensorFusion.calculateAccMagOrientation();

			accelerationList.add(new Float(event.values[0]));
			azimuthList.add(new Float(sensorFusion.getAzimuth()));
			Log.d("AZ", String.format("Azimuth %f", sensorFusion.getAzimuth()));
			accelValues[0] = new Float(event.values[0]);
			accelValues[1] = new Float(event.values[1]);
			accelValues[2] = new Float(event.values[2]);
			if (accelerationList.size() == 256) {
				readCount++;
				try {
					JSONObject json = new JSONObject();
					Float orientation = getMedian(azimuthList);
					if (prevOrientation == null) {
						prevOrientation = orientation;
					}


					double distance = distanceCalculator.calculateDistance(accelerationList, 36.67);
					//Turn detection
					if (Math.abs(orientation - prevOrientation) > 30 ) {
						distanceCalculator.clearStoredDisplacement();
						readCount = 10;

					}


					Log.d("FFT", String.format("%f", distance));
						json.put("pos", distance);
						json.put("orientation", orientation);
						FiSocketHandler.getInstance().sendUpdate(UPDATE_POSITION, json);
						FiSocketHandler.getInstance().storedDistance = distance;
						vibrator.vibrate(500);
						prevOrientation = orientation;
						distanceCalculator.clearStoredDisplacement();
						readCount = 0;

				} catch (JSONException e) {
					e.printStackTrace();
				}


				accelerationList.clear();

			}

/*			if (System.currentTimeMillis() >= (pastTime + (5 * 1000))) {
				pastTime = System.currentTimeMillis();
				Log.d("FFT", String.format("%f", distanceCalculator.calculateDistance(accelerationList, SensorManager.SENSOR_DELAY_FASTEST)));
				Log.d("ACCEL", "5 seconds has passed");
				try {
					JSONObject json = new JSONObject();
					Float orientation = getMedian(azimuthList);
					json.put("pos", calcDistance(event.values[2], pastTime - startTime));
					json.put("orientation", orientation);
					FiSocketHandler.getInstance().sendUpdate(UPDATE_POSITION, json);
				} catch (JSONException e) {
						e.printStackTrace();
				}


				Log.d("ACCEL", String.format("%f", calcDistance(event.values[2], pastTime - startTime)));
				accelerationList.clear();
				azimuthList.clear();
			} */
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

