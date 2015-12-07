package org.firesist.sockethandler;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URISyntaxException;
import android.util.Log;
/**
  * Web socket communication for FiResist
  */
public class FiSocketHandler {

	// Socket message constants
	private final String MSG_NEW_FIREFIGHTER = "new-firefighter";
	private final String MSG_UPDATE_FIREFIGHTER = "update-firefighter";
	private final String MSG_CONNECTED = "connect";
	private final String MSG_DISCONNECT_FIREFIGHTER = "delete-firefighter";

	private int firefighterId;
	private String firefighterName;
	private static FiSocketHandler fiSocketHandlerInstance;
	private Socket socket;
	public double storedDistance = 0;

	/**
	 * Constructor initializes socket to server url, and generates an Id
	 * Private because this is a Singleton class
	 * @param url Url to connect to
	 * @param name name of the firefighter
	 */

	private FiSocketHandler(String url) {
		initSocket(url);
	}

	private FiSocketHandler() {
	}


	/**
	 * Setter for the firefighter name, also generates an Id
	 */
	public void setName(String name) {
		firefighterName = name;
		firefighterId = name.hashCode();
	}

	/**
	 * Initialize socket connection
	 */
	public void initSocket(String url) {
		try {
			socket = IO.socket(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Emits JSON to socket server
	 * @param eventName the event to emit
	 * @param message the message to send
	 */
	private void sendJSON(String eventName, JSONObject message) {
		socket.emit(eventName, message);
	}

	/**
	 * Constructs FiSocketHandler Singleton
	 */
	public static void initInstance() {
		if (fiSocketHandlerInstance == null) {
			fiSocketHandlerInstance = new FiSocketHandler();
		}
	}

	public static FiSocketHandler getInstance() {
		return fiSocketHandlerInstance;
	}



	/**
	 * Wrapper for socket connect method
	 * Connects firefighter to server, sends name + id
	 * Does nothing if socket is already connected
	 */
	public void connect() {
		if (socket.connected()) return;

		socket.connect();
		JSONObject newFirefighter = new JSONObject();
		try {
			newFirefighter.accumulate("name", firefighterName);
			newFirefighter.accumulate("id", firefighterId);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			sendJSON(MSG_NEW_FIREFIGHTER, newFirefighter);
		}
	}

	/**
	 * Wrapper for socket's disconnect method
	 */
	public void disconnect() throws JSONException {
		// Tell server we're leaving then close connection
		sendUpdate(MSG_DISCONNECT_FIREFIGHTER, new JSONObject()); 
		socket.disconnect();
	}

	/**
     * Sends update to server
	 * @param eventName Name of the event (e.g. NEW-FIREFIGHTER)
	 * @param update Json object to send
	 */
	public void sendUpdate(String eventName, JSONObject update) throws JSONException {
		// append ID + name to object
		update.accumulate("name", firefighterName);
		update.accumulate("id", firefighterId);
		sendJSON(eventName, update);
	}
}

