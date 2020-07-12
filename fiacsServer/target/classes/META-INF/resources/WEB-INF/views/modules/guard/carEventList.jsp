<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆事件管理</title>
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
 			 url : "${ctx}/concurrency/car/listByLatestId?latestId=" + latestId + "&latestTime="+latestTime + "&nodes=" + $("#nodes").val()+"&time="+getTimestamp
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
			<td>{{= time}}</td>
			<td>{{= cardNum}}</td>
			<td>{{= carplate}}</td>
			<td>{{= admin}}</td>
			<td>{{= phone}}</td>
		</tr>
        </script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/carEvent/list?nodes=${nodes}">车辆事件列表</a></li>
	</ul>
	<p class="help-block">当前显示最新<span id="pageSize">N/A</span>记录。</p>
	<form:form id="searchForm" modelAttribute="carEvent"
		action="${ctx}/guard/carEvent/" method="post"
		class="breadcrumb form-inline" style="display:none;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<input id="nodes" name="nodes" htmlEscape="false" class="form-control"
			value="${nodes}" />

		<div class="form-group">
			<label>编号：</label>
			<form:input path="id" htmlEscape="false" class="form-control" />
		</div>
		<div class="form-group">
			<label>事件ID：</label>
			<form:input path="carId" htmlEscape="false" class="form-control" />
		</div>
		<div class="form-group">
			<label>设备ID：</label>
			<form:input path="equipmentId" htmlEscape="false"
				class="form-control" />
		</div>
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
				<th>时间</th>
				<th>车辆卡号</th>
				<th>车牌号</th>
				<th>负责人</th>
				<th>联系方式</th>
			</tr>
		</thead>
		<tbody id="eventMessages">
			<c:forEach items="${page.list}" var="carEvent" varStatus="varStatus">
				<tr data-id="${carEvent.id}" data-time="${carEvent.time}">
<%-- 					<td style="text-align: center;">${varStatus.count }</td> --%>
					<td>${carEvent.officeName}</td>
					<td>${carEvent.time}</td>
					<td>${carEvent.cardNum}</td>
					<td>${carEvent.carplate}</td>
					<td>${carEvent.admin}</td>
					<td>${carEvent.phone}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>