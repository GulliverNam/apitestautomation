<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>API Test</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#fileUploadBtn").click(function(){
					var form = $("#fileForm")[0];
					var formData = new FormData(form);
					var apidoc = $("#apidoc")[0].files[0];
					if(apidoc == null){
						alert("파일을 등록해 주세요.");
					} else {
						formData.append("apidoc", apidoc);
						$.ajax({
							url: '/upload',
							processData: false,
							contentType: false,
							data: formData,
							type: 'POST',
							success: function(jsonData){
								console.log(jsonData)
								var newSpac = ``;
								jsonData.forEach(function(path, pathIdx){
									var pathName = path["path"];
									var methods = path["methods"];
									
									newSpac+=`
											<div class="container p-1 my-1">
												<p>
													<button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#collapseExample-\${pathIdx}" aria-expanded="false" aria-controls="collapseExample">
														\${pathName}</td>
													</button>
												</p>
												<div class="collapse" id="collapseExample-\${pathIdx}">
													<div class="card card-body">
									`;
											
									methods.forEach(function(method, methodIdx){
										var httpMethod = method["httpMethod"];
										var params = method["parameters"];
										console.log(method);
										newSpac+=`
														<p><strong>\${httpMethod}</strong>  \${method["summary"]}</p>
														<div class="container">
										`;
										params.forEach(function(param, paramIdx){
											if(paramIdx == 0)
												newSpac+=`	<h5>params</h5>`;
											var paramName = param["name"];
											newSpac +=`
															<div class="form-group container">
																<label for="\${pathName}-\${httpMethod}-\${paramName}"> 
																	<p>\${paramName} - [\${param["desc"]}] (required: \${param["required"]})</p>
																	<p>type : \${param["schema"]["type"]}</p>
																</label>
																<input type="text" id="\${pathName}-\${httpMethod}-\${paramName}" class="form-control" name="\${pathName}-\${httpMethod}-\${paramName}" value="\${param["schema"]["defaultVal"]}" required="required">
															</div>
											`;
										});
										newSpac+=`		</div>`;
									});
														
									newSpac+=`
												  	</div>
												</div>
											</div>
									`;
								});
								newSpac+=`
									<button type="submit" class="btn btn-primary">입력</button>
								`;
								$("#specForm").html(newSpac);
							}
						})
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="container p-3 my-3" align="center">
			<h3>명세서 파일을 등록해주세요.(.yaml, .yml, .json)</h3>
			<form id="fileForm" action="" method="post" enctype="multipart/form-data">
				<input type="file" id="apidoc" name="apidoc">
				<button class="btn btn-primary" id="fileUploadBtn" type="button">파일 등록</button>
			</form>
		</div>
		<div id="apiSpec" class="container p-3 my-3">
			<form id="specForm" action="">
			</form>
		</div>
	</body>
</html>