<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>附件信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										if(!document.getElementById('fileEntityId').value){
											if(!document.getElementById('fileid').value){
											alert('附件不能为空');
											return;
											}
										}
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
		<li><a href="${ctx}/guard/file/">附件信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/file/form?id=${fileEntity.id}">附件信息<shiro:hasPermission
					name="guard:file:edit">${not empty fileEntity.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:file:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="fileEntity" enctype="multipart/form-data"
		action="${ctx}/guard/file/upload" method="post"
		class="form-horizontal">
		<form:hidden path="id" id="fileEntityId"/>
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>附件：</label>
			<div class="col-xs-3">
			<c:if test="${not empty fileEntity.id}">
			<form:input  path="fileName"
					class="form-control input-sm required alnum" id="fileName" 
					readOnly="true"/> 
					<input type="file" name="file" style="display:none;"/>
			</c:if>
			<c:if test="${empty fileEntity.id}">
				<input type="file" name="file" id="fileid"/>
			</c:if>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2">附件描述：</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="100" class="input-xxlarge" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:file:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="${not empty fileEntity.id?'保存':'上传'}" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</div>
</body>
</html>