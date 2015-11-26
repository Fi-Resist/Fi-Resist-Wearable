var _ = require("lodash");
var SOCKET_EVT = require("../constant/constant").socket;


var emitData = function(io, data, socket_evt) {
	if (_.includes(_.values(SOCKET_EVT.send), socket_evt)) {
		io.emit(socket_evt, data);
	}
};

module.exports = {
	emitData: emitData
};


module.exports = {
	initialize: function(io) {
		io.on("connection", function(socket) {
			console.log("A user connected");
		
			/* A new firefighter has connected
			 * Marks firefighter as monitorable 
			 */
			socket.on(SOCKET_EVT.receive.NEW, function(msg) {
				// TODO
			});
		
			/* update a firefighter's biometrics
			 * msg schema:
			 * {
			 *	id: firefighter id
			 *	bio: biometric data (bpm & resp)
			 * }
			 *  This is purely for transporting to monitoring app
			 */
			socket.on(SOCKET_EVT.receive.UPDATE_BIOMETRICS, function(msg) {
				console.log("Biometrics update");
				emitData(io, msg, SOCKET_EVT.send.UPDATE);
			});
		
		
			/* firefighter has disconnected
		     * Emits to monitor that firefighter is disconnected		
			 */
			socket.on(SOCKET_EVT.receive.DELETE, function(msg) {
				emitData(io, msg, SOCKET_EVT.send.DISCONNECT);
		
			});
		});
	}
};

