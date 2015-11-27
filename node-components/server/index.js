var express       = require("express");
var app           = express();
var http          = require("http").Server(app);
var io            = require("socket.io")(http);
var bodyParser    = require("body-parser");
var socketHandler = require("./socketHandler/socketHandler");
var cookieParser  = require("cookie-parser");
var session       = require("cookie-session");
var mongoose      = require("mongoose");
var passport      = require("passport");
var LocalStrategy = require("passport-local").Strategy;


app.use(express.static("server/static"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(session({keys: ["secretkey"]}));

app.use(passport.initialize());
app.use(passport.session());

//Configure auth handlers
var Account = require("./model/model").Account;
passport.use(new LocalStrategy(Account.authenticate()));
passport.serializeUser(Account.serializeUser());
passport.deserializeUser(Account.deserializeUser());

//initialize socket.io handler
socketHandler.initialize(io);

//register routes
app.use("/", require("./routes/index"));

// Connect to mongodb
var mongoHost = "mongodb://";
if (process.env.DB_HOST) {
	mongoHost += process.env.DB_HOST;
}
else {
	mongoHost = "mongodb://localhost/firesist";
}

mongoose.connect(mongoHost, function(err) {
	if (err) {
		console.log("connection error");
	}
});



var port = process.env.PORT || 3000

http.listen(port, function() {
	console.log("Listening on 3000");
});

