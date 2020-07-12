<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>区域管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
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
	<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/area/">区域列表</a></li>
		<li class="active"><a
			href="form?id=${area.id}&parent.id=${area.parent.id}">区域<shiro:hasPermission
					name="sys:area:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="sys:area:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="area"
		action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
			<sys:message content="${message}" />
			<div class="form-group">
				<label class="control-label col-xs-2">上级区域:</label>
				<div class="col-xs-3">
					<form:hidden path="parent.id"/>
					<form:input path="parent.name" readonly="true" cssClass="form-control input-sm"/>
	<%-- 				<sys:treeselect id="area" name="parent.id" value="${area.parent.id}" --%>
	<%-- 					labelName="parent.name" labelValue="${area.parent.name}" title="区域" --%>
	<%-- 					url="/sys/area/treeData" extId="${area.id}" cssClass="" --%>
	<!-- 					allowClear="true" /> -->
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"><font class="red">*</font>区域名称:</label>
				<div class="col-xs-3">
				<form:input path="name" htmlEscape="false" maxlength="50"
					class="form-control  input-sm required" />
				</div>
				<form:errors path="name" cssClass="error"></form:errors>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">区域编码:</label>
				<div class="col-xs-3">
				<form:input path="code" htmlEscape="false" maxlength="50" class="form-control input-sm"/>
				</div>
				<form:errors path="code" cssClass="error"></form:errors>
			</div>
			<div class="form-group" style="display: none">
				<label class="control-label  col-md-2">区域类型:</label>
				<div class="col-md-3">
				<form:select path="type" class="form-control input-sm">
					<form:options items="${fns:getDictList('sys_area_type')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				</div>
			</div>
		<div class="form-group">
			<label class="control-label col-md-2">备注:</label>
			<div class="col-md-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3"
					maxlength="200" class="form-control input-sm" />
			</div>
		</div>
			<div class="form-group">
				<div class="col-md-offset-2 col-md-10">
					<shiro:hasPermission name="sys:area:edit">
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