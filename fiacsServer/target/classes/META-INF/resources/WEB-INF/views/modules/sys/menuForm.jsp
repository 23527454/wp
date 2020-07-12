\<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<li class="active"><a href="${ctx}/sys/menu/form?id=${menu.id}&parent.id=${menu.parent.id}">菜单<shiro:hasPermission name="sys:menu:edit">${not empty menu.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="control-label col-md-2"><font color="red">*</font>上级菜单:</label>
			<div class="col-md-3">
                <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
					title="菜单" url="/sys/menu/treeData" extId="${menu.id}" cssClass="required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font color="red">*</font>名称:</label>
			<div class="col-md-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">链接:</label>
			<div class="col-md-3">
				<form:input path="href" htmlEscape="false" maxlength="2000" class="form-control input-sm"/>
				<span class="help-inline">点击菜单跳转的页面</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">目标:</label>
			<div class="col-md-3">
				<form:input path="target" htmlEscape="false" maxlength="10" class="form-control input-sm"/>
				<span class="help-inline">链接地址打开的目标窗口，默认：mainFrame</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">图标:</label>
			<div class="col-md-3">
				<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font color="red">*</font>排序:</label>
			<div class="col-md-3">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits form-control input-sm"/>
				<span class="help-inline">排列顺序，升序。</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2"><font color="red">*</font>可见:</label>
			<div class="col-md-3">
				<div class="radio-inline">
					<form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
					<span class="help-inline">该菜单或操作是否显示到系统菜单中</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">权限标识:</label>
			<div class="col-md-3">
				<form:input path="permission" htmlEscape="false" maxlength="100" class="form-control input-sm"/>
				<span class="help-inline">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-2">备注:</label>
			<div class="col-md-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control input-sm"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>