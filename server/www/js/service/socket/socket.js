// Factory exposes socket.io to angular modules
angular.module("app")
	.factory("socket", function(socketFactory, socketEvent) {
		var mySocket = io.connect("http://localhost:3000/");

		var sock = socketFactory({
			ioSocket: mySocket
		});

		// forward socket events to $scope events
		sock.forward(socketEvent.UPDATE);

		return sock;
	});

