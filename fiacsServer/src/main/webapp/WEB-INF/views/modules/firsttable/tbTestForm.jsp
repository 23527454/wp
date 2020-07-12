<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
	<meta name="decorator" content="default"/>
	<%--<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>--%>
	<script type="text/javascript">
		$(document).ready(
				function() {
					var id = $("#equipment_id").val();
					$("#equipment_ids2").val(id);
					if (id != "" && typeof (id) != "undefined") {
						$("#officeButton").addClass("disabled");
					}else{
// 					$("#ip").val("192.168.1.118");

						var office_id=parent.$("#office_id").val();
						var office_name=parent.$("#office_name").val();
						$("#controlPos").val(office_name);
						var office_type=parent.$("#office_type").val();

						if(office_id!=null && office_id!="" && office_id!="0" && typeof (office_id) != "undefined"){
							$.ajax({
								type: "GET",
								url: "${ctx}/guard/equipment/getNumber?id="+office_id
							}).done(function(response) {
								var data = JSON.parse(response);
								var data_number= data.number;
								if(data_number=="0"){
									$("#officeName").val(office_name);
									$("#controlPos").val(office_name);
									//网点ID
									$("#equipment_ids").val(office_id);
									$("#officeId").val(office_id);
								}
							}).fail(function() {
							});
						}
					}
				}
		);
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/firsttable/tbTest/">测试列表</a></li>
		<li class="active"><a href="${ctx}/firsttable/tbTest/form?id=${tbTest.id}">测试<shiro:hasPermission name="firsttable:tbTest:edit">${not empty tbTest.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="firsttable:tbTest:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tbTest" action="${ctx}/firsttable/tbTest/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<form:input path="equipment_ids" htmlEscape="false" maxlength="64"
					class="form-control input-sm required" id="equipment_ids" type="hidden" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 设备名称：</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="office.id" value="${tbTest.office.id}" labelName="office.name"
								labelValue="${tbTest.office.name}" title="网点" url="/sys/office/equipmentTreeData" cssClass="required" allowClear="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">phone：</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">name：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">age：</label>
			<div class="controls">
				<form:input path="age" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">remarks：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<%--<div class="control-group">
			<label class="control-label">office_id：</label>
			<div class="controls">
				<sys:treeselect id="office" name="office.id" value="${tbTest.office.id}" labelName="office.name" labelValue="${tbTest.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>--%>
		<div class="form-actions">
			<shiro:hasPermission name="firsttable:tbTest:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>