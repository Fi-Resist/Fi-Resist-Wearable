/**
  * Holds the data models for the firesist server
  */
var mongoose      = require("mongoose");
var Schema        = mongoose.Schema;
var passportLocal = require("passport-local-mongoose");


// Account for authentication
var Account = new Schema({
	name: String,
});
Account.plugin(passportLocal);

// Fire station
var Station = new Schema({
	name: String,
})

// Firefighter
var FireFighter = new Schema({
	name: String,
	username: String,
	stationRef: Number
});


// Deployment location + list of firefighters
var Deployment = new Schema({
	location: String,
	firefighters: [Number]
});



module.exports = {
	Account: mongoose.model("Account", Account),
	Station: mongoose.model("Station", Station),
	FireFighter: mongoose.model("FireFighter", FireFighter),
	Deployment: mongoose.model("Deployment", Deployment)
};


