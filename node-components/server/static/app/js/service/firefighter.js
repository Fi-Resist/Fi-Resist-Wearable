/* Firefighter service
 * Provides GET and POST methods for accessing firefighters
 */
angular.module("app.services")
	.factory("firefighter", function($http) {

		// Gets the list of firefighters
		var get = function() {
			return $http({
				method: "GET",
				url: "/firefighters"
			});
		};

		var post = function(data) {
			return $http({
				method: "POST",
				url: "/firefighters",
				data: data
			});
		};

		return {
			get: get,
			post: post
		};
	});

