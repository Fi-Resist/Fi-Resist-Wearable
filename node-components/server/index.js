var express = require("express");
var app = express();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var socketHandler = require("./socketHandler/socketHandler");
var SOCKET_EVT = require("./constant/constant").socket;
var FIREFIGHTERS = [];
var _ = require("lodash");



app.use(express.static("server/static"));

app.get("/", function(req, res) {
	res.sendFile(__dirname + "/static/index.html");
});

app.post("/post", function(req, res) {
	console.log("POST!");
	console.log(req);
});

io.on("connection", function(socket) {
	console.log("a user connected");

	// receive a new firefighter
	socket.on(SOCKET_EVT.receive.NEW, function(msg) {
		console.log("New Event received");
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
		console.log(FIREFIGHTERS);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//update a firefighter's biometrics
	socket.on(SOCKET_EVT.receive.UPDATE_BIOMETRICS, function(msg) {
		console.log("Biometrics update");
		var index = _.findIndex(FIREFIGHTERS, {id: msg.id});
		FIREFIGHTERS[index].bio = msg;
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//update a firefighters position
	socket.on(SOCKET_EVT.receive.UPDATE_POSITION, function(msg) {
		console.log("Position Update");
		var index = _.findIndex(FIREFIGHTERS, {id: msg.id});

		//Firefighters position is an array of data points
		FIREFIGHTERS[index].position.push(msg);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});

	//Firefighter disconnects app
	socket.on(SOCKET_EVT.receive.DELETE, function(msg) {
		console.log("Delete request received");
		_.remove(FIREFIGHTERS, function(firefighter) {
			return firefighter.id == msg.id;
		});
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
		console.log(FIREFIGHTERS);
	});

	// Chief requests info
	socket.on(SOCKET_EVT.receive.REQUEST_INFO, function(msg) {
		console.log(FIREFIGHTERS);
		socketHandler.emitData(io, FIREFIGHTERS, SOCKET_EVT.send.UPDATE);
	});


});

var port = process.env.PORT || 3000

http.listen(port, function() {
	console.log("Listening on 3000");
});

