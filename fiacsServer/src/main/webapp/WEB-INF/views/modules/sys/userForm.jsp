<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/custom/spectrum.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/docs.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/toc.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/fileinput.js'></script>
<script type="text/javascript">
//图像加载出错时的处理
function errorImg(img) {
	img.src = "${ctxStatic}/images/user_img.jpg";
	img.onerror = null;
}
	$(document)
			.ready(
					function() {
						
						var userId=${user.id}+"";
						if(userId=="" || typeof (userId) == "undefined"){
							userId= "0" ;
						}
						var preview2 = '<img src="${ctx}/guard/image/user?id=' + userId
								+ '"  onerror="errorImg(this)" alt="点击选择证件照片" style="width:100px">';
						$("#avatar_file")
								.fileinput(
										{
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

											allowedFileExtensions : [ "jpg" ],
											previewSettings : {
												image : {
													width : "100px",
													height : "auto"
												}
											}
										});
						
						
							$("#up").change(function() {
						        var $file = $(this);
						        var fileObj = $file[0];
						        var windowURL = window.URL || window.webkitURL;
						        var dataURL;
						        var $img = $("#img_id");
						        if(fileObj && fileObj.files && fileObj.files[0]){
						        dataURL = windowURL.createObjectURL(fileObj.files[0]);
						        var subDataUrl= dataURL.substring(dataURL.length-3,dataURL.length);
						        if(subDataUrl!="jpg"){
						        	alert("只能选择JPG格式的图片");			        	
						        }
						        $img.attr('src',dataURL);
						        }else{
						        dataURL = $file.val();
						        var element = document.getElementById('img_id');
						        element.src = dataURL;
						        $("#photo").val(dataURL);
						        }
						    });						
				
						
						
						$("#no").focus();
						$("#inputForm")
								.validate(
										{
											rules : {
												loginName : {
													remote : "${ctx}/sys/user/checkLoginName?oldLoginName="
															+ encodeURIComponent('${user.loginName}')
												}
											},
											messages : {
												loginName : {
													remote : "用户名已存在"
												},
												confirmNewPassword : {
													equalTo : "输入与上面相同的密码"
												}
											},
											submitHandler : function(form) {
												loading('正在提交，请稍等...');
												form.submit();
											}
										});
					});


</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户<shiro:hasPermission
					name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="user" enctype="multipart/form-data"
		action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">头像:<br/><span class="help-inline">
			（请选择jpg格式图片）</span></label>
			<div class="col-xs-3">
				<!--[if lt IE 10]>
				<div class="picbtn"><img id="img_id" src= "${ctx}/guard/image/user?id=${user.id}"  onerror="errorImg(this)"  style="width:150px" /></div>
				<input type="file" name="file" id="up">
				<![endif]-->
				<!--[if gte IE 10]>
						<div class="kv-avatar" style="width: 100">
					<input id="avatar_file" name="file" type="file"
						class="file-loading">
					</div>
				<![endif]-->
				<!--[if !IE]><!-->
					<div class="kv-avatar" style="width: 100">
					<input id="avatar_file" name="file" type="file"
						class="file-loading">
				</div>
				<!--<![endif]-->
				<form:hidden id="photo" path="photo" htmlEscape="false"
					maxlength="255" class="input-xlarge" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">所属机构(所属机构区域):</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="office.id"
					value="${user.office.id}" labelName="office.name"
					labelValue="${user.office.name}" title="所属机构"
					url="/sys/office/treezeeData" cssClass="required"
					notAllowSelectRoot="true" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>姓名:</label>
			<div class="col-xs-3">
				<form:input path="name" htmlEscape="false" maxlength="50"
					class="required form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">工号:</label>
			<div class="col-xs-3">
				<form:input path="no" htmlEscape="false" maxlength="50"
					class="form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>登录账号:</label>
			<div class="col-xs-3">
				<input id="oldLoginName" name="oldLoginName" type="hidden"
					value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="50"
					class="required userName form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">
			<c:if test="${empty user.id}">
				<font class="red">*</font>
			</c:if>
			密码:</label>
			<div class="col-xs-3">
				<input id="newPassword" name="newPassword" type="password" value=""
					maxlength="50" minlength="3" class="${empty user.id?'required':''} form-control input-sm" />
				<c:if test="${not empty user.id}">
					<span class="help-inline">若不修改密码，请留空。</span>
				</c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">
			<c:if test="${empty user.id}">
				<font class="red">*</font>
			</c:if>
			确认密码:</label>
			<div class="col-xs-3">
				<input id="confirmNewPassword" name="confirmNewPassword" class="${empty user.id?'required':''} form-control input-sm"
					type="password" value="" maxlength="50" minlength="3"
					equalTo="#newPassword" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">邮箱:</label>
			<div class="col-xs-3">
				<form:input path="email" htmlEscape="false" maxlength="100"
					class="email form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">电话:</label>
			<div class="col-xs-3">
				<form:input path="phone" htmlEscape="false" maxlength="100"  class="form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">手机:</label>
			<div class="col-xs-3">
				<form:input path="mobile" htmlEscape="false" maxlength="100"  class="form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>是否允许登录:</label>
			<div class="col-xs-3">
				<form:select path="loginFlag" cssStyle="width :160px;"  class="form-control input-sm">
					<form:options items="${fns:getDictList('yes_no')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				</br>	
				<span class="help-inline">
					“是”代表此账号允许登录，“否”则表示此账号不允许登录</span>
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2">用户类型:</label>
			<div class="col-xs-3">
				<form:select path="userType" class="form-control input-sm">
					<form:option value="" label="请选择" />
					<form:options items="${fns:getDictList('sys_user_type')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>用户角色:</label>
			<div class="col-xs-8">
				<div class="checkbox-inline">
					<form:checkboxes path="roleIdList" items="${allRoles}"
						itemLabel="name" itemValue="id" htmlEscape="false" class="required" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注:</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3"
					maxlength="200"  class="form-control input-sm"/>
			</div>
		</div>
		<c:if test="${not empty user.id}">
			<div class="form-group">
				<label class="control-label col-xs-2">创建时间:</label>
				<div class="col-xs-3">
					<p class="lbl" style="margin-top: 5px"><fmt:formatDate
							value="${user.createDate}" type="both" dateStyle="full" /></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">最后登陆:</label>
				<div class="col-xs-5">
					<p class="lbl" style="margin-top: 5px">IP:
						${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate
							value="${user.loginDate}" type="both" dateStyle="full" />
					</p>
				</div>
			</div>
		</c:if>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="sys:user:edit">
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