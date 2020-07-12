<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>第三方公司管理</title>
<meta name="decorator" content="default" />
<script type='text/javascript' src='${ctxStatic}/custom/fileinput.js'></script>
<script type="text/javascript" src="${ctxStatic}/custom/spectrum.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/docs.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/toc.js'></script>
<script type="text/javascript">
		$(document).ready(function() {
			
			if (document.all['radio_hidden'].value == '0') {
				document.all['radio'][0].checked = true;
			} else if (document.all['radio_hidden'].value == '1') {
				document.all['radio'][1].checked = true;
			}  else {
				document.all['radio'][0].checked = true;
				$("#radio_hidden").val("0");
			}
			
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
		});
		
		　//图像加载出错时的处理
		function errorImg(img) {
			img.src = "${ctxStatic}/images/company_img.jpg";
			img.onerror = null;
		}
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			
			var rowId;
			if(row=="" || typeof (row) == "undefined"){
				rowId= "0" ;
			}else{
				rowId=row.id+"";
			}
			if(rowId=="" || typeof (rowId) == "undefined"){
					rowId= "0" ;
			}
			var preview2 = '<img src="${ctx}/guard/image/company?id=' + rowId
				+ '"  onerror="errorImg(this)" alt="点击选择证件照片" style="width:100px">';
				$("#avatar"+idx+"_file").fileinput({
					overwriteInitial : false,
					maxFileSize : 1500,
					showCaption : false,
					showBrowse : false,
					autoReplace : true,
					browseOnZoneClick : true,
					removeLabel : '',
					showRemove : false,
					removeTitle : '取消或重新修改',
					elErrorContainer : '#kv-avatar-errors-2',
					msgErrorClass : 'alert alert-block alert-danger',
					defaultPreviewContent : preview2,
					removeFromPreviewOnError : true,
					layoutTemplates : {
						main2 : '{preview} {remove} {browse}'
					},
					allowedFileExtensions : [ "jpg","bmp","gif","png", "jpeg", "ico"],
					previewSettings : {
						image : {
							width : "100px",
							height : "auto"
						}
					}
				});
				
				//头像上传预览
			    $("#companyExList"+idx+"_up").change(function() {
			        var $file = $(this);
			        var fileObj = $file[0];
			        var windowURL = window.URL || window.webkitURL;
			        var dataURL;
			        var $img = $("#companyExList"+idx+"_img_id");
			        if(fileObj && fileObj.files && fileObj.files[0]){
			        dataURL = windowURL.createObjectURL(fileObj.files[0]);
			        var subDataUrl= dataURL.substring(dataURL.length-3,dataURL.length);
			        if(subDataUrl!="jpg"){
			        	alert("只能选择JPG格式的图片");			        	
			        }
			        $img.attr('src',dataURL);
			        }else{
			        dataURL = $file.val();
			        var element = document.getElementById("companyExList"+idx+"_img_id");
			        element.src = dataURL;
			        $("#companyExList"+idx+"_imagePath").val(dataURL);
			        }
			    });
				
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
		
		function getRadio() {
			e = event.srcElement;
			if (e.type == "radio" && e.name == "radio") {
				if (e.value == 0) {
					$("#radio_hidden").val(0);
				} else if (e.value == 1) {
					$("#radio_hidden").val(1);
				}
			}
		}
		
	</script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/custom/spectrum.css">
