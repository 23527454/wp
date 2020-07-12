<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
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
						

						$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
					});
</script>

</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/info">个人信息</a></li>
		<li><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="user"
		enctype="multipart/form-data" action="${ctx}/sys/user/info"
		method="post" class="form-horizontal">
		<%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>

		<sys:message content="${message}" />
		<div class="form-group">
			<label for="photo" class="col-xs-2 control-label">头像:<br/>	<span class="help-inline">
			（请选择jpg格式图片）</span></label>
			<div class="col-xs-10">
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
			<label for="company" class="col-xs-2 control-label">所属区域:</label>
			<div class="col-xs-4">
				<p class="form-control-static">${user.office.area.name}</p>
			</div>
		</div>
		<div class="form-group">
			<label for="office" class="col-xs-2 control-label">所属机构:</label>
			<div class="col-xs-4">
				<p class="form-control-static">${user.office.name}</p>
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-xs-2 control-label">姓名:</label>
			<div class="col-xs-4">
				<form:input path="name" htmlEscape="false" maxlength="50"
					class="required form-control" readonly="true" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-2 control-label">邮箱:</label>
			<div class="col-xs-4">
				<form:input path="email" htmlEscape="false" maxlength="50"
					class="email form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-2 control-label">电话:</label>
			<div class="col-xs-4">
				<form:input path="phone" htmlEscape="false" maxlength="50"
					class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label for="mobile" class="col-xs-2 control-label">手机:</label>
			<div class="col-xs-4">
				<form:input path="mobile" htmlEscape="false" maxlength="50"
					class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label for="remarks" class="col-xs-2 control-label">备注:</label>
			<div class="col-xs-4">
				<form:textarea path="remarks" htmlEscape="false" rows="3"
					maxlength="200" class="form-control input-xlarge" />
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="col-xs-2 control-label">用户类型:</label>
			<div class="col-xs-4">
				<p class="form-control-static">${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-2 control-label">用户角色:</label>
			<div class="col-xs-4">
				<p class="form-control-static">${user.roleNames}</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-xs-2 control-label">上次登录:</label>
			<div class="col-xs-10">
				<p class="form-control-static">
					IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：
					<fmt:formatDate value="${user.oldLoginDate}" type="both"
						dateStyle="full" />
				</p>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />
			</div>
		</div>
	</form:form>
</body>
</html>