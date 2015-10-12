var express = require("express");
var app = express();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var socketHandler = require("./socketHandler/socketHandler");
var SOCKET_EVT = require("./constant/constant").socket;
var FIREFIGHTERS = [];



app.use(express.static("public"));

app.get("/", function(req, res) {
	res.sendFile(__dirname + "/public/index.html");
});

io.on("connection", function(socket) {
	console.log("a user connected");

	// receive a new firefighter
	socket.on(SOCKET_EVT.receive.NEW, function(msg) {
		FIREFIGHTERS.push(msg);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//update a firefighter's data
	socket.on(SOCKET_EVT.receive.UPDATE, function(msg) {
		var index = _.findIndex(FIREFIGHTERS, {id: msg.id});
		FIREFIGHTERS[index] = msg;

		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});
});


http.listen(3000, function() {
	console.log("Listening");
});

