var socketHandler = require("../../socketHandler/socketHandler");
var sinon = require("sinon");
var assert = require("chai").assert;

var io;

describe("socketHandler", function() {
	beforeEach(function() {
		io = {
			emit: sinon.stub()
		};
	});
	describe("emitData", function() {
		it("should emit the data", function() {
			socketHandler.emitData(io, { msg: 'hi' }, "SOCKET");
			assert.isTrue(io.emit.called);
		});
	});
});


