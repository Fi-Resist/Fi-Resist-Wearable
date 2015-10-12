
var emitData = function(io, data, socket_msg) {
	io.emit(socket_msg, data);
};


module.exports = {
	emitData: emitData
};

