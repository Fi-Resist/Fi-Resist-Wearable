var express = require("express");
var app = express();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var socketHandler = require("./socketHandler/socketHandler");
var SOCKET_EVT = require("./constant/constant").socket;

app.use(express.static("public"));


app.get("/", function(req, res) {
	res.sendFile(__dirname + "/public/index.html");
});

io.on("connection", function(socket) {
	console.log("a user connected");

	//get msg from firefighter app
	socket.on(SOCKET_EVT.RECEIVE.firefighter, function(msg) {
		socketHandler.emitData(io, msg, SOCKET_EVT.SEND.officer);
	});
});


http.listen(3000, function() {
	console.log("Listening");
});

