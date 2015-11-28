angular.module("app", ["ui.router", "ui.bootstrap", "app.services", "app.controllers"])

	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider.state("app", {
			url: "",
			abstract: "true",
			templateUrl: "app/js/view/toolbar/toolbar.html",
			controller: "ToolbarCtrl",
			resolve: {
				user: function(user) {
					return user.get();
				}
			}
		})
		.state("app.home", {
			url: "/home",
			templateUrl: "app/js/view/home/home.html", 
			controller: "HomeCtrl"
		})
		.state("app.roster", {
			url: "/roster",
			templateUrl: "app/js/view/roster/roster.html",
			controller: "RosterCtrl"
		})
		.state("app.deployment", {
			url: "/deployment",
			templateUrl: "app/js/view/deployment/deployment.html",
			controller: "DeploymentCtrl"
		});

		$urlRouterProvider.otherwise("/home");
	});


