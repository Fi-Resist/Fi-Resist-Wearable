/* routes.js
 * Loads all the routes into the express router
 */

var register = require("./register");
var login    = require("./login");
var router   = require("express").Router();
var passport = require("passport");


router.get("/", function(req, res) {
	if (req.user) {
		res.sendFile("app/index.html");
	}
	else {
		res.sendFile("landing/index.html");
	}
});


router.get("/register", register.get);
router.post("/register", register.post);
router.get("/login", login.get);
// Login uses passport's authenticate
router.post("/login", passport.authenticate("local"), login.post);


module.exports = router;

