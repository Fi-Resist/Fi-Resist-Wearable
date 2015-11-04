// Set of constants (mostly for sockets
module.exports = {
	socket: {
		//These are the messages sent from the wearable
		receive: {
			NEW: "new-firefighter",
			UPDATE: "update-firefighter",
			UPDATE_BIOMETRICS: "update-biometrics",
			UPDATE_POSITION: "update-position",
			DELETE: "delete-firefighter"
		},
		// These are the messages to be sent to the monitoring application
		send: {
			UPDATE: "officer-update"
		}
	}
};



