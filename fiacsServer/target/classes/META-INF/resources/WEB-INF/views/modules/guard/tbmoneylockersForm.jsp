<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>款箱管理</title>
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
		<li><a href="${ctx}/guard/tbmoneylockers/">款箱列表</a></li>
		<li class="active"><a href="${ctx}/guard/tbmoneylockers/form?id=${tbmoneylockers.id}">款箱<shiro:hasPermission name="guard:tbmoneylockers:edit">${not empty tbmoneylockers.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="guard:tbmoneylockers:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tbmoneylockers" action="${ctx}/guard/tbmoneylockers/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="control-label col-sm-2">款箱序号：</label>
				<div class="col-sm-4">
				<form:input path="nlockersn" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">款箱卡号：<span class="help-inline"><font color="red">*</font> </span></label>
				<div class="col-sm-4">
				<form:input path="szcardnumber" htmlEscape="false" maxlength="12" class="input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">款箱编码：</label>
				<div class="col-sm-4">
				<form:input path="szlockercode" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">关联网点：</label>
				<div class="col-sm-4">
				<sys:treeselect id="area" name="area.id" value="${tbmoneylockers.area.id}" labelName="area.name" labelValue="${tbmoneylockers.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">款箱类型：</label>
				<div class="col-sm-2">
				<form:select path="uclockertype" class="input-xlarge " style="width:100%;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ulocker_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">款箱状态：</label>
				<div class="col-sm-2">
				<form:select path="uclockerstatus" class="input-xlarge " style="width:100%;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('clocker_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<shiro:hasPermission name="guard:tbmoneylockers:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
</body>
</html>