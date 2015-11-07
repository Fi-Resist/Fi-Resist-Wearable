var socket = io();

var events = [
	"new-firefighter",
	"update-firefighter",
	"update-position",
	"update-biometrics",
	"delete-firefighter",
	"request-info",
	"officer-update"
];


var current;
for (var i = 0; i < events.length; i++) {
	current = events[i];
	socket.on(current, function(msg) {
		$("#data")
			.append("<tr><td>" + current + "</td><td>" + JSON.stringify(msg) + "</td></tr>");
	});
}


