<!DOCTYPE html>
<html>
	<%-- Special page created in order to provide the student import feature in "non Single Page Application" style --%>
	<%-- The objective is to allow XXE vulnerability exploitation using tools like BEEF --%>
	<head>
		<meta charset="ISO-8859-1">
		<title>Student RAW import mode</title>
		<!-- Load JQuery (Bootstrap dependency) -->
		<script src="${pageContext.request.contextPath}/app/js/jquery.js"></script>
		<!-- Load Bootstrap -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/bootstrap.css" type="text/css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/bootstrap-theme.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/app/js/bootstrap.js"></script>
	</head>
	<body>
		<form action="${pageContext.request.contextPath}/services/import" enctype="application/x-www-form-urlencoded" method="POST" autocomplete="off" >
			<label for="XML">Copy/Paste complete XML file containing students informations to register:</label>
			<textarea id="XML" name="XML" autofocus="autofocus" ng-model="xmlContent" class="form-control" rows="10">
<?xml version="1.0" encoding="UTF-8"?>
<Students>
	<Student>
		<Email>d@d.com</Email>
		<LastnameFirstname>XXE test user</LastnameFirstname>
		<Password>azertyuiop</Password>
		<Motivation>I want to learn application security stuff</Motivation>
	</Student>
</Students>						
			</textarea>
			<input type="submit" value="Import">
		</form>		
	</body>
</html>