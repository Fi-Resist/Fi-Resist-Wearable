/* firefighter.js
 * Handle routes for creating and fetching firefighters
 */

var FireFighter = require("../model/model").FireFighter;

module.exports = {
	get: function(req, res) {
		FireFighter.find({ "stationRef": req.body.stationRef}, function(err, firefighters) {
			res.send(firefighters);
		});
	},
	post: function(req, res) {
	}
};



