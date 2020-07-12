<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				if (document.all['radio_hidden'].value == '0') {
					document.all['radio'][0].checked = true;
				} else if (document.all['radio_hidden'].value == '1') {
					document.all['radio'][1].checked = true;
				} else {
					document.all['radio'][0].checked = true;
					$("#radio_hidden").val("0");
				}

				$("#selectedOfficeId").val(parent.$("#office_id").val());
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

	function getRadio() {
		e = event.srcElement;
		if (e.type == "radio" && e.name == "radio") {
			if (e.value == 0) {
				$("#radio_hidden").val(0);
			} else if (e.value == 1) {
				$("#radio_hidden").val(1);
			}
		}
	}
	//字母数字
    jQuery.validator.addMethod("alnum", function(value, element) {
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
    }, "只能包括英文字母和数字");
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/moneyBox/">款箱信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/moneyBox/form?id=${moneyBox.id}">款箱信息<shiro:hasPermission
					name="guard:moneyBox:edit">${not empty moneyBox.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:moneyBox:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="moneyBox"
		action="${ctx}/guard/moneyBox/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<input type="hidden" name="selectedOfficeId" id="selectedOfficeId"/>
		<sys:message content="${message}" />

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>款箱编码：</label>
			<div class="col-xs-3">
				<form:input path="boxCode" htmlEscape="false" maxlength="16"
					class="form-control input-sm required" id="box_code" />
			</div>
			<form:errors path="boxCode" cssClass="error"></form:errors>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>款箱卡号：</label>
			<div class="col-xs-3">
				<form:input path="cardNum" htmlEscape="false" maxlength="12"
					class="form-control input-sm required" />
			</div>
			<form:errors path="cardNum" cssClass="error"></form:errors>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>所属网点：</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="office.id"
					value="${moneyBox.office.id}" labelName="office.name"
					labelValue="${moneyBox.office.name}" title="网点"
					selectIds="${moneyBox.office.id}"
					url="/sys/office/treeData" cssClass="required" allowClear="true"
					notAllowSelectRoot="true" />
			</div>
		</div>

		<form:input path="boxType" id="radio_hidden" type="hidden"/>

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>款箱类型：</label>
			<div class="col-xs-2">
				<div class="radio-inline">
					<span onclick="getRadio();"> 
						<input type="radio" name="radio" value="0"><label>早晚尾箱</label>
						<input type="radio" name="radio" value="1"><label>调拨款箱</label>
					</span>
				</div>
			</div>
		</div>


		<div style="display: none;">
			<div class="form-group">
				<label class="control-label col-xs-2">箱门状态：</label>
				<div class="col-xs-2">
					<form:select path="doorStatus" class="form-control input-sm "
						style="width:50%;">
						<form:options items="${fns:getDictList('door_status')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">电量信息：</label>
				<div class="col-xs-3">
					<form:input path="powerInfo" htmlEscape="false"
						class="form-control input-sm " />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">设封密钥：</label>
				<div class="col-xs-3">
					<form:input path="closePwd" htmlEscape="false"
						class="form-control input-sm " />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">异常状态：</label>
				<div class="col-xs-3">
					<form:input path="alarmStatus" htmlEscape="false"
						class="form-control input-sm " />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">异常时间：</label>
				<div class="col-xs-3">
					<input name="alarmDate" type="text" readonly="readonly"
						maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${moneyBox.alarmDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">设封时间：</label>
				<div class="col-xs-3">
					<input name="closeDate" type="text" readonly="readonly"
						maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${moneyBox.closeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">传感器状态：</label>
				<div class="col-xs-3">
					<form:input path="sensorStatus" htmlEscape="false" class="form-control input-sm " />
				</div>
			</div>

		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注信息：</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="255" class="input-xxlarge " />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:moneyBox:edit">
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