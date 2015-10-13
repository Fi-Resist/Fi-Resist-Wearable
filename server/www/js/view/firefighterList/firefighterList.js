angular.module("app")
	.controller("firefighterListCtrl", function($scope, firefighters, event) {

		var refreshData = function() {
			$scope.firefighters = firefighters.get();
		};

		$scope.$on("$ionicView.beforeEnter", refreshData);
		$scope.$on(event.SCOPE_CREATE, refreshData);

		$scope.isInDanger = function(f) {
			return f.bpm > 110;
		};
	});

