/* login.js
 * Handles routes for logging in
 */
module.exports = {
	get: function(req, res) {
		res.sendFile("login.html");
	},
	post: functoin(req, res) {
		res.redirect("/");
	}
};

