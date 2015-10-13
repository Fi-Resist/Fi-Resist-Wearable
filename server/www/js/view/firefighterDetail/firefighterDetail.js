angular.module("app.controllers")
	.controller("firefighterDetailCtrl", function($scope, $stateParams, firefighters, event) {

		var refreshData = function() {
			$scope.firefighter = firefighters.get($stateParams.id);
		};

		$scope.$on("$ionicView.beforeEnter", refreshData);
		$scope.$on(event.SCOPE_UPDATE, refreshData);
	});
