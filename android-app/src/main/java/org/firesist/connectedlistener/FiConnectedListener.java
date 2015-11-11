package org.firesist.connectedlistener;

import zephyr.android.BioHarnessBT.*;
import android.os.Handler;
import android.util.Log;

public class FiConnectedListener extends ConnectListenerImpl {
		private Handler handler;
		private PacketTypeRequest packetType = new PacketTypeRequest();
		private GeneralPacketInfo generalPacketInfo = new GeneralPacketInfo();

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
				Log.d("FiResist", "Connected to Bioharness");
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
										Log.d("FiResist", String.format("Data: %d %d", heartRate, respirationRate));
								}
						}

				});
		}
}

