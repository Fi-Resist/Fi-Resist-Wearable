// Set of constants (mostly for sockets)
module.exports = {
	socket: {
		//These are the messages sent from the wearable
		receive: {
			NEW: "new-firefighter", // A new firefighter has connected
			UPDATE: "update-firefighter", //A firefighter has updated information
			UPDATE_BIOMETRICS: "update-biometrics", //A firefighter has updated biometrics
			UPDATE_POSITION: "update-position", //A firefighter has updated positional data
			DELETE: "delete-firefighter", // A firefighter has been removed
			REQUEST_INFO: "request-info" // Someone wants the firefighter information
		},
		// These are the messages to be sent to the monitoring application
		send: {
			UPDATE: "officer-update" // Broadcast the firefighter information
		}
	}
};



