angular.module("app.controllers")
	.controller("RosterCtrl", function($scope, $state, firefighter) {

		// Form submission fn: add a new firefighter
		$scope.submit = function() {
			alert("submit!");
			if ($scope.newFirefighter) {
				firefighter.post({name: $scope.newFirefighter})
					.then(function() {
						$state.go(".", {}, {reload: true});
					});
			}
		}
	});

