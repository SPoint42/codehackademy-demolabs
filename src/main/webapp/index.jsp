<!DOCTYPE html>
<html id="ng-app" ng-app="CHDApp">
<head>
	<meta http-equiv="content-type" content="text/html" charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Code Hackademy Demo</title>
	<!-- Define JS constants -->
	<script type="text/javascript">
		var appContextPath = "${pageContext.request.contextPath}";
	</script>	
	<!-- Load JQuery (Bootstrap dependency) -->
	<script src="${pageContext.request.contextPath}/app/js/jquery.js"></script>
	<!-- Load Bootstrap -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/bootstrap.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/bootstrap-theme.css" type="text/css" />
	<script src="${pageContext.request.contextPath}/app/js/bootstrap.js"></script>
	<!-- Load style for Checkboxes -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/checkbox.css" type="text/css" />
	<!-- Load Angular -->
	<script src="${pageContext.request.contextPath}/app/js/angular.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/angular-route.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/angular-sanitize.js"></script>
	<!-- Load client application Angular components -->
	<script src="${pageContext.request.contextPath}/app/js/application.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/login-controller.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/profile-controller.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/register-controller.js"></script>
	<script src="${pageContext.request.contextPath}/app/js/admin-controller.js"></script>
</head>
<body role="document">
    <!-- Angular templating -->
    <!-- this is where content will be injected -->
    <div ng-view class="fluid-container main-container"></div>
</body>
</html>