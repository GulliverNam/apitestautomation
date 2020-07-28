<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Swagger test</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	</head>
	<body>
		<form action="/swagger/upload" method="post" enctype="multipart/form-data">
			<input type="file" id="apidoc" name="apidoc">
			<button type="submit">Swagger 파일 업로드(.json)</button>
		</form>
	</body>
</html>