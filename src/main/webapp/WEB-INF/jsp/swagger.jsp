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
		<div align="center">
			<h3>명세서 파일을 등록해주세요.(.yaml, .yml, .json)</h3>
			<form action="/upload" method="post" enctype="multipart/form-data">
				<input type="file" id="apidoc" name="apidoc" required="required">
				<button type="submit">파일 등록</button>
			</form>
		</div>
	</body>
</html>