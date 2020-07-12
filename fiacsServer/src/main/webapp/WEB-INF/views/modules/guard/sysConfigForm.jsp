<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆事件管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
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
	<br />
	<br />
	<form:form id="inputForm" modelAttribute="sysConfig"
		action="${ctx}/guard/sysConfig/form" method="post"
		class="form-horizontal">
		<sys:message content="${message}" />
		<div class="form-group">
			<div class="control-label col-xs-4">
				<h3>基本配置</h3>
			</div>
		</div>
		<br />
		<br />
		<div class="form-group">
			<label class="control-label col-xs-2">单位名称：<span
				class="help-inline"> </span></label>
			<div class="col-xs-4">
				<form:input path="corporationName" htmlEscape="false"
					class="input-xs form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">系统名称<font class="red">*</font>：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="systemName" htmlEscape="false"
					class="input-xs form-control required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">专员数量<font class="red">*</font>：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="interNum" htmlEscape="false"
					class="input-xs form-control required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">押款员数量<font class="red">*</font>：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="superGoNum" htmlEscape="false"
					class="input-xs form-control required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">任务切割时间<font class="red">*</font>：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<input name="dtTaskCut" readonly="readonly" 
					class="input-xs form-control Wdate required"
					value="${sysConfig.dtTaskCut}"
							onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">有效期(年)<font class="red">*</font>：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input type="number" min="1" path="staffValidity" htmlEscape="false"
					class="input-xs form-control required" />
			</div>
		</div>
	
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2">版本号：<span
				class="help-inline"></span></label>
			<div class="col-xs-4">
				<form:input path="versionNumber" htmlEscape="false"
					class="input-xs form-control" style="width: 30%;" />
			</div>
		</div>
		<div style="display: none">
			<div class="form-group">
				<label class="control-label col-xs-2">交接事件再次上传的条数：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="resetUploadConnectItems" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">异常报警再次上传的条数：<span
					class="help-inline"> </span></label>
				<div class="col-xs-4">
					<form:input path="resetUploadAlarmItems" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">时间点1：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="syncTime1" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">时间点2：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="syncTime2" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">时间点3：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="syncTime3" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">是否开启看门狗运行：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="isDogRun" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">是否加锁保护：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="isLockProtect" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">是否自动登录：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="isAutoLogin" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">开机启动：<span
					class="help-inline"></span></label>
				<div class="col-xs-4">
					<form:input path="autoOpenWhenStartComputer" htmlEscape="false"
						class="input-xs form-control" style="width: 30%;" />
				</div>
			</div>
		</div>
		<br/>
		<div class="form-group">
			<div class="control-label col-xs-4">
				<shiro:hasPermission name="guard:sysConfig:view">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
			</div>
		</div>
	</form:form>
	</div>
</body>
</html>