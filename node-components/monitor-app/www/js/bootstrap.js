/**
 * Bootstraps angular application and loads config file
 */

angular.element(document)
	.ready(function() {

		//Load config file into constant
		var $http = angular.injector(["ng"]).get("$http");

		$http.get("config.json")
			.success(function(response) {
				// set up constant holding our server config
				angular.module("app.constants")
					.constant("server", response);
				// bootstrap angular application
				angular.bootstrap(document, ["app"]);
			});
	});

