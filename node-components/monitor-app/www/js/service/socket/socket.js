// Factory exposes socket.io to angular modules
angular.module("app.services")
	.factory("socket", function(socketFactory, event, firefighters, server) {
		var mySocket = io.connect(server.host);

		var sock = socketFactory({
			ioSocket: mySocket
		});

		//on connection, request ff data
		sock.on("connect", function(evt, data) {
				io.emit(event.REQUEST_INFO);
		});

		sock.on(event.SOCKET_CREATE, function(evt, data) {
			//create
			firefighters.create(data);
		});

		sock.on(event.SOCKET_UPDATE, function(evt, data) {
			//update
			firefighters.update(data);
		});

		return sock;
	});

