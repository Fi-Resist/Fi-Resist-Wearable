// wrapper to do an ionic build 

var exec = require("child_process").exec;
process.chdir("monitor-app");
exec("ionic build", function(err, stdout, stderr) {
	if (err) {
		console.log("child failed with error code: " + err.code);
	}
	console.log(stdout);
	console.log(stderr);
});




