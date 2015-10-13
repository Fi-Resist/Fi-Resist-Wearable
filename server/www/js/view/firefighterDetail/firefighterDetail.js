angular.module("app")
	.controller("firefighterDetailCtrl", function($scope, $stateParams, firefighters) {
		$scope.$on("$ionicView.beforeEnter", function() {
			$scope.firefighter = firefighters.get($stateParams.id);
			console.log($scope.firefighter);
		});
	});
