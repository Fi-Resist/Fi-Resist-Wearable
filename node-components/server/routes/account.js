/* account.js
 * Fetches account information for authenticated user
 */

module.exports = {
	get: function(req, res) {
		res.send(req.user);
	}
}



