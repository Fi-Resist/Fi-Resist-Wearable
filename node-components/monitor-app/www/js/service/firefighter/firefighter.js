// firefighter service
// internaly stores firefighters with create, update, read services
angular.module("app.services")
	.factory("firefighters", function($rootScope, event) {
		var firefighters = [];

		
		var images = [
			{pic: 'F1.jpg'},
			{pic: 'F2.jpg'},
			{pic: 'F3.jpg'},
			{pic: 'F4.jpg'},
			{pic: 'F5.jpg'},
			{pic: 'F6.jpg'},
			{pic: 'F7.jpg'}
		];


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
			var index = _.findIndex(firefighters, { id: fighter.id });
			firefighters[index] = fighter;
			$rootScope.$broadcast(event.SCOPE_UPDATE);
		};

		var create = function(fighter) {
			// create new firefighter
			firefighters.push(fighter);
			$rootScope.$broadcast(event.SCOPE_CREATE);
		};

		/**
		 * initializes firefighters from array
		 */
		var initFirefighterArray = function(firefighterArr) {
			firefighters = firefighterArr;
		};
/*
		var findimagebyID = function(id) {
			if (id) {
				var avatar = firefighteravatars[id];
				return avatar
			}
			else
				return null;
			
		};
*/
		var remove = function(id) {
			if (id) {
				_.remove(firefighters, function(f) {
					return f.id === id;
				});
			}
			else {
				firefighters = [];
			}
		}

		return {
			get: get,
			update: update,
			create: create,
			initFirefighterArray: initFirefighterArray,
			remove: remove
			/*findimagebyID; findimagebyID*/
		};
	});
