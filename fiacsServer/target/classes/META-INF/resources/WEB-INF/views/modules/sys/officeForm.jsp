
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网点管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				
				//$("#officeButton").addClass("disabled");
				
// 				if (document.all['radio_hidden'].value == '0') {
// 					document.all['radio'][0].checked = true;
// 				} else if (document.all['radio_hidden'].value == '1') {
// 					document.all['radio'][1].checked = true;
// 				} else if (document.all['radio_hidden'].value == '2') {
// 					document.all['radio'][2].checked = true;
// 				} else if (document.all['radio_hidden'].value == '3') {
// 					document.all['radio'][3].checked = true;
// 				} else if (document.all['radio_hidden'].value == '4') {
// 					document.all['radio'][4].checked = true;
// 					document.all['radio'][0].disabled=true;
// 					document.all['radio'][1].disabled=true;
// 					document.all['radio'][2].disabled=true;
// 					document.all['radio'][3].disabled=true;
// 				} else {
// 					document.all['radio'][3].checked = true;
// 					$("#radio_hidden").val("3");
// 				}

				$("#name").focus();
				$("#inputForm").validate({
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
				
// 				$(".radio-group .radio-item").click(function(){
// 					$(this).find("[type=radio]").attr({checked:true});
// 				});
			});
	
</script>
</head>
<body>
	<div class="container-fluid">
	
	<ul class="nav nav-tabs">
		<li><a
			href="${ctx}/sys/office/list?id=${office.parent.id}&parentIds=${office.parentIds}">网点列表</a></li>
		<li class="active"><a
			href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}&selectedOfficeId=${selectedOfficeId}">网点<shiro:hasPermission
					name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="office"
		action="${ctx}/sys/office/save?selectedOfficeId=${selectedOfficeId}" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">上级网点:</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="parent.id"
					value="${office.parent.id}" labelName="parent.name"
					labelValue="${office.parent.name}" title="机构"
					url="/sys/office/treezeeData" extId="${office.id}"
					allowClear="${office.currentUser.admin}" />
			</div>
			<form:errors path="parent" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>归属区域:</label>
			<div class="col-xs-3">
				<sys:treeselect id="area" name="area.id" value="${office.area.id}"
					labelName="area.name" labelValue="${office.area.name}" title="区域"
					url="/sys/area/treeData" cssClass="required"
					notAllowSelectRoot="true" allowClear="${office.currentUser.admin}" />
			</div>

		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>网点名称:</label>
			<div class="col-xs-3">
				<form:input path="name" htmlEscape="false" maxlength="50"
					class="form-control input-sm required" />
			</div>
			<form:errors path="name" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">网点编码:</label>
			<div class="col-xs-3">
				<form:input path="code" htmlEscape="false" maxlength="50"
					class="form-control input-sm digits" />
			</div>
			<form:errors path="code" cssClass="error"></form:errors>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2">网点类型:</label>
			<div class="col-xs-6">
				<div class="radio-inline">
					<form:radiobuttons path="type" class="required" 
					items="${fns:getDictList('sys_office_type')}" 
					itemLabel="label" itemValue="value" htmlEscape="false" 
					delimiter="&nbsp;&nbsp;&nbsp;"/>
				</div>
			</div>
			<form:errors path="type" cssClass="error"></form:errors>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2">网点级别:</label>
			<div class="col-xs-3">
				<form:select path="grade" class="form-control input-sm">
					<form:options items="${fns:getDictList('sys_office_grade')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2">是否可用:</label>
			<div class="col-xs-3">
				<form:select path="useable" class="form-control input-sm">
					<form:options items="${fns:getDictList('yes_no')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">联系地址:</label>
			<div class="col-xs-3">
				<form:input path="address" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">邮政编码:</label>
			<div class="col-xs-3">
				<form:input path="zipCode" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">负责人:</label>
			<div class="col-xs-3">
				<form:input path="master" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">电话:</label>
			<div class="col-xs-3">
				<form:input path="phone" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">传真:</label>
			<div class="col-xs-3">
				<form:input path="fax" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">邮箱:</label>
			<div class="col-xs-3">
				<form:input path="email" class="form-control input-sm" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注:</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3"
					maxlength="200" class="form-control input-sm" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="sys:office:edit">
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