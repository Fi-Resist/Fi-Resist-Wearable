package org.firesist;

import zephyr.android.BioHarnessBT.*;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

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


public class FiResistActivity extends Activity {
	private FiReceiver receiver;
	private PendingIntent pendingIntent;
	private AlarmManager manager;
	private final int INTERVAL = 1000;
	private EditText nameInput;
    
    /** Called when the activity is first created. */
    BluetoothAdapter adapter = null;
    BTClient _bt;
    ZephyrProtocol _protocol;
    //BiometricListener _NConnListener;
    private final int HEART_RATE = 0x100;
    private final int RESPIRATION_RATE = 0x101;
    private final int SKIN_TEMPERATURE = 0x102;
    private final int POSTURE = 0x103;
    private final int PEAK_ACCLERATION = 0x104;
    

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
        
            /*Sending a message to android that we are going to initiate a pairing request*/
            //IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
            /*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
            //this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
            // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
           /* IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
            this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);
        
            
            Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
            if (btnConnect != null)
            {
                btnConnect.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String BhMacID = "00:07:80:9D:8A:E8";
                        adapter = BluetoothAdapter.getDefaultAdapter();
                        
                        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
                        
                        if (pairedDevices.size() > 0)
                        {
                            for (BluetoothDevice device : pairedDevices)
                            {
                                if (device.getName().startsWith("BH"))
                                {
                                    BluetoothDevice btDevice = device;
                                    BhMacID = btDevice.getAddress();
                                    break;
                                    
                                }
                            }
                            
                            
                        }
                        
                        //BhMacID = btDevice.getAddress();
                        BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
                        String DeviceName = Device.getName();
                        _bt = new BTClient(adapter, BhMacID);
                        _NConnListener = new BiometricListener(Newhandler,Newhandler);
                        _bt.addConnectedEventListener(_NConnListener);
                        
                        TextView tv1 = (EditText)findViewById(R.id.labelHeartRate);
                        tv1.setText("000");
                        
                        tv1 = (EditText)findViewById(R.id.labelRespRate);
                        tv1.setText("0.0");
                        
                        tv1 = 	(EditText)findViewById(R.id.labelSkinTemp);
                        tv1.setText("0.0");
                        
                        tv1 = 	(EditText)findViewById(R.id.labelPosture);
                        tv1.setText("000");
                        
                        tv1 = 	(EditText)findViewById(R.id.labelPeakAcc);
                        tv1.setText("0.0");
                    }
                });
            }*/
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
    
    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }
    private class BTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BTIntent", intent.getAction());
            Bundle b = intent.getExtras();
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
            try {
                BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
                Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
                byte[] pin = (byte[])m.invoke(device, "1234");
                m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
                Object result = m.invoke(device, pin);
                Log.d("BTTest", result.toString());
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
