package org.firesist.biometric;

import zephyr.android.BioHarnessBT.*;
import android.os.Handler;
import android.util.Log;
import org.firesist.sockethandler.FiSocketHandler;
import org.json.JSONObject;
import org.json.JSONException;

public class FiConnectedListener extends ConnectListenerImpl {
		private Handler handler;
		private PacketTypeRequest packetType = new PacketTypeRequest();
		private GeneralPacketInfo generalPacketInfo = new GeneralPacketInfo();
		private final String UPDATE_BIOMETRICS = "update-biometrics";
		private int lastHeartRate = 0;

		// BioHarness Constants
		private final int HEART_RATE = 0x100;
		private final int RESPIRATION_RATE = 0x101;
		private final int SKIN_TEMPERATURE = 0x102;
		final int GP_MSG_ID = 0x20;

		public FiConnectedListener(Handler handler) {
				super(handler, null);
				this.handler = handler;
		}

		public void Connected(ConnectedEvent<BTClient> eventArgs) {
				//configure packet
				packetType.GP_ENABLE = true;
				packetType.BREATHING_ENABLE = true;
				packetType.LOGGING_ENABLE = true;

				// Create new ZephyrProtocol object
				ZephyrProtocol protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), packetType);

				protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {

						//function runs when packet is received
						public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
								ZephyrPacketArgs message = eventArgs.getPacket();
								int messageId = message.getMsgID();
								byte[] messageData = message.getBytes();

								//message is biometric data
								if (messageId == GP_MSG_ID) {
										//heart rate
										int heartRate = generalPacketInfo.GetHeartRate(messageData);
										//Respirationrate
										double respirationRate = generalPacketInfo.GetRespirationRate(messageData);
										Log.d("FiResist", String.format("Data: %d %f", heartRate, respirationRate));

										//Send to the socket

										if (heartRate != lastHeartRate) {
											try {
												JSONObject bioData = new JSONObject();
												bioData.accumulate("bpm", heartRate);
												bioData.accumulate("resp", respirationRate);
												FiSocketHandler.getInstance().sendUpdate(UPDATE_BIOMETRICS, bioData);
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
										lastHeartRate = heartRate;
								}
						}

				});
		}
}

