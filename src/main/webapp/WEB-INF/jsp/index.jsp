<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Yaml 생성</title>
		<script type="text/javascript">
			function createYaml(){
				alert("success");
				let data = {
				    title: 'Reading and Writing YAML to a File in Node.js/JavaScript',
				    'url path': '/reading-and-writing-yaml-to-a-file-in-node-js-javascript',
				    domain: 'stackabuse.com',
				    port: 443,
				    'is-https': true,
				    meta: {
				        'published-at': 'Nov. 1st, 2019',
				        author: {
				            name: 'Scott Robinson',
				            contact: 'scott@stackabuse.com'
				        },
				        tags: [
				            'javascript', 'node.js', 'web development'
				        ]
				    }
				};
				const fs = require('fs');
				const yaml = require('js-yaml');

				let yamlStr = yaml.safeDump(data);
				fs.writeFileSync('data-out.yaml', yamlStr, 'utf8');
			}
		</script>
	</head>
	<body>
		<h1> create Yaml!! </h1>
		<form action="/">
			<label>row text:</label>
			<br/>
			<textarea rows="50" cols="100"></textarea>
			<br/>
			<button type="button" onclick="createYaml()">작성</button>
		</form>
	</body>
</html>