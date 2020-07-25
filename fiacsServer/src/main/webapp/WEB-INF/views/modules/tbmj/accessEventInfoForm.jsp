<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门禁出入事件管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {

				$('a[rel=popover]').popover(
						{
							container : 'body',
							html : true,
							trigger : 'hover',
							placement : 'left',
							content : function() {
								return '<img src="' + $(this).data('img') + '" width="256px" height="256px"/>';
							}
						});

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
	function addRow(list, idx, tpl, row) {
		$(list).append(Mustache.render(tpl, {
			idx : idx,
			delBtn : true,
			row : row
		}));
		$(list + idx).find("select").each(function() {
			$(this).val($(this).attr("data-value"));
		});
		$(list + idx).find("input[type='checkbox'], input[type='radio']").each(
				function() {
					var ss = $(this).attr("data-value").split(',');
					for (var i = 0; i < ss.length; i++) {
						if ($(this).val() == ss[i]) {
							$(this).attr("checked", "checked");
						}
					}
				});
	}
	function delRow(obj, prefix) {
		var id = $(prefix + "_id");
		var delFlag = $(prefix + "_delFlag");
		if (id.val() == "") {
			$(obj).parent().parent().remove();
		} else if (delFlag.val() == "0") {
			delFlag.val("1");
			$(obj).html("&divide;").attr("title", "撤销删除");
			$(obj).parent().parent().addClass("error");
		} else if (delFlag.val() == "1") {
			delFlag.val("0");
			$(obj).html("&times;").attr("title", "删除");
			$(obj).parent().parent().removeClass("error");
		}
	}

	function goBack(){
		window.location.href = document.referrer;
	}
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tbmj/accessEventInfo/list?nodes=${accessEventInfo.nodes}">门禁出入事件列表</a></li>
		<li class="active"><a
			href="${ctx}/tbmj/accessEventInfo/form?id=${accessEventInfo.id}&nodes=${accessEventInfo.nodes}">门禁出入事件查看</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="accessEventInfo"
		action="${ctx}/tbmj/accessEventInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label col-xs-4">网点名称：</label>
					<div class="col-xs-6">
						<form:input path="officeName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">人员姓名：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="staff.name" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">指纹号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="fingerInfo.fingerNum" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">门号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="doorPos" htmlEscape="false"
							class="form-control input-sm required"
							value="${fns:getDictLabel(accessEventInfo.doorPos, 'door_pos', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">进出状态：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="accessStatus" htmlEscape="false"
							class="form-control input-sm required"
							value="${fns:getDictLabel(accessEventInfo.accessStatus, 'door_access_status', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">事件类型：</label>
					<div class="col-xs-6">
						<form:input path="eventType" htmlEscape="false" maxlength="32"
							class="form-control input-sm "
							value="${fns:getDictLabel(accessEventInfo.eventType, 'door_control_event_type', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">发生时间：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="eventDate" htmlEscape="false"
							class="form-control input-sm required"
							/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="goBack();" />
					</div>
				</div>
			</div>
			<div class="col-xs-6">
			<div class="form-group">
					<label class="control-label col-xs-4">处理状态：</label>
					<div class="col-xs-6">
						<form:input path="handleStatus" htmlEscape="false" maxlength="32"
							class="form-control input-sm "
							value="${fns:getDictLabel(accessEventInfo.handleStatus, 'door_handle_status', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">处理人：</label>
					<div class="col-xs-6">
						<form:input path="handleUser.name" htmlEscape="false" maxlength="32"
							class="form-control input-sm "/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">处理方式：</label>
					<div class="col-xs-6">
						<form:input path="handleMode" htmlEscape="false" maxlength="32"
							class="form-control input-sm "
							value="${fns:getDictLabel(accessEventInfo.handleMode, 'door_control_handle_mode', '')}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">处理时间：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="handleTime" htmlEscape="false"
							class="form-control input-sm required"
							/>
					</div>
				</div>
				
				
				 <div class="form-group">
					<label class="control-label col-xs-4">抓拍照片：</label>
					<div class="col-xs-6">
						<a rel="popover" data-img="${ctx}/tbmj/image?id=${accessEventInfo.eventDetailId}">
							<img src="${ctx}/tbmj/image?id=${accessEventInfo.eventDetailId}" style="width: 200px; height: 126px" class="img-thumbnail"/>
						</a>
					</div>
				</div> 
			</div>
		</div>
	</form:form>
	<div style="height: 90px;"></div>
</div>
</body>
</html>