angular.module("app")
	.controller("firefighterListCtrl", function($scope, firefighters) {
		$scope.$on("$ionicView.beforeEnter", function() {
			$scope.firefighters = firefighters.get();
		});

		$scope.isInDanger = function(f) {
			return f.bpm > 110;
		};


	});
