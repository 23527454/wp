<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>异常报警事件管理</title>
<script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
<meta name="decorator" content="default" />

<script type="text/javascript">

	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}


	$(document).ready(function() {
		var $tr = $("#eventMessages tr");
		$("#pageSize").html($tr.length);
		setInterval(refreshConnectEvent, 3000);
	});

	function refreshConnectEvent(){
		var $tr = $("#eventMessages tr");
		 var latestId = $tr.eq(0).attr("data-id");
		 var latestTime = $tr.eq(0).attr("data-time");
		 var getTimestamp=new Date().getTime();
		 $.ajax({
  			 type : "GET",
  			 url : "${ctx}/concurrency/alarm/listByLatestId?latestId=" + latestId + "&latestTime="+latestTime+"&nodes="+$("#nodes").val()+"&time="+getTimestamp
  		 }).done(function(list) {
  			if(list.length == 0){
  				return;
  			}
  			$tr.removeClass("warning-fixed");
  			$("#template").tmpl(list).hide().prependTo("#eventMessages").fadeIn();
  			for(var i=$("#eventMessages tr").length; i >1000 ; i--){
  				$("#eventMessages tr").eq(i-1).remove();
			}
  			$("#pageSize").html($("#eventMessages tr").length);
  			
  			$.each(list, function(idx, elemt){
	  			var reg = /[^}{}]+(?=})/g;
				var text = $("#tts").val();
				var toSpeachText = text.format(elemt);
				textToSpeach(toSpeachText);
  			});
  		 }).fail(function() {
  		 });
	}
	//
</script>

<script id="template" type="text/x-jquery-tmpl">
        <tr  data-id="{{= id}}" class="warning-fixed" data-time="{{= time}}">
				<td>
					<a href="${ctx}/guard/alarmEvent/form?id={{= id}}">{{= officeName}}</a>
				</td>
				<td>
					{{= time}}
				</td>
				<td>
					{{= taskName}}
				</td>
				<td>
					{{= taskId}}
				</td>
				<td>
					{{= eventType}}
				</td>
				<td>
					{{= areaName}}
				</td>
				<td>
                    {{= handleUserName}}
                </td>
			</tr>
        </script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/alarmEvent/list?nodes=${nodes}">异常报警事件列表</a></li>
	</ul>
	<p class="help-block">当前显示最新<span id="pageSize">N/A</span>记录。</p>
	<form:form id="searchForm" modelAttribute="alarmEvent"
		action="${ctx}/guard/alarmEvent/" method="post" style="display: none;"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" value="${page.pageSize}" />
		<input id="nodes" name="nodes" htmlEscape="false" class="form-medium"
			value="${ nodes}" />
		<label>编号：</label>
		<form:input path="id" htmlEscape="false" class="form-medium" />
		<label>设备ID：</label>
		<form:input path="equipmentId" htmlEscape="false"
			class="form-medium" />
		<label>事件序号：</label>
		<form:input path="recordId" htmlEscape="false" class="form-control" />
		<label>事件类型：</label>
		<form:input path="eventType" htmlEscape="false" maxlength="1"
			class="form-control" />
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />


	</form:form>
	<input id="tts" value="${tts}" style="display: none;">
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>网点</th>
				<th>时间</th>
				<th>班组</th>
				<th>班次</th>
				<th>异常类型</th>
				<th>区域</th>
				<th>处理人</th>
			</tr>
		</thead>
		<tbody id="eventMessages">
			<c:forEach items="${page.list}" var="alarmEvent" varStatus="varStatus">
				<tr data-id="${alarmEvent.id}" data-time="${alarmEvent.time }">
					<td><a href="${ctx}/guard/alarmEvent/form?id=${alarmEvent.id}&nodes=${nodes}">${alarmEvent.officeName}</a></td>
					<td>${alarmEvent.time}</td>
					<td>${alarmEvent.taskName}</td>
					<td>${alarmEvent.taskId}</td>
					<td>${alarmEvent.eventType}</td>
					<td>${alarmEvent.areaName}</td>
					<td>${alarmEvent.handleUserName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>