<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>维保员事件管理</title>
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
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/safeGuardEventQuery/list">维保员事件查询列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/safeGuardEventQuery/form?id=${safeGuardEventQuery.id}">维保员事件查看</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="safeGuardEventQuery"
		action="${ctx}/guard/safeGuardEventQuery/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-7">
				<div class="form-group">
					<label class="control-label col-xs-4">网点名称：</label>
					<div class="col-xs-6">
						<form:input path="office.name" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">人员姓名：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="personName" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-xs-4">指纹号：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="fingerNumLabel" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				
				<%-- <div class="form-group">
					<label class="control-label col-xs-4">事件类型：</label>
					<div class="col-xs-6">
						<form:input path="eventType" htmlEscape="false" maxlength="32"
							class="form-control input-sm "
							value="${fns:getDictLabel(safeGuardEventQuery.eventType, 'event_type', '')}" />
					</div>
				</div> --%>
				<div class="form-group">
					<label class="control-label col-xs-4">事件时间：<span
						class="help-inline"></span></label>
					<div class="col-xs-6">
						<form:input path="time" htmlEscape="false"
							class="form-control input-sm required"
							/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-xs-4">照片：</label>
					<div class="col-xs-6">
						<a rel="popover" data-img="${ctx}/guard/image/image?id=${safeGuardEventQuery.eventDetailId}">
							<img src="${ctx}/guard/image?id=${safeGuardEventQuery.eventDetailId}" style="width: 200px; height: 126px" class="img-thumbnail"/>
						</a>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-offset-3 col-xs-9">
						<input id="btnCancel" class="btn" type="button" value="返 回"
							onclick="history.go(-1)" />
					</div>
				</div>
			</div>
		</div>
	</form:form>
	<div style="height: 90px;"></div>
</div>
</body>
</html>