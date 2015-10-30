describe("firefighterList controller", function() {
	var scope, rootScope, firefightersServiceStub, _event, _firefighters;

	beforeEach(function() {
		module('app.controllers');

		inject(function($rootScope, $controller, event, firefighters) {

			if (firefighters.get().length == 0) {
				firefighters.create({
					id: 1
				});
			}

			scope = $rootScope.$new();
			rootScope = $rootScope;
			_event = event;	
			_firefighters = firefighters;

	
			$controller("firefighterListCtrl", {
				$scope: scope,
				firefighters: firefighters
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
				assert.equal(scope.firefighters.length, 1);
			});
		});

		describe("update event", function() {
			beforeEach(function() {
				_firefighters.update({
					id: 1
				});
			});
			it("should put data on the scope", function() {
				assert.equal(scope.firefighters.length, 1);
			});
		});


		describe("create event", function() {
			beforeEach(function() {
				_firefighters.create({
					id: 2
				});
			});
			it("should put data on the scope", function() {
				assert.equal(scope.firefighters.length, 2);
			});
		});
	});
});

