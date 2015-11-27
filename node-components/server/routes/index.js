/* routes.js
 * Loads all the routes into the express router
 */

var register = require("./register");
var login    = require("./login");
var router   = require("express").Router();
var passport = require("passport");
var path = require("path");


router.get("/", function(req, res) {
	var p = path.resolve(__dirname + "/../static/landing/index.html");

	if (req.user) {
		p = path.resolve(__dirname + "/../static/app/index.html");
	}

	res.sendFile(p);
});


router.get("/register", register.get);
router.post("/register", register.post);
router.get("/login", login.get);
// Login uses passport's authenticate
router.post("/login", passport.authenticate("local"), login.post);


module.exports = router;

