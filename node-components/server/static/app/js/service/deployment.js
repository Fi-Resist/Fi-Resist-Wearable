angular.module("app.services")
	.factory("deployment", function($http) {
		var get = function() {
			return $http({
				method: "GET",
				url: "/deployments"
			});
		};

		return {
			get: get
		};
	});
