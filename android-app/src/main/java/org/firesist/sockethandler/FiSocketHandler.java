package org.firesist.sockethandler;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URISyntaxException;

/**
  * Web socket communication for FiResist
  */
public class FiSocketHandler extends SocketHandler {

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
		super(url);
		firefighterName = name;
		firefighterId = name.hashCode();
	}

	/*
	 * Wrapper for socket connect method
	 * Connects firefighter to server, sends name + id
	 */
	@Override
	public void connect() {
		super.connect();
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

	/*
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

