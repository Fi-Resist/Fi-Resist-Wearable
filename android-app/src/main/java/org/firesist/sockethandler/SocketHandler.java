package org.firesist.sockethandler;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URISyntaxException;



/**
  * Handles web socket set up and communication
  */
public class SocketHandler {

	private Socket socket;

	/*
	 * Initializes socket connection
	 */
	public SocketHandler(String url) {
		try {
			socket = IO.socket(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Wrapper for socket connect method
	 */
	public void connect() {
		socket.connect();
	}

	/*
	 * Wrapper for socket's disconnect method
	 */
	public void disconnect() {
		socket.disconnect();
	}

	/*
	 * Emits JSON to socket server
	 * @param eventName the event to emit
	 * @param message the message to send
	 */
	public void sendJSON(String eventName, JSONObject message) {
		socket.emit(eventName, message);
	}

}

