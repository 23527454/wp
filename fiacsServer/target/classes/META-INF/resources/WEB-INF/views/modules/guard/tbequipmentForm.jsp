<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>设备信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
		<li><a href="${ctx}/guard/tbequipment/">设备信息列表</a></li>
		<li class="active"><a href="${ctx}/guard/tbequipment/form?id=${tbequipment.id}">设备信息<shiro:hasPermission name="guard:tbequipment:edit">${not empty tbequipment.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="guard:tbequipment:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tbequipment" action="${ctx}/guard/tbequipment/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="control-label col-sm-2">设备类型：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-2">
				<form:select path="nequiptype" class="input-xlarge required" style="width:100%;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('equip_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">IP地址：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="szip" htmlEscape="false" maxlength="32" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">端口：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="nport" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">网点名称：</label>
				<div class="col-sm-4">
				<sys:treeselect id="area" name="area.id" value="${tbequipment.area.id}" labelName="area.name" labelValue="${tbequipment.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">序列号：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="szserialnum" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">中心上传地址：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="szuploadeventsrvip" htmlEscape="false" maxlength="32" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">中心端口：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="nuploadeventsrvport" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">网关：</label>
				<div class="col-sm-4">
				<form:input path="sznetgate" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">子网掩码：</label>
				<div class="col-sm-4">
				<form:input path="sznetmask" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">打印服务器IP：</label>
				<div class="col-sm-4">
				<form:input path="szprintserverip" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">打印服务器端口：</label>
				<div class="col-sm-4">
				<form:input path="nprintserverport" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">网点类型：</label>
				<div class="col-sm-2">
				<form:select path="nsitetype" class="input-xlarge " style="width:100%;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('site_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">备注：</label>
				<div class="col-sm-4">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<shiro:hasPermission name="guard:tbequipment:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>