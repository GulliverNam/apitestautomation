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
				var json = new Object();
				
				$(document).on("click", "#fileUploadBtn", function(){
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
								$("#fileName").html(`<h2>\${$("#apidoc").val()}</h2>`);
								$("#apidoc").val("");
								json = jsonData;
								console.log(json);
								var httpMethods = ["get", "post", "put", "delete"]; 
								var newSpac = ``;

								var paths = json.paths;
								var components = json.components;
								Object.getOwnPropertyNames(paths).forEach(function(path, pathIdx){
									console.log(path+":");
									newSpac+=`
										<div class="container p-1 my-1">
											<p>
												<button id="collapseBtn-\${pathIdx}" class="btn btn-outline-primary" type="button" data-toggle="collapse" data-target="#collapse-\${pathIdx}" aria-expanded="false" aria-controls="collapse">
													\${path}</td>
												</button>
											</p>
											<div class="collapse" id="collapse-\${pathIdx}">
												<div class="card card-body">
									`;
									
									var operation = paths[path];
									var httpMethods = ["get", "post", "put", "delete"]; 
									httpMethods.forEach(function(httpMethod){
										var method = operation[httpMethod]
										if(method != null){
											newSpac += `
													<h4>\${httpMethod}</h4>
													<p>\${method.summary}</p>
													<div class="container">
											`;
											
											var params = method.parameters;
											var reqBody = method.reqestBody;
											console.log(method);
											if(params != null){
												newSpac+=`	<h5>params</h5>`;
												params.forEach(function(param, paramIdx){
													var schemaType = param.schema.type;
													var dataType = ["string", "integer", "number"];
													var schemaDetail = null;
													if(dataType.includes(schemaType)){
														schemaDetail = ["format", param.schema.format];
													} else if(schemaType == "array"){
														schemaDetail = ["items", param.schema.items.type];
													} else if(schemaType == "object"){
														schemaDetail = ["properties", param.schema.properties];
													}
													
													newSpac +=`
														<div class="form-group container">
															<label for="\${path}-\${httpMethod}-parameters-\${paramIdx}"> 
																<p>\${param.name} - [\${param.description}] (required: \${param.required})</p>
																<p>type : \${schemaType}\${(schemaDetail[0] == "format" && schemaDetail[1] != null) ||
																			 			   (schemaDetail[0] == "items" && schemaDetail[1] != null) ? "("+schemaDetail[1]+")":""}
																</p>
															</label>
															\${schemaDetail[0] == "format"? 
															   `<input type="text" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" value="\${param.schema.default==null? "":param.schema.default}" required="required">`
															   :
																   schemaDetail[0] == "items"?
																   `<input type="text" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" value="[\${schemaDetail[1]}, \${schemaDetail[1]}, \${schemaDetail[1]}]" required="required">`
																   :															
															   	   `<textarea rows="20" cols="50" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" required="required">\${JSON.stringify(schemaDetail[1])}</textarea>`
															}
															
														</div>
													`;
												});
											}
											newSpac+=`		</div>`;
										}
									});
									newSpac+=`
								  				</div>
											</div>
										</div>
									`;
								});
								newSpac+=`
									<button type="button" class="btn btn-success" id="testBtn">입력</button>
								`;
								$("#specForm").html(newSpac);
							}
						});
					}
				});

				$(document).on("click", "#testBtn", function(){
					var form = $("#specForm");
					var inputs = form.serializeArray();
					var validate = true;
					console.log(inputs);
					inputs.some(function(input){
						var name = input.name;
						if(input.value == ""){
							validate = false;
							console.log("name: "+name);
							var inputTag = $("#"+$.escapeSelector(name));
							var parent = inputTag.parent().parent().parent().parent();
							console.log("parent: ");
							console.log(parent);
							parent.addClass("show");
							inputTag.focus();	
							return true;
						}
					});
					if(validate){
						inputs.forEach(function(input){
							var defaultPath = input.name.split("-");
							var defaultLayer = json.paths;
							
							console.log("json start!!!");
							defaultPath.forEach(function(path){
								console.log(path+"!!");
								defaultLayer = defaultLayer[path];
							});
							defaultLayer.schema.default = input.value;
						});
						$.ajax({
							url: "/apitest",
							type: "post",
							data: JSON.stringify(json),
							dataType: "json",
							contentType:"application/json; charset=uft-8",
							success: function(data){
								alert("test success!!");
							}
						});
					}
				});
			});
		</script>
	</head>
	<body>
		
		<div class="container p-3 my-3" align="center">
			<h3>명세서 파일을 등록해주세요.(.yaml, .yml, .json)</h3>
			<form id="fileForm" action="" method="post" enctype="multipart/form-data">
				<input type="file" id="apidoc" name="apidoc" accept=".yaml, .yml, .json">
				<button class="btn btn-primary" id="fileUploadBtn" type="button">파일 등록</button>
			</form>
		</div>
		<div id="apiSpec" class="container p-3 my-3">
			<div id="fileName">
			</div>
			<form id="specForm" method="post" action="">
			</form>
		</div>
	</body>
</html>