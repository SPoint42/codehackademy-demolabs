/*Angular controller for admin feature*/

CHDApp.controller("AdminController", function($scope, $rootScope, $http, $location) {

	$scope.processingStatus = "";
	$scope.displayClass = "";
	$scope.xmlContent = "";
	$scope.newMotivation = "";

	// Initialize controller
	$scope.init = function() {
		// Get XML from sample file
		$http.get(appContextPath + "/app/files/xxe-sample.xml")
		.success(function(data, status, headers, config) {
			$scope.xmlContent = data;
		});
	};

	// Upload function
	$scope.import = function() {
		if ($scope.xmlContent != null && $scope.xmlContent != "") {
			var params = "XML=" + encodeURIComponent($scope.xmlContent);
			$http.post(appContextPath + "/services/import", params, {
				headers : {
					"Content-Type" : "application/x-www-form-urlencoded; charset=UTF-8"
				}
			}).success(function(data, status, headers, config) {
				if (data == null || data == "") {
					$scope.processingStatus = "Import failed !";
					$scope.displayClass = "danger";
				} else {
					tmpStr = "(";
					for (var property in data) {
						tmpStr += property + ":" + data[property] + " ";
					}
					tmpStr = tmpStr.trim() +  ")";
					$scope.processingStatus = "Import succeed, see details for each student by position: " + tmpStr;
					$scope.displayClass = "success";
				}
			}).error(function(data, status, headers, config) {
				$scope.processingStatus = "Import failed !";
				$scope.displayClass = "danger";
			});
		} else {
			$scope.processingStatus = "Content not specified !";
			$scope.displayClass = "warning";
		}
	};
	

	// Update motivation function
	$scope.updateMotivation = function() {
		if ($scope.newMotivation != null && $scope.newMotivation != "") {
			var params = "MOTIVATION=" + encodeURIComponent($scope.newMotivation);
			$http.post(appContextPath + "/services/updateMotivation", params, {
				headers : {
					"Content-Type" : "application/x-www-form-urlencoded; charset=UTF-8"
				}
			}).success(function(data, status, headers, config) {
				$scope.processingStatus = "Update succeed !";
				$scope.displayClass = "success";
			}).error(function(data, status, headers, config) {
				$scope.processingStatus = "Update failed !";
				$scope.displayClass = "danger";
			});			
		} else {
			$scope.processingStatus = "New motivation not specified !";
			$scope.displayClass = "warning";
		}
	}
	
	
	//Go to profile area
	$scope.profile= function() {
		$location.path("/profile");
	}; 	
	
    //Disconnection action
    $scope.logout = function(){
		$http.get(appContextPath + "/logout")
		.success(function(data, status, headers, config) {
			$scope.processingStatus = "";
			$scope.displayClass = "";			
			$location.path("/login");
		}).error(function(data, status, headers, config) {
			$scope.processingStatus = "Error occur during logout !";
			$scope.displayClass = "danger";
		}); 
    };	

	// Runs once per controller instantiation
	$scope.init();

});