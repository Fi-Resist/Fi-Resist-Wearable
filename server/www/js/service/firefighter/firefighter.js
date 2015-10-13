// firefighter service
// internaly stores firefighters with create, update, read services
angular.module("app")
	.factory("firefighters", function($rootScope, event) {
		var firefighters = [];

		var get = function(id) {
			if (id) {
				for (var i = 0; i < firefighters.length; i++) {
					if (firefighters[i].id == id) return firefighters[i]
				}
			}
			else {
				return firefighters;
			}
			return null;
		};

		var update = function(fighter) {
			// update firefighter
			var index = _.indexOf(firefighters, { id: fighter.id });
			firefighters[id] = fighter;
			$rootScope.$broadcast(event.SCOPE_UPDATE);
		};

		var create = function(fighter) {
			// create new firefighter
			firefighters.push(figher);
			$rootScope.$broadcast(event.SCOPE_CREATE);
		};

		return {
			get: get,
			update: update,
			create: create
		};
	});
