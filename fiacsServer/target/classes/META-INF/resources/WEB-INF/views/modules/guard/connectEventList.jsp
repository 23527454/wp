<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>交接事件管理</title>
<meta name="decorator" content="default" />
<%-- <script src="${ctxStatic}/custom/atmosphere.js"></script> --%>
<script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
<script type="text/javascript">

	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	$(function(){
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
  			 url : "${ctx}/concurrency/connect/listByLatestId?latestId=" + latestId + "&latestTime="+latestTime+"&nodes=" + $("#nodes").val()+"&time="+getTimestamp
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
        <tr data-id="{{= id}}" class="warning-fixed" data-time="{{= time}}">	
				<td>
					<a href="${ctx}/guard/connectEvent/form?id={{= id}}">
							{{= officeName}} </a>
				</td>
				<td>
					{{= time}}
				</td>
				<td>
					{{= carplate}}
				</td>
				<td>
					{{= cardNum}}
				</td>
				<td>
					{{= taskType}}
				</td>
				<td>
					{{= taskName}}
				</td>
				<td>
					{{= taskId}}
				</td>
				<td>
					{{= areaName}}
				</td>
				<shiro:hasPermission name="guard:connectEvent:edit"><td>
    				<a href="${ctx}/guard/connectEvent/form?id={{= id}}">修改</a>
					<a href="${ctx}/guard/connectEvent/delete?id={{= id}}" onclick="return confirmx('确认要删除该交接事件吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
        </script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/connectEvent/?nodes=${nodes}">交接事件列表</a></li>
	</ul>
	<p class="help-block">当前显示最新<span id="pageSize">N/A</span>记录。</p>
	<form:form id="searchForm" modelAttribute="connectEvent"
		action="${ctx}/guard/connectEvent/list" method="post"
		class="breadcrumb form-inline" style="display:none;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<input id="nodes" name="nodes" htmlEscape="false" class="form-control"
			value="${nodes}" />
<!-- 		<input id="type" name="type" htmlEscape="false" class="form-control" -->
<%-- 			value="${page.refreshType}" /> --%>

		<div class="form-group">
			<label>编号：</label>
			<form:input path="id" htmlEscape="false" class="form-control" />
		</div>
		<div class="form-group">
			<label>设备编号：</label>
			<form:input path="equipmentId" htmlEscape="false"
				class="form-control" />
		</div>
		<div class="form-group">
			<label>任务类型：</label>
			<form:select path="taskType" class="input-medium">
				<form:option value="" label="" />
				<form:options items="${fns:getDictList('taskType')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />


	</form:form>
	<sys:message content="${message}" />
	<input id="tts" value="${ tts}" style="display: none;">
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>网点</th>
				<th>时间</th>
				<th>车牌号</th>
				<th>车辆卡号</th>
				<th>任务类型</th>
				<th>班组</th>
				<th>班次</th>
				<th>区域</th>
			</tr>
		</thead>
		<tbody id="eventMessages">
<!-- 			<tr id="placeHolder"></tr> -->
			<c:forEach items="${page.list}" var="connectEvent" varStatus="varStatus">
				<tr data-id="${connectEvent.id}" data-time="${connectEvent.time}">
					<td><a href="${ctx}/guard/connectEvent/form?id=${connectEvent.id}&nodes=${nodes}">
							${connectEvent.officeName} </a>
					</td>
					<td>${connectEvent.time}</td>
					<td>${connectEvent.carplate}</td>
					<td>${connectEvent.cardNum}</td>
					<td>${connectEvent.taskType}</td>
					<td>${connectEvent.taskName}</td>
					<td>${connectEvent.taskId}</td>
					<td>${connectEvent.areaName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>

</body>
</html>