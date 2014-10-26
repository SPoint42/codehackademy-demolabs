/* Script used to define and configure Angular application */

//Define Angular application
var CHDApp = angular.module("CHDApp", [ "ngRoute", "ngSanitize" ]);

// Define client side MVC routes
CHDApp.config([ "$routeProvider", function($routeProvider) {
	$routeProvider.when("/login", {
		templateUrl : "app/login.html",
		controller : "LoginController"
	}).when("/profile", {
		templateUrl : "app/profile.html",
		controller : "ProfileController"
	}).when("/register", {
		templateUrl : "app/register.html",
		controller : "RegisterController"
	}).when("/admin", {
		templateUrl : "app/admin.html",
		controller : "AdminController"
	}).otherwise({
		redirectTo : "/login"
	});
} ]);