<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门禁警报管理</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/custom/atmosphere.js"></script>
<script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
<script type="text/javascript">
	//var msg = "";
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
 			 url : "${ctx}/guard/doorInOutEvent/listByLatestId?latestId=" + latestId + "&latestTime="+latestTime + "&nodes=" + $("#nodes").val()+"&time="+getTimestamp
 		 }).success(function(list) {
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
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>


<script id="template" type="text/x-jquery-tmpl">
        <tr class="warning-fixed"  data-id="{{= id}}" data-time="{{= time}}">
			<td>{{= officeName}}</td>
			<td>{{= personName}}</td>
			<td>{{= fingerNumLabel}}</td>
			<td>{{= doorPosName}}</td>
			<td>{{= accessStatusName}}</td>
			<td>{{= eventTypeName}}</td>
			<td>{{= time}}</td>
			<td>{{= handleStatusName}}</td>
			<td>{{= authorType}}</td>
			<td>{{= handleUserName}}</td>
			<td>{{= handleModeName}}</td>
			<td>{{= handleTime}}</td>
			<td>{{= remark}}</td>
		</tr>
        </script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/doorInOutEvent/list?nodes=${nodes}">门禁出入事件列表</a></li>
	</ul>
	<p class="help-block">当前显示最新<span id="pageSize">N/A</span>记录。</p>
	<form:form id="searchForm" modelAttribute="doorInOutEvent"
		action="${ctx}/guard/doorInOutEvent/" method="post"
		class="breadcrumb form-inline" style="display:none;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<input id="nodes" name="nodes" htmlEscape="false" class="form-control"
			value="${nodes}" />

		
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />


	</form:form>
	<sys:message content="${message}" />
	<input id="tts" value="${ tts}" style="display: none;">
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
<!-- 				<th style="width: 30px;">序号</th> -->
				<th>网点</th>
				<th>人员</th>
				<th>指纹号</th>
				<th>门号</th>
				<th>进出状态</th>
				<th>事件类型</th>
				<th>事件时间</th>
				<th>处理状态</th>
				<th>认证类型</th>
				<th>处理人</th>
				<th>处理方式</th>
				<th>处理时间</th>
				<th>备注</th>
				
			</tr>
		</thead>
		<tbody id="eventMessages">
			<c:forEach items="${page.list}" var="doorInOutEvent" varStatus="varStatus">
				<tr data-id="${doorInOutEvent.id}" data-time="${doorInOutEvent.time}">
<%-- 					<td style="text-align: center;">${varStatus.count }</td> --%>
					<td>
					<a href="${ctx}/guard/doorInOutEvent/form?id=${doorInOutEvent.id}&nodes=${nodes}">
							${doorInOutEvent.officeName} </a>
					</td>
					<td>${doorInOutEvent.personName}</td>
					<td>${doorInOutEvent.fingerNumLabel}</td>
					<td>${fns:getDictLabel(doorInOutEvent.doorPos, 'door_pos', '')}</td>
					<td>${fns:getDictLabel(doorInOutEvent.accessStatus, 'door_access_status', '')}</td>
					<td>${fns:getDictLabel(doorInOutEvent.eventType, 'door_control_event_type', '')}</td>
					<td>${doorInOutEvent.time}</td>
					<td>${fns:getDictLabel(doorInOutEvent.handleStatus, 'door_handle_status', '')}</td>
					<td>${fns:getDictLabel(doorInOutEvent.authorType, 'door_open_type', '')}</td>
					<td>${doorInOutEvent.handleUserName}</td>
					<td>
					<c:if test="${doorInOutEvent.eventType=='6'}">
						${fns:getDictLabel(doorInOutEvent.handleMode, 'door_control_handle_mode', '')}
					</c:if>
					
					</td>
					<td>${doorInOutEvent.handleTime}</td>
					<td>${doorInOutEvent.remark}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>