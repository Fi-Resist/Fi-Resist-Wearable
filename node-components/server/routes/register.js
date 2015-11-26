/* register.js
 * Routes for registering a new user
 */

var Account = require("../model/model").Account;
module.exports = {
	/* get
	 * renders the new user page
	 */ 
	get: function(req, res) {
		res.sendFile("register.html");
	},

	/* post
	 * Registers a new user
	 */
	post: function(req, res, next) {
		console.log("Registering User");
		Account.register(new Account({
			username: req.body.username,
			name: req.body.name
		}), req.body.password, function(err) {
			if (err) {
				console.log("registration error!");
				return next(err);
			}
		});

		console.log("Registration successful");
		res.redirect("/");
	}
};

