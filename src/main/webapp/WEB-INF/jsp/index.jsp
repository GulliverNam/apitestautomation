<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Yaml 생성</title>

<style type="text/css">
.yaml-form {
	margin-left: 10px;
	margin-bottom: 10px;
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
	var patternNum = -1;
	var detailNums = [];
	function addFieldPattern(){
		++patternNum;
		var div = document.querySelector("#field-patterns");
		div.innerHTML += `
			<div class="yaml-form">
				<h3>field pattern</h3>
				<input id='field-pattern-\${patternNum}' placeholder='/path'/>
				<a href='#' onclick='addApiDetail(\${patternNum})'>추가</a>
				<div id="api-detail-\${patternNum}">
				</div>
			</div>
		`;
		detailNums[patternNum] = -1;
	}
	function addApiDetail(pnum){
		++detailNums[pnum];
		var div = document.querySelector(`#api-detail-\${pnum}`);
		var detailNum = detailNums[pnum];
		var item = `
		<table>
			<tr>
				<td><label>http method</label></td>
				<td><select name='paths-method-\${pnum}-\${detailNum}'>
						<option value='get'>get</option>
						<option value='post'>post</option>
						<option value='put'>put</option>
						<option value='delete'>delete</option>
				</select></td>
			</tr>
		</table>
		<div class='yaml-form'>
			<h4>parameters</h4>
			<table>
				<tr>
					<td><label>name</label></td>
					<td><input name='paths-params-name-\${pnum}-\${detailNum}' type='text'
						placeholder='param'></td>
				</tr>
				<tr>
					<td><label>in</label></td>
					<td><select name='paths-params-in-\${pnum}-\${detailNum}'>
							<option value='query'>query</option>
							<option value='header'>header</option>
							<option value='path'>path</option>
							<option value='cookie'>cookie</option>
					</select></td>
				</tr>
				<tr>
					<td><label>required</label></td>
					<td><input type='radio' name='paths-params-required-\${pnum}-\${detailNum}'
						value='Y' checked='checked'>Y <input type='radio'
						name='paths-params-required-\${pnum}-\${detailNum}' value='N'>N
					</td>
				</tr>
				<tr>
					<td><label>default</label></td>
					<td><input name='paths-params-default-\${pnum}-\${detailNum}' type='text'
						placeholder='default value'></td>
				</tr>
			</table>
		</div>
		<div class='yaml-form'>
			<h4>request body</h4>
			<table>
				<tr>
					<td><label>required</label></td>
					<td><input type='radio' name='paths-reqbody-required-\${pnum}-\${detailNum}'
						value='Y' checked='checked'>Y <input type='radio'
						name='paths-reqbody-required-\${pnum}-\${detailNum}' value='N'>N
					</td>
				</tr>
				<tr>
					<td><h4>content</h4></td>
					<td>
						<div class = 'yaml-form'>
							<table>
								<tr>
									<td><label>content type</label></td>
									<td>
										<select name='paths-reqbody-contenttype-\${pnum}-\${detailNum}'>
											<option value='stream'>application/octet-stream</option>
											<option value='json'>application/json</option>
											<option value='plain'>text/plain</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>schema-type</td>
									<td>
										<select id='paths-reqbody-schematype-\${pnum}-\${detailNum}' name='paths-reqbody-schematype-\${pnum}-\${detailNum}'  onchange='schemaChange(\${pnum}, \${detailNum})'>
											<option value='object'>object</option>
											<option value='array'>array</option>
											<option value='string'>string</option>
											<option value='integer'>integer</option>
											<option value='number'>number</option>
											<option value='boolean'>boolean</option>
										</select>
									</td>
								</tr>
								<tr id='paths-reqbody-schemadetail-\${pnum}-\${detailNum}'>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class='yaml-form'>
			<h4>response</h4>
			<table>
				<tr>
					<td><label>default</label></td>
					<td><input name='paths-res-default-\${pnum}-\${detailNum}' type='text'
						placeholder='default value'></td>
				</tr>
				<tr>
					<td><label>status code</label></td>
					<td><input name='paths-res-statuscode-\${pnum}-\${detailNum}' type='text'
						placeholder='status code'></td>
				</tr>
			</table>
		</div>
		<hr>`;
		div.innerHTML += item;
	}
	function schemaChange(pnum, dnum){
		var schemaDetail = document.querySelector(`#paths-reqbody-schemadetail-\${pnum}-\${dnum}`);
		var schemaType = document.querySelector(`#paths-reqbody-schematype-\${pnum}-\${dnum}`);
		var selected = schemaType.options[schemaType.selectedIndex].value;
		if(selected == "array"){
			schemaDetail.innerHTML = `<td><label>items</label></td>
									  </td>
									  	<input id='#paths-reqbody-schemadetail-items-\${pnum}-\${dnum}'>
									  </td>`;
		} else if(selected == "object"){
			schemaDetail.innerHTML = `<td><label>properties</label></td>
									  <td>
									  	<input id='#paths-reqbody-schemadetail-properties-\${pnum}-\${dnum}'>
									  </td>`;
		} else if(selected == "string"){
			schemaDetail.innerHTML = `<td>
										<label>format</label>
									  </td>
									  <td>
									  	<select id='paths-reqbody-schemadetail-format-\${pnum}-\${dnum}'>
											<option value='default'>default</option>
											<option value='byte'>byte</option>
											<option value='binary'>binary</option>
											<option value='date'>date</option>
											<option value='data-time'>data-time</option>
											<option value='password'>password</option>
										</select>
									  </td>`;
		} else if(selected == "number"){
			schemaDetail.innerHTML = `<td>
										<label>format</label>
									  </td>
									  <td>
									  	<select id='paths-reqbody-schemadetail-format-\${pnum}-\${dnum}'>
											<option value='float'>float</option>
											<option value='double'>double</option>
										</select>
									  </td>`;
		} else if(selected == "integer"){
			schemaDetail.innerHTML = `<td>
										<label>format</label>
									  </td>
									  <td>
									  	<select id='paths-reqbody-schemadetail-format-\${pnum}-\${dnum}'>
											<option value='int32'>int32</option>
											<option value='int64'>int64</option>
										</select>
									  </td>`;
		} else {
			schemaDetail.innerHTML = ``;
		}
	}
	/* function yamlSubmit(){
		var result = 
`openapi: 3.0.0
info:
  version: \${document.querySelector('#info-version').value}
  title: \${document.querySelector('#info-title').value}
servers:
  - url: \${document.querySelector('#servers-url').value}
paths:`;
		detailNums.forEach((item, index) => {
			result += `
  \${document.querySelector(`#field-pattern-\${index}`).value}:`
			for(var i=0; i<=item; i++){
				console.log("dnum: "+i)
			}
		});
		
		console.log(result);
		alert(result);
	} */
</script>
</head>
<body>
	<form action="/upload" method="post" enctype="multipart/form-data">
		<input type="file" id="apidoc" name="apidoc">
		<button type="submit">파일 업로드(.yaml, .yml)</button>
	</form>
	<form action="/" method="post">
		<h3>row text</h3>
		<textarea name="rowtext" rows="50" cols="100"></textarea>
		<br>
		<button type="submit">yaml test</button>
	</form>
	<form id="yaml-form" action="#" method="post">
		<div>
			<h2>API 개요</h2>
			<div class="yaml-form">
				<table>
					<tr>
						<td><label>version</label></td>
						<td><input id="info-version" type="text"
							placeholder="1.0.0"></td>
					</tr>
					<tr>
						<td><label>title</label></td>
						<td><input id="info-title" type="text" placeholder="Demo"></td>
					</tr>
				</table>
			</div>
		</div>
		<div>
			<h2>연결 URL</h2>
			<div class="yaml-form">
				<table>
					<tr>
						<td><label>url</label></td>
						<td><input id="servers-url" type="text"
							placeholder="http://127.0.0.1:8080/"></td>
					</tr>
				</table>
			</div>
		</div>
		<div>
			<h2>API 상세</h2>
			<a href="#" onclick="addFieldPattern()">pattern 추가</a>
			<div id="field-patterns" class="yaml-form"></div>
		</div>
		<button id="yaml-btn" type="button" >작성</button>
	</form>
</body>
</html>