package org.firesist.biometriclistener;

import zephyr.android.BioHarnessBT.*;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.R.*;
import android.app.Activity;
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
import android.widget.TextView;

public class BiometricListener extends ConnectListenerImpl {
    private Handler _OldHandler;
    private Handler _aNewHandler;
    final int GP_MSG_ID = 0x20;
    final int BREATHING_MSG_ID = 0x21;
    final int ECG_MSG_ID = 0x22;
    final int RtoR_MSG_ID = 0x24;
    final int ACCEL_100mg_MSG_ID = 0x2A;
    final int SUMMARY_MSG_ID = 0x2B;

    private int GP_HANDLER_ID = 0x20;
    
    /*Creating the different Objects for different types of Packets*/
    private GeneralPacketInfo GPInfo = new GeneralPacketInfo();
    private ECGPacketInfo ECGInfoPacket = new ECGPacketInfo();
    private BreathingPacketInfo BreathingInfoPacket = new  BreathingPacketInfo();
    private RtoRPacketInfo RtoRInfoPacket = new RtoRPacketInfo();
    private AccelerometerPacketInfo AccInfoPacket = new AccelerometerPacketInfo();
    private SummaryPacketInfo SummaryInfoPacket = new SummaryPacketInfo();

    private PacketTypeRequest RqPacketType = new PacketTypeRequest();

    public BiometricListener(Handler handler,Handler _NewHandler) {
        super(handler, null);
        _OldHandler= handler;
        _aNewHandler = _NewHandler;
        
        // TODO Auto-generated constructor stub
        
        String message;
    }
    
    
    /* Socket broadcast should happen here
 	 * I think for sending over the socket server we'll have to create events for each biometric
	 * e.g. BIOMETRIC_HEARTRATE
 	 * Then the socekt server can update the firefighter and broadcast the new data
	 */
    public void Connected(ConnectedEvent<BTClient> eventArgs) {
        /*System.out.println(String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));*/
        /*Use this object to enable or disable the different Packet types*/
        RqPacketType.GP_ENABLE = true;
        RqPacketType.BREATHING_ENABLE = true;
        RqPacketType.LOGGING_ENABLE = true;
        
        
        //Creates a new ZephyrProtocol object and passes it the BTComms object
        ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), RqPacketType);
        //ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), );
        _protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
            public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
                ZephyrPacketArgs msg = eventArgs.getPacket();
                byte CRCFailStatus;
                byte RcvdBytes;
                
                
                
                CRCFailStatus = msg.getCRCStatus();
                RcvdBytes = msg.getNumRvcdBytes() ;
                int MsgID = msg.getMsgID();
                byte [] DataArray = msg.getBytes();
                switch (MsgID)
                {
                        
                    case GP_MSG_ID:
                        //***************Displaying the Heart Rate********************************
                        int HRate =  GPInfo.GetHeartRate(DataArray);
                        System.out.println("Heart Rate is "+ HRate);
                        
                        //***************Displaying the Respiration Rate********************************
                        double RespRate = GPInfo.GetRespirationRate(DataArray);
                        System.out.println("Respiration Rate is "+ RespRate);

                        //***************Displaying the Skin Temperature*******************************
                        double SkinTempDbl = GPInfo.GetSkinTemperature(DataArray);
                        System.out.println("Skin Temperature is "+ SkinTempDbl);

                        //***************Displaying the Posture******************************************
                        int PostureInt = GPInfo.GetPosture(DataArray);
                        System.out.println("Posture is "+ PostureInt);
                        break;

					// Shouldn't have to handle these... 
                    case BREATHING_MSG_ID:
                        /*Do what you want. Printing Sequence Number for now*/
                        System.out.println("Breathing Packet Sequence Number is "+BreathingInfoPacket.GetSeqNum(DataArray));
                        break;
                    case ECG_MSG_ID:
                        /*Do what you want. Printing Sequence Number for now*/
                        System.out.println("ECG Packet Sequence Number is "+ECGInfoPacket.GetSeqNum(DataArray));
                        break;
                    case RtoR_MSG_ID:
                        /*Do what you want. Printing Sequence Number for now*/
                        System.out.println("R to R Packet Sequence Number is "+RtoRInfoPacket.GetSeqNum(DataArray));
                        break;
                    case ACCEL_100mg_MSG_ID:
                        /*Do what you want. Printing Sequence Number for now*/
                        System.out.println("Accelerometry Packet Sequence Number is "+AccInfoPacket.GetSeqNum(DataArray));
                        break;
                    case SUMMARY_MSG_ID:
                        /*Do what you want. Printing Sequence Number for now*/
                        System.out.println("Summary Packet Sequence Number is "+SummaryInfoPacket.GetSeqNum(DataArray));
                        break;
                }
 	                /*Construct JSON Object*/
/*	                JSONObject item = new JSONObject();
	                item.put("Heart Rate", HRate);
	                item.put("Resperation Rate", RespRate);
	                item.put("Skin Temperature", SkinTempDbl);
	                item.put("Posture", PostureInt); */
					// Broadcast socket here
            }
        });
        
        
    }
    

}
