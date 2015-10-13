angular.module("app")
	.factory("socket", function(socketFactory) {
		var mySocket = io.connect("http://localhost:3000/");
		return socketFactory({
			ioSocket: mySocket
		});
	});

