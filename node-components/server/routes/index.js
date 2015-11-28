/* routes.js
 * Loads all the routes into the express router
 */

var register    = require("./register");
var firefighter = require("./firefighter");
var login       = require("./login");
var logout      = require("./logout");
var account     = require("./account");
var router      = require("express").Router();
var passport    = require("passport");
var path        = require("path");


router.get("/", function(req, res) {
	var p = path.resolve(__dirname + "/../static/landing/index.html");

	if (req.user) {
		p = path.resolve(__dirname + "/../static/app/index.html");
	}

	res.sendFile(p);
});

// Middleware to check authentication
// Sends a 401 if the user is not logged in
var checkAuth = function(req, res, next) {
	if (!req.user) {
		res.status(401).send("Auth required");
	}
	else {
		next();
	}
}


router.get("/register"     , register.get);
router.post("/register"    , register.post);
router.get("/login"        , login.get);
router.get("/logout"       , logout.get);
router.post("/login"       , passport.authenticate("local") , login.post);
router.get("/firefighter"  , checkAuth                      , firefighter.get);
router.post("/firefighter" , checkAuth                      , firefighter.post);
router.get("/account"      , checkAuth                      , account.get);


module.exports = router;

