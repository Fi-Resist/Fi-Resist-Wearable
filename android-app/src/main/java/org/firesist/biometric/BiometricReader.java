package org.firesist.biometric;
import zephyr.android.BioHarnessBT.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.lang.RuntimeException;
import java.util.Set;

public class BiometricReader {

	private BTClient btClient;
	private String bhMacId;
	FiConnectedListener connectedListener;
	BluetoothAdapter adapter = null;

	public BiometricReader() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	private void connectBioharness()  throws RuntimeException {
		// Get the mac id
		if (bhMacId == null) {
			Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
						
			if (pairedDevices.size() > 0)
			{
				for (BluetoothDevice device : pairedDevices)
				{
					if (device.getName().startsWith("BH"))
					{
						BluetoothDevice btDevice = device;
						bhMacId = btDevice.getAddress();
						break;
					}
				}
			}
		}

		//if no device found
		if (bhMacId == null) {
			throw new RuntimeException("No BioHarness found");
		}

					
		BluetoothDevice Device = adapter.getRemoteDevice(bhMacId);
		btClient = new BTClient(adapter, bhMacId);
		connectedListener= new FiConnectedListener(null);
		btClient.addConnectedEventListener(connectedListener);
	}
		


	/**
	  * Initializes BlueTooth connection and hooks up the connected listener
	  */
	public void startReading() throws RuntimeException {
		connectBioharness();
		if (btClient.IsConnected()) {
			btClient.start();
		}
	}

	/**
	  * Closes down BlueTooth connection
	  */
	public void stopReading() {
		// Disconnect bioharness
		if (btClient != null && btClient.IsConnected()) {
			btClient.removeConnectedEventListener(connectedListener);
			btClient.Close();
		}
	}
}
