/*Angular controller for login feature*/

CHDApp.controller("LoginController", function($scope, $rootScope, $http, $location) {
	
	$scope.authenticationStatus = "";
	$scope.displayClass = "";
	$scope.email = "drighetto@gmail.com";
	$scope.password = "AZERTY12345";
	
	
    //Initialize controller
    $scope.init = function (){};	

    //Define login function
	//[EPLVULN] : Explicit vulnerability in order to allow CSRF (no Anti CSRF token used)
	$scope.authenticate = function() {
		var params = "email=" + $scope.email + "&password=" + $scope.password;
		$http.post(appContextPath + "/login", params, {
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded; charset=UTF-8"
			}
		}).success(function(data, status, headers, config) {
			$scope.authenticationStatus = "Authentication succeed";
			$scope.displayClass = "success";
			$location.path("/profile");
		}).error(function(data, status, headers, config) {
			$scope.authenticationStatus = "Authentication failed !";
			$scope.displayClass = "danger";
		});
	};
	
	//Go to register form
	$scope.register= function() {
		$location.path("/register");
	};

	// Runs once per controller instantiation
	$scope.init();

});