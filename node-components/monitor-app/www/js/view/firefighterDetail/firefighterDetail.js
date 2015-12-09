angular.module("app.controllers")
	.controller("firefighterDetailCtrl", function($scope, $stateParams, firefighters, event) {

		var refreshData = function() {
			var firefighter= firefighters.get($stateParams.id);

			$scope.firefighter = {
				bio: firefighter.bio
			};


			$scope.firefighter.position = _.map(firefighter.position, function(p, index) {
				var direction = "";
				var orientation = p.orientation;
				if (index == 0) {
					direction = "START";
				}
				else {
					var diff = p.orientation - firefighter.position[index - 1].orientation;
					direction = (diff > 0) ? "LEFT" : "RIGHT";

					orientation = Math.abs(diff); 

				}

				return {
					pos: p.pos,
					orientation: orientation,
					direction: direction
				};
			});
		};

		$scope.$on("$ionicView.beforeEnter", refreshData);
		$scope.$on(event.SCOPE_UPDATE, refreshData);
	});
