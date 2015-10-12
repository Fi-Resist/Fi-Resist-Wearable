var _ = require("lodash");
var SOCKET_EVENT = require("../constant/constant").socket;
var emitData = function(io, data, socket_evt) {
	if (_.includes(_.values(SOCKET_EVENT.send), socket_evt)) {
		io.emit(socket_evt, data);
	}
};

module.exports = {
	emitData: emitData
};

