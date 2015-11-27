angular.module("app.controllers")
	.controller("ToolbarCtrl", function($scope, user) {
		$scope.user = user.name;
	});
