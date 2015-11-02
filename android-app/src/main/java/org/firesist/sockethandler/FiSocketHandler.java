package org.firesist.sockethandler;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URISyntaxException;

/**
  * Web socket communication for FiResist
  */
public class FiSocketHandler {

	// Socket message constants
	private final String MSG_NEW_FIREFIGHTER = "new-firefighter";
	private final String MSG_UPDATE_FIREFIGHTER = "update-firefighter";
	private int firefighterId;
	private String firefighterName;
	private static FiSocketHandler fiSocketHandlerInstance;
	private Socket socket;

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
	 */
	public void connect() {
		socket.connect();
		JSONObject newFirefighter = new JSONObject();
		try {
			newFirefighter.append("name", firefighterName);
			newFirefighter.append("id", firefighterId);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			sendJSON(MSG_NEW_FIREFIGHTER, newFirefighter);
		}
	}

	/**
	 * Wrapper for socket's disconnect method
	 */
	public void disconnect() {
		socket.disconnect();
	}

	/**
     * Sends update to server
	 * @param json Json object to send
	 */
	public void sendUpdate(JSONObject update) throws JSONException {
		// append ID + name to object
		update.append("name", firefighterName);
		update.append("id", firefighterId);
		sendJSON(MSG_UPDATE_FIREFIGHTER, update);
	}
}

