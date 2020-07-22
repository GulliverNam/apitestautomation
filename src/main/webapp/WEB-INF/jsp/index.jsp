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
					<div class="yaml-content">
						<label>parameters</label>
						<table>
							<tr>
								<td><label>name</label></td>
								<td><input name="paths-params-name" type="text" placeholder="param"></td>
							</tr>
							<tr>
								<td><label>in</label></td>
								<td>
									<select name="paths-params-in">
										<option value="query">query</option>
										<option value="header">header</option>
										<option value="path">path</option>
										<option value="cookie">cookie</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label>required</label></td>
								<td>
									<input type="radio" name="paths-params-required" value="Y" checked="checked">Y
									<input type="radio" name="paths-params-required" value="N" checked="checked">N
								</td>
							</tr>
							<tr>
								<td><label>default</label></td>
								<td><input name="paths-params-default" type="text" placeholder="default value"></td>
							</tr>
						</table>
					</div>
					<div class="yaml-content">
						<label>request body</label>
						<table>
							<tr>
								<td><label>required</label></td>
								<td>
									<input type="radio" name="paths-reqbody-required" value="Y" checked="checked">Y
									<input type="radio" name="paths-reqbody-required" value="N" checked="checked">N
								</td>
							</tr>
							<tr>
								<td><label>content</label></td>
								<td><input name="paths-reqbody-content" type="text" placeholder="content"></td>
							</tr>
						</table>
					</div>
					<div class="yaml-content">
						<label>response</label>
						<table>
							<tr>
								<td><label>default</label></td>
								<td><input name="paths-res-default" type="text" placeholder="default value"></td>
							</tr>
							<tr>
								<td><label>status code</label></td>
								<td><input name="paths-res-statuscode" type="text" placeholder="status code"></td>
							</tr>
						</table>
					</div>
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