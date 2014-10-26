/*Angular controller for registration feature*/

CHDApp.controller("RegisterController", function($scope, $rootScope, $http, $location, $sce) {
	
	$scope.registrationStatus = "";
	$scope.displayClass = "";
	$scope.email = null;
	$scope.lastnameFirstname = null;
	$scope.password = null;
	$scope.motivation = null;
	
    //Initialize controller
    $scope.init = function (){};				
	
    //Disconnection action
    $scope.register = function(){
    	//Buld json object
    	var student = {};
    	student["email"] = $scope.email;
    	student["lastnameFirstname"] = $scope.lastnameFirstname;
    	student["password"] = $scope.password;
    	student["motivation"] = $scope.motivation;    	
    	student["classesRegistered"] = new Array();    	
    	//Call registration service
    	$http.post(appContextPath + "/services/register", student, {
			headers : {
				"Content-Type" : "application/json; charset=UTF-8"
			}
		}).success(function(data, status, headers, config) {
			if(!data){
				$scope.registrationStatus = "Error occur during registration !";
				$scope.displayClass = "danger";				
			}else{
				$scope.registrationStatus = "";
				$scope.displayClass = "";			
				$location.path("/login");
			}
		}).error(function(data, status, headers, config) {
			$scope.registrationStatus = "Error occur during registration !";
			$scope.displayClass = "danger";
		}); 
    };
    
   
    
   
	// Runs once per controller instantiation
	$scope.init();
	
});