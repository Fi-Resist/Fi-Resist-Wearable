var socketHandler = require("../../socketHandler/socketHandler");
var SOCKET_EVENTS = require("../../constant/constant").socket;
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
		describe("with a proper event", function() {
			it("should emit the data", function() {
				socketHandler.emitData(io, { msg: 'hi' }, SOCKET_EVENTS.send.UPDATE);
				assert.isTrue(io.emit.called);
			});
		});
		describe("with an invalid event", function() {
			it("should not emit the data", function() {
				socketHandler.emitData(io, { msg: 'hi' }, "SOCKET");
				assert.isFalse(io.emit.called);
			});	
		});
	});
});


