/* login.js
 * Handles routes for logging in
 */

var path = require("path");

module.exports = {
	get: function(req, res) {
		var p = path.resolve(__dirname + "/../static/login/index.html");
		res.sendFile(p);
	},
	post: function(req, res) {
		res.redirect("/");
	}
};

