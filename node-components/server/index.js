var express = require("express");
var app = express();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var socketHandler = require("./socketHandler/socketHandler");
var SOCKET_EVT = require("./constant/constant").socket;
var FIREFIGHTERS = [];
var _ = require("lodash");



app.use(express.static("www"));

app.get("/", function(req, res) {
	res.sendFile(__dirname + "/public/index.html");
});

app.post("/post", function(req, res) {
	console.log("POST!");
	console.log(req);
});

io.on("connection", function(socket) {
	console.log("a user connected");

	// receive a new firefighter
	socket.on(SOCKET_EVT.receive.NEW, function(msg) {
		console.log(msg);

		/**
		 * Firefighter object:
		 *  id: unique identifier
		 * 	bio: Object containing name & biometric data
		 *  position: array of data points showing movement history
		 */
		var newFirefighter = {
			id: msg.id,
			bio: _.omit(msg, "id"),
			position: []
		};
		FIREFIGHTERS.push(newFirefighter);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//update a firefighter's biometrics
	socket.on(SOCKET_EVT.receive.UPDATE_BIOMETRICS, function(msg) {
		var index = _.findIndex(FIREFIGHTERS, {id: msg.id});
		FIREFIGHTERS[index].bio = msg;
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//update a firefighters position
	socket.on(SOCKET_EVT.receive.UPDATE_POSITION, function(msg) {
		var index = _.findIndex(FIREFIGHTERS, {id: msg.id});

		//Firefighters position is an array of data points
		FIREFIGHTERS[index].position.push(msg);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});


});


http.listen(3000, function() {
	console.log("Listening");
});

