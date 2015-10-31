describe("firefighters service", function() {
	var _firefighters;
	beforeEach(function() {
		module('app.services')
		inject(function(firefighters) {
			_firefighters = firefighters;
		});
	});

	describe("create method", function() {
		beforeEach(function() {
			_firefighters.remove();
		});
		it("should create a new firefighter", function() {
			assert.isTrue(_firefighters.get().length == 0);
			_firefighters.create({ id: 1});
			assert.equal(_firefighters.get().length, 1);
		});
	});

	describe("remove method", function() {
		beforeEach(function() {
			_firefighters.create({id: 1});
			_firefighters.create({id: 2});
		});
		describe("with no arguments", function() {
			it("should remove all firefighters", function() {
				_firefighters.remove();
				assert.equal(_firefighters.get().length, 0);
			});
		});
		
		describe("with an id passed in", function() {
			it("should remove the specified firefighter", function() {
				_firefighters.remove(1);
				var index = _.findIndex(_firefighters.get(), { id: 1 });
				assert.equal(index, -1);
			});
		});
	});


	describe("get method", function() {
		beforeEach(function() {
			_firefighters.remove();
			_firefighters.create({
				id: 1
			});
			_firefighters.create({
				id: 2
			});
		});
		describe("with no arguments", function() {
			it("should get all the firefighters", function() {
				var ff = _firefighters.get();
				assert.equal(ff.length, 2, "length is 2");
			});
		});

		describe("with an id passed in", function() {
			it("should get the firefighter with that id", function() {
				var ff = _firefighters.get(1);
				assert.equal(ff.id, 1, "id is 1");
			});
		});
	});


	describe("update method", function() {
		beforeEach(function() {
			_firefighters.remove();
			_firefighters.create({
				id: 1,
				name: "Bill"
			});
		});

		it("should update the firefighter", function() {
			_firefighters.update({
				id: 1,
				name: "Sue"
			});

			var ff = _firefighters.get(1);
			assert.equal(ff.name, "Sue", "name change");
		});
	});



});
