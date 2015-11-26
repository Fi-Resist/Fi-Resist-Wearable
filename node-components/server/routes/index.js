/* routes.js
 * Loads all the routes into the express router
 */

var register = require("./register");
var login    = require("./login");
var router   = require("express").Router();



router.get("/register", register.get);
router.post("/register", register.post);
router.get("/login", login.get);
router.post("/login", login.post);


module.exports = router;

