<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网点列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript" src="${ctxStatic}/guard/main.js"></script>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 0px;
	padding: 10px 0 0 10px;
}
</style>
</head>
<body>
	<sys:message content="${message}" />
	<div id="content" class="row-fluid">
		<div id="topWrap" style="padding: 10px;">
			<form:form id="searchForm" method="post"
				class="form-inline">
				<div class="form-group">
					<select  id ="officeSelect" class="form-control input-sm">
						<c:forEach items="${officeList}" var="item">
							<option value="${item.id}">${item.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<input name="dateStr" type="text"
						readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
						value="${systemDate}" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
				<div class="form-group">
						<input id="btnSubmit" class="btn btn-primary" type="button" value="查询" />
				</div>
			</form:form>
		</div>
		
<!-- 		<div id="left" class="accordion-group"> -->
<!-- 		</div> -->
<!-- 		<div id="openClose" class="close"></div> -->
		<div id="right">
			<iframe id="officeContent" src="" width="100%" height="91%"
				frameborder="0"></iframe>
		</div>
	</div>
	<script type="text/javascript">
		var leftWidth = 0; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize() {
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({
				"overflow-x" : "hidden",
				"overflow-y" : "hidden"
			});
			mainObj.css("width", "auto");
			frameObj.height(strs[0] - 50);
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width(
					$("#content").width() - leftWidth - $("#openClose").width()
							- 5);
		}
		$(function(){
			var selectedOfficeId = $("#officeSelect option:selected").val();
			var dateStr=$("[name=dateStr]").val();
			$('#officeContent').attr("src", "${ctx}/guard/coffer/connect?officeId=" + selectedOfficeId+ "&date="+dateStr);
		})
		
		$("#officeSelect").change(function(){
			var selectedOfficeId = $("#officeSelect option:selected").val();
			initDateList(selectedOfficeId);
		});
		$("#btnSubmit").click(function(){
			var selectedOfficeId = $("#officeSelect option:selected").val();
			var dateStr=$("[name=dateStr]").val();
			
			$('#officeContent').attr("src", "${ctx}/guard/coffer/connect?officeId=" + selectedOfficeId + "&date="+dateStr);
		});
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>