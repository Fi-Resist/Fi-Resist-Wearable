describe("firefighterDetail controller", function() {
	var scope, firefightersServiceStub, rootScope, _event;

	beforeEach(function() {
		module("app.controllers");
		inject(function($rootScope, $controller, event) {
			scope = $rootScope.$new();
			_event = event;
			rootScope = $rootScope;

			firefightersServiceStub = {
				get: function(id) {
					return {
						id: id
					};
				}
			};

			$controller("firefighterDetailCtrl", {
				$scope: scope,
				$stateParams: {id: 1},
				firefighters: firefightersServiceStub
			});
		});
	});

	describe("event listeners", function() {
		describe("enter view", function() {
			beforeEach(function() {
				rootScope.$broadcast("$ionicView.beforeEnter");
				scope.$apply();
			});
			it("should put data on the scope", function() {
				assert.equal(scope.firefighter.id, 1);
			});
		});

		describe("update event", function() {
			beforeEach(function() {
				rootScope.$broadcast(_event.SCOPE_UPDATE);
				scope.$apply();
			});
			it("should put data on the scope", function() {
				assert.equal(scope.firefighter.id, 1);
			});
		});
	});
});

