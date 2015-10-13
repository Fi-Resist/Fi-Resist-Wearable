angular.module("app")
	.factory("firefighters", function() {
		var firefighters = [
			{
				name: "John Firefighter",
				id: 1,
				bpm: 90,
				resp: 30
			},
			{
				name: "Bill Firefighter",
				id: 2,
				bpm: 90,
				resp: 30

			},
			{
				name: "Jeff Firefighter",
				id: 3,
				bpm: 140,
				resp: 30

			},
		]



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

		return {
			get: get
		};
	});
