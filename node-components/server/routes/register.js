/* register.js
 * Routes for registering a new user
 */

var path    = require("path");
var Model   = require("../model/model");
var Account = Model.Account;
var Station = Model.Station;

module.exports = {
	/* get
	 * renders the new user page
	 */ 
	get: function(req, res) {
		var p = path.resolve(__dirname + "/../static/register/index.html");
		res.sendFile(p);

	},

	/* post
	 * Registers a new user
	 */
	post: function(req, res, next) {
		console.log("Registering User");

		Station.findOne({ "stationId": req.body.station}, function(err, station) {
			Account.register(new Account({
				username: req.body.username,
				name: req.body.name,
				stationRef: station._id
			}), req.body.password, function(err) {
				if (err) {
					console.log("registration error!");
					return next(err);
				}

				console.log("Registration successful");
				res.redirect("/");
			});
		});

	}
};

