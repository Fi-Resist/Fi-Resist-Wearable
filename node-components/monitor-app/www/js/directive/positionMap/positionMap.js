angular.module("app.directives")
	.directive("positionMap", function() {
		return {
			restrict: "A",
			scope: {
				positions: '@'
			},
			link: function(scope, element, attrs) {
				var svg = d3.select(element[0])
					.append("svg");
			}
		}});

