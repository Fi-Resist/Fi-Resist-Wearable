/**
  * Holds the data models for the firesist server
  */
var mongoose      = require("mongoose");
var Schema        = mongoose.Schema;
var passportLocal = require("passport-local-mongoose");


// Account for authentication
var Account = new Schema({
	name: String,
	stationRef: Schema.Types.ObjectId
});

Account.plugin(passportLocal);

// Fire station
var Station = new Schema({
	name: String,
	stationId: String
});

// Firefighter
var FireFighter = new Schema({
	name: String,
	username: String,
	stationRef: Schema.Types.ObjectId
});


// Deployment location + list of firefighters
var Deployment = new Schema({
	location: String,
	firefighters: [Schema.Types.ObjectId],
	timestarted: Date,
	timeEnded: Date
});



module.exports = {
	Account: mongoose.model("Account", Account),
	Station: mongoose.model("Station", Station),
	FireFighter: mongoose.model("FireFighter", FireFighter),
	Deployment: mongoose.model("Deployment", Deployment)
};


