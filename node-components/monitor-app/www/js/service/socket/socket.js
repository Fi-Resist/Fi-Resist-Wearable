// Factory exposes socket.io to angular modules
angular.module("app.services")
	.factory("socket", function(socketFactory, event, firefighters, server) {
		var mySocket = io.connect(server.host);

		var sock = socketFactory({
			ioSocket: mySocket
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

