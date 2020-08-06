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
				var openAPI = new Object();
				var requestBodyJson = new Object;
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
								openAPI = jsonData;
								console.log(openAPI);
								var httpMethods = ["get", "post", "put", "delete"]; 
								var newSpac = ``;

								var paths = openAPI.paths;
								var components = openAPI.components;
								Object.getOwnPropertyNames(paths).forEach(function(path, pathIdx){
									
									
									var operation = paths[path];
									var httpMethods = ["get", "post", "put", "delete"]; 
									httpMethods.forEach(function(httpMethod, hmIdx){
										var method = operation[httpMethod]
										if(method != null){
											console.log(path+"-"+httpMethod+":");
											newSpac+=`
										<div class="container p-1 my-1">
											<p>
												<button id="collapseBtn-\${pathIdx}-\${hmIdx}" class="btn btn-outline-primary" type="button" data-toggle="collapse" data-target="#collapse-\${pathIdx}-\${hmIdx}" aria-expanded="false" aria-controls="collapse">
													<span style="font-size:1.5em;font-weight:bold;">\${path}-\${httpMethod}</span> \${method.summary}
												</button>
											</p>
											<div class="collapse" id="collapse-\${pathIdx}-\${hmIdx}">
												<div class="card card-body">
													<div class="row">
											`;
											
											var params = method.parameters;
											var reqBody = method.requestBody;
											var responses = method.responses;
											
											console.log(method);
																						
											/////////// params /////////////
											if(params != null){
												newSpac+=`	
														<div class="container col-sm-6">
															<h5>Parameters</h5>`;
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
													if((schemaDetail[0] == "format" && schemaDetail[1] != null) ||
												 	   (schemaDetail[0] == "items" && schemaDetail[1] != null)){
											 			schemaType += "("+schemaDetail[1]+")";
									 			    }
													newSpac +=`
															<div class="form-group container">
																<label for="\${path}-\${httpMethod}-parameters-\${paramIdx}"> 
																	<p>\${param.required ? "<span style='color:red;'>*</span>":""} \${param.name} \${schemaType != null ? "["+schemaType+"]":""} - \${param.description}</p>
																	
																</label>
																\${param.required ? "<input name='"+path+"-"+httpMethod+"-parameters-"+paramIdx+"-required' value=true type='hidden' >":""}
																\${schemaDetail[0] == "format"? 
																`<input type="text" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" value="\${param.schema.default==null? "":param.schema.default}" \${param.required ? "required='required'":""}>`
																:
																	schemaDetail[0] == "items"?
																	`<input type="text" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" value="\${schemaDetail[1]}, \${schemaDetail[1]}, \${schemaDetail[1]}" \${param.required ? "required='required'":""}>`
																	:															
																    `<textarea rows="20" cols="50" id="\${path}-\${httpMethod}-parameters-\${paramIdx}" class="form-control" name="\${path}-\${httpMethod}-parameters-\${paramIdx}" \${param.required ? "required='required'":""}>\${JSON.stringify(schemaDetail[1])}</textarea>`
																}
															</div>
													`;
												});
												newSpac+=`	
														</div>`;
											}
											/////////// params end /////////////
											
											/////////// reqBody /////////////
											var contentBodies = null;
											if(reqBody != null){
												console.log("/////////// reqBody /////////////");
												newSpac+=`	
													<div class="container col-sm-6">
														<h5>Request Body</h5>`;
												var required = reqBody.required;
												var desc = reqBody.description;
												var content = reqBody.content;
												contentBodies = new Object();
												if(content != null){
													Object.getOwnPropertyNames(content).forEach(function(mediaType){
														var contentBody = new Object();
														var media = content[mediaType];
														var type = media.schema.type;
														var body = new Object();
														contentBody["type"] = type;
														console.log("mediaType: "+mediaType);
	
														var properties = null;
														if(type == "array"){
															console.log("array!!!!!");
															properties = media.schema.items.properties;
														} else {
															console.log("object!!!!! ");
															properties = media.schema.properties;
														}
														Object.getOwnPropertyNames(properties).forEach(function(propKey){
															var property = properties[propKey];
															console.log(propKey+": "+property.type);
															body[propKey] = property.type;
														});
														contentBody["body"] = body;
														contentBodies[mediaType] = contentBody;
													});
												}
												
												newSpac+=`
														\${desc != null ? "<p>"+desc+"</p>":""}
														\${required ? "<span style='color:red;'>*</span>":""}
														<select id="\${path}-\${httpMethod}-requestBody" name="\${path}-\${httpMethod}-requestBody">
															<option value="none" selected>=====select=====</option>`;
												
												Object.getOwnPropertyNames(contentBodies).forEach(function(mediaType){
													newSpac+=`
															<option value="\${mediaType}">\${mediaType}</option>
														`;
												});
												newSpac+=`
														</select>
														\${required ? "<input name='"+path+"-"+httpMethod+"-requestBody-content-required' value=true type='hidden' >":""}
														<textarea rows="10" cols="10" id="\${path}-\${httpMethod}-requestBody-content" class="form-control" name="\${path}-\${httpMethod}-requestBody-content" \${required ? "required='required'":""} ></textarea>
													</div>`;
												console.log("/////////// reqBody end /////////////");
											}
											requestBodyJson[path+"-"+httpMethod] = contentBodies;
											/////////// reqBody end /////////////
											
											///////////   responses   /////////////
											newSpac+=`	<div class="container col-sm-12">
															<div class="form-group container"> 
																<span style="font-size:1.3em;">Test Status Code</span> `;
											Object.getOwnPropertyNames(responses).forEach(function(statusCode, idx){
												if((statusCode >= 100 && statusCode <= 599) || statusCode == "default"){
													newSpac+=`		<input type="radio" id="\${path}-\${httpMethod}-test" name="\${path}-\${httpMethod}-test" value="\${statusCode}" \${idx==0 ? "checked":""}> \${statusCode} `;	
												}											
											});
											newSpac+=`		</div>
														</div>
													</div>
												</div>
											</div>
										</div>
											`;
											/////////// responses end /////////////
										}
									});
											
								});
								newSpac+=`
									<button type="button" class="btn btn-success" id="testBtn">입력</button>
								`;
								$("#specForm").html(newSpac);
							}
						});
					}
				});
				$(document).on("change", "select", function(event){
					var name = event.target.name.split("-");
					var pathMethod = name[0]+"-"+name[1];
					var index = event.target.selectedIndex;
					var mediaType = event.target[index].innerText;
					var contentName = event.target.name+"-content";
					if(mediaType != "=====select====="){
						var body = requestBodyJson[pathMethod][mediaType].body;
						var type = requestBodyJson[pathMethod][mediaType].type;
						if(type == "array"){
							$("textarea[name='"+contentName+"']").text("["+JSON.stringify(body, null, '\t')+"]");
						} else{
							$("textarea[name='"+contentName+"']").text(JSON.stringify(body, null, '\t'));
						}
					} else{
						$("textarea[name='"+contentName+"']").text("");
					}
					/* \${JSON.stringify(body, null, '\t')} */
				});
				$(document).on("click", "#testBtn", function(){
					var form = $("#specForm");
					var inputs = form.serializeArray();
					var validate = true;
					var testForm = new Object();
					var reqBodyForm = new Object();
					var paramForm = new Object();
					console.log(inputs);
					inputs.some(function(input){
						var name = input.name;
						if(input.value == "" && inputs[name+"-required"] != null){
							console.log("empty parameter find!!")
							validate = false;
							console.log("name: "+name);
							var inputTag = $("#"+$.escapeSelector(name));
							var parent = inputTag.closest(".collapse");
							console.log("parent: ");
							console.log(parent);
							parent.addClass("show");
							inputTag.focus();	
							return true;
						}
					});
					if(validate){
						console.log(inputs);
						inputs.forEach(function(input){
							
							var defaultPath = input.name.split("-");
							if(!defaultPath.includes("required")){
								if(defaultPath.includes("requestBody")){
									reqBodyForm[input.name] = input.value;
								}
								else if(defaultPath.includes("test")){ // debug 모드(responses 부분 추가 예정)
									testForm[input.name] = input.value;
								} 
								else if(defaultPath.includes("parameters")){
									//paramForm[input.name] = input.value;
									var defaultLayer = openAPI.paths;
									console.log("json start!!!");
									defaultPath.forEach(function(path){
										console.log(path+"!!");
										defaultLayer = defaultLayer[path];
									});
									console.log(defaultLayer);
									defaultLayer.schema.default = input.value;
								}
							}
						});
						var formJson = {"openAPI": openAPI, "testForm": testForm, "reqBodyForm": reqBodyForm};
						console.log("----------------------start last ajax----------------------");
						console.log(formJson);
						$.ajax({
							url: "/apitest",
							type: "post",
							data: JSON.stringify(formJson),
							dataType: "json",
							contentType:"application/json; charset=uft-8",
							error: function(){
								alert("error!!");
							},
							success: function(data){
								alert("success!!");
							}
						});
					} else {
						alert("parameter 값을 입력해 주세요.");
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