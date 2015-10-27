var assert = require("chai").assert; 
var proxyquire = require("proxyquire");
var sinon = require("sinon");


var socketStub = {
	on: sinon.stub()
};


var ioStub = function() {
	return {
		on: sinon.stub().callsArgWith(1, socketStub)
	};
};


var httpStub = {
	Server: sinon.stub().returns({
		listen: sinon.stub()
	})
};

var index = proxyquire("../index.js", { 
	"socket.io": ioStub,
	"http": httpStub
});

describe("index", function() {
	it("should initialize socket listeners", function() {
		assert.isTrue(socketStub.on.calledTwice, "socket called twice");
	});

});
