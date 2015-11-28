// Fetches the logged in user
angular.module("app.services")
	.factory("user", function($http) {
		var get = function() {
			return $http({
				method: "GET",
				url: "/account"
			});
		};

		return {
			get: get
		}
	});