</head>
<body>
	<div class="container-fluid">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/guard/company/">第三方公司列表</a></li>
			<li class="active"><a
				href="${ctx}/guard/company/form?id=${company.id}">第三方公司<shiro:hasPermission
						name="guard:company:edit">${not empty company.id?'修改':'添加'}</shiro:hasPermission>
					<shiro:lacksPermission name="guard:company:edit">查看</shiro:lacksPermission></a></li>
		</ul>
		<br />
		<form:form id="inputForm" modelAttribute="company"
			action="${ctx}/guard/company/save" method="post"
			enctype="multipart/form-data" class="form-horizontal">
			<form:hidden path="id" />
			<sys:message content="${message}" />
			<div class="form-group">
				<label class="control-label col-xs-2">上级公司:</label>
				<div class="col-xs-3">
					<sys:treeselect id="company" name="parent.id"
						value="${company.parent.id}" labelName="parent.name"
						labelValue="${company.parent.shortName}" title="公司"
						url="/guard/company/treeData" extId="${company.id}" cssClass=""
						allowClear="true" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"><font class="red">*</font>区域：</label>
				<div class="col-xs-3">
					<sys:treeselect id="area" name="area.id" value="${company.area.id}"
						labelName="area.name" labelValue="${company.area.name}" title="区域"
						url="/sys/area/treeData" cssClass="required" allowClear="true"
						notAllowSelectRoot="true" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"><font class="red">*</font>简称：</label>
				<div class="col-xs-3">
					<form:input path="shortName" htmlEscape="false" maxlength="32"
						class="form-control input-sm required" />
				</div>
				<form:errors path="shortName" cssClass="error"></form:errors>
			</div>

			<div class="form-group">
				<label class="control-label col-xs-2">公司编码：</label>
				<div class="col-xs-3">
					<form:input path="companyCode" htmlEscape="false"
						class="form-control input-sm" id="company_code" />
				</div>
				<form:errors path="companyCode" cssClass="error"></form:errors>
			</div>

			<form:input path="companyType" id="radio_hidden" type="hidden" />
			<div class="form-group">
				<label class="control-label col-xs-2"><font class="red">*</font>公司类型：</label>
				<div class="col-xs-3">
					<div class="radio-inline">
						<span onclick="getRadio();"> <input type="radio"
							name="radio" value="0"><label for="taskType">押运公司</label>&nbsp;&nbsp;<input
							type="radio" name="radio" value="1"> <label
							for="taskTypse">维保公司</label>&nbsp; 
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">全称：</label>
				<div class="col-xs-3">
					<form:input path="fullName" htmlEscape="false" maxlength="128"
						class="form-control input-sm" />
				</div>
				<form:errors path="fullName" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">公司地址：</label>
				<div class="col-xs-3">
					<form:input path="address" htmlEscape="false" maxlength="128"
						class="form-control input-sm" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">联系人：</label>
				<div class="col-xs-3">
					<form:input path="contact" htmlEscape="false" maxlength="32"
						class="form-control input-sm" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">联系方式：</label>
				<div class="col-xs-3">
					<form:input path="phone" htmlEscape="false" maxlength="18"
						class="form-control input-sm" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">传真：</label>
				<div class="col-xs-3">
					<form:input path="fax" htmlEscape="false" maxlength="32"
						class="form-control input-sm " />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">电子邮箱：</label>
				<div class="col-xs-3">
					<form:input path="email" htmlEscape="false" maxlength="32"
						class="form-control input-sm " />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">公司附件资料：
				</label>
				<div class="col-xs-5">
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>照片类型</th>
								<th style="width:150px">照片路径</th>
								<shiro:hasPermission name="guard:company:edit">
									<th  style="width:37px;">&nbsp;</th>
								</shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="companyExList">
						</tbody>
						<shiro:hasPermission name="guard:company:edit">
							<tfoot>
								<tr>
									<td class="hide"></td>
									<td colspan="3"><a href="javascript:"
										onclick="addRow('#companyExList', companyExRowIdx, companyExTpl);companyExRowIdx = companyExRowIdx + 1;"
										class="btn">新增</a></td>
								</tr>
							</tfoot>
						</shiro:hasPermission>
					</table>
					<script type="text/template" id="companyExTpl">//<!--
						<tr id="companyExList{{idx}}">
							<td class="hide">
								<input id="companyExList{{idx}}_id" name="companyExList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="companyExList{{idx}}_delFlag" name="companyExList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="companyExList{{idx}}_imageType" name="companyExList[{{idx}}].imageType" data-value="{{row.imageType}}" class="form-control input-sm required">
									<c:forEach items="${fns:getDictList('comm_image_type')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
							<input id="companyExList{{idx}}_imagePath" name="companyExList[{{idx}}].imagePath" value="{{row.imagePath}}" type="hidden" maxlength="255" />
							<!--[if lt IE 10]>
							<div class="picbtn"><img id="companyExList{{idx}}_img_id" src= "${ctx}/guard/image/company?id={{row.id}}" onerror="errorImg(this)"  style="width:100px" /></div>
							<input type="file" name="companyExList[{{idx}}].file" id="companyExList{{idx}}_up">
							<![endif]-->
							<!--[if gte IE 10]>
								<div class="kv-avatar" style="width: 100px">
									<input id="avatar{{idx}}_file" name="companyExList[{{idx}}].file" type="file" class="file-loading">
								</div>
							<![endif]-->
							<!--[if !IE]><!-->
								<div class="kv-avatar" style="width: 100px">
									<input id="avatar{{idx}}_file" name="companyExList[{{idx}}].file" type="file" class="file-loading">
								</div>
							<!--<![endif]-->
								<p class="help-block">点击上面图片上传新图片。</p>
							</td>
							<shiro:hasPermission name="guard:company:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="btn close" onclick="delRow(this, '#companyExList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var companyExRowIdx = 0, companyExTpl = $("#companyExTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(company.companyExList)};
							for (var i=0; i<data.length; i++){
								addRow('#companyExList', companyExRowIdx, companyExTpl, data[i]);
								companyExRowIdx = companyExRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-offset-2 col-xs-10">
					<shiro:hasPermission name="guard:company:edit">
						<input id="btnSubmit" class="btn btn-primary" type="submit"
							value="保 存" />&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn" type="button" value="返 回"
						onclick="history.go(-1)" />
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>