angular.module("app.controllers")
	.controller("firefighterMapCtrl", function($scope, firefighters) {
		var refreshData = function() {
			$scope.firefighters = firefighters.get();
		};

		$scope.$on("$ionicView.beforeEnter", refreshData);
	});
