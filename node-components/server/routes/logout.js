/* logout.js
 * routes for logging out
 */

module.exports = {
	get: function(req, res) {
		req.logout();
		res.redirect("/");
	}
};


