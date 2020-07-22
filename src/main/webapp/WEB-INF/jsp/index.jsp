<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Yaml 생성</title>
		<style type="text/css">
			.yaml-content {
			 	margin-left: 10px;
			 	margin-bottom: 10px;
			}
		</style>
	</head>
	<body>
		<form action="/upload" method="post" enctype="multipart/form-data">
			<input type="file" id="apidoc" name="apidoc">
			<button type="submit">파일 업로드(.yaml, .yml)</button>
		</form>
		<form action="/" method="post">
			<div>
				<h4>API 개요</h4>
				<div class="yaml-content">
					<table>
						<tr>
							<td><label>version</label></td>
							<td><input name="info-version" type="text" placeholder="1.0.0"></td>
						</tr>
						<tr>
							<td><label>title</label></td>
							<td><input name="info-title" type="text" placeholder="Demo"></td>
						</tr>
					</table>
				</div>
			</div>		
			<div>
				<h4>연결 URL</h4>
				<div class="yaml-content">
					<table>
						<tr>
							<td><label>url</label></td>
							<td><input name="servers-url" type="text" placeholder="http://127.0.0.1:8080/"></td>
						</tr>
					</table>
				</div>
			</div>		
			<div>
				<h4>API 상세</h4>
				<div class="yaml-content">
					<table>
						<tr>
							<td><label>http method</label></td>
							<td>
								<select name="paths-method">
									<option value="get">get</option>
									<option value="post">post</option>
									<option value="put/patch">put/patch</option>
									<option value="delete">delete</option>
								</select>
							</td>
						</tr>
					</table>
				</div>
			</div>		
			<div>
				<h4>Model</h4>
				<div class="yaml-content">
					내용
				</div>
			</div>		
			<button type="submit">작성</button>
		</form>
	</body>
</html>