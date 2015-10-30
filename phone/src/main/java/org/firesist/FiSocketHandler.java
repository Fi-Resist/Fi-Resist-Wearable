package org.firesist;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URISyntaxException;

public class FiSocketHandler {
	private Socket socket;

	// Socket message constants
	private final String MSG_NEW_FIREFIGHTER = "new-firefighter";
	private final String MSG_UPDATE_FIREFIGHTER = "update-firefighter";

	private int firefighterId;
	private String firefighterName;

	/*
	 * Constructor initializes socket to server url, and generates an Id
	 * @param url Url to connect to
	 * @param name name of the firefighter
	 */
	public FiSocketHandler(String url, String name) {
		try {
			socket = IO.socket(url);
			firefighterName = name;
			firefighterId = name.hashCode();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/*
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
		}

		socket.emit(MSG_NEW_FIREFIGHTER, newFirefighter); 
	}

	/*
	 * Wrapper for socket's disconnect method
	 */
	public void disconnect() {
		socket.disconnect();
	}

	/*
     * Sends update to server
	 * @param json Json object to send
	 */
	public void sendUpdate(JSONObject json) throws JSONException {
		// append ID + name to object
		json.append("name", firefighterName);
		json.append("id", firefighterId);
		socket.emit(MSG_UPDATE_FIREFIGHTER, json); 
	}
}

