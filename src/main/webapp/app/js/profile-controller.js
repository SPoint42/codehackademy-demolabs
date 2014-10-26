/*Angular controller for student profile feature*/

CHDApp.controller("ProfileController", function($scope, $rootScope, $http, $location, $sce) {
	
	$scope.processingStatus = "";
	$scope.displayClass = "";
	$scope.classesCollection = null;
	$scope.studentProfile = null;
	$scope.isAdmin = false;
		
	//[EPLVULN] Allow direct rendering of the URL (Angular sub part) specified in url bar field in order to allow DOM XSS
	$scope.breadcrumb = "Breadcrumb: " + decodeURIComponent($location.url());
	$scope.trustedBreadcrumb = $sce.trustAsHtml($scope.breadcrumb);  	
	
    //Initialize controller
    $scope.init = function (){
    	     	    
    	//Load student admin status
		$http.get(appContextPath + "/services/isadmin")
		.success(function(data, status, headers, config) {
			$scope.isAdmin = data;			
		}).error(function(data, status, headers, config) {
			$scope.isAdmin = false;
		});     	
    	    	
    	//Load student profile
    	//Load collection of available CH classes
    	//Prepare rending of registred classes
		$http.get(appContextPath + "/services/profile")
		.success(function(data, status, headers, config) {
			//Set profile basic data
			$scope.studentProfile = data;
			$scope.processingStatus = "";
			$scope.displayClass = "";
			//[EPLVULN] Allow direct rendering of the "motivation" field in order to allow STORED XSS 
			$scope.studentProfile.trustedMotivation = $sce.trustAsHtml($scope.studentProfile.motivation);			
	    	//Load collection of available CH classes
			$http.get(appContextPath + "/services/classes")
			.success(function(data, status, headers, config) {
				$scope.classesCollection = data;
				$scope.processingStatus = "";
				$scope.displayClass = "";
				//Prepare rendering of registred classes by dynamically adding a property named "selected" 
				//representing the registration status for this classes for the current student
				var lRef = $scope.classesCollection;
				var lRef2 = $scope.studentProfile.classesRegistered
				for(var x = 0 ; x < lRef.length ; x++){
					lRef[x].selected = false;
					if(lRef2 != null){
						for(var y = 0 ; y < lRef2.length ; y++){
							if(lRef2[y] == lRef[x].sessionIdentifier){
								lRef[x].selected = true;
								break;
							}
						}
					}					
				}				
			}).error(function(data, status, headers, config) {
				$scope.processingStatus = "Error occur during data processing !";
				$scope.displayClass = "danger";
			});     				
		}).error(function(data, status, headers, config) {
			$scope.processingStatus = "Error occur during data processing !";
			$scope.displayClass = "danger";
		});
		
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
    
    //Update registration action
    $scope.update = function(){
    	//Gather new classes selection
    	var selectedClasses = new Array();
    	for(x = 0 ; x < $scope.classesCollection.length ; x++){
    		if($scope.classesCollection[x].selected){$
    			var chc = {};
    			chc["sessionIdentifier"] = $scope.classesCollection[x].sessionIdentifier;
    			chc["label"] = $scope.classesCollection[x].label;
    			selectedClasses.push(chc);
    		}
    	}
    	//Call update service
		$http.post(appContextPath + "/services/update", selectedClasses, {
			headers : {
				"Content-Type" : "application/json; charset=UTF-8"
			}
		}).success(function(data, status, headers, config) {
			if(!data){
				$scope.processingStatus = "Registration update failed !";
				$scope.displayClass = "danger";
			}else{
				$scope.processingStatus = "Registration update succeed !";
				$scope.displayClass = "success";				
			}
		}).error(function(data, status, headers, config) {
			$scope.processingStatus = "Registration update failed !";
			$scope.displayClass = "danger";
		});   	   	
    };
       
	//Go to admin area
	$scope.administer= function() {
		$location.path("/admin");
	};    
      
	// Runs once per controller instantiation
	$scope.init();
		
});