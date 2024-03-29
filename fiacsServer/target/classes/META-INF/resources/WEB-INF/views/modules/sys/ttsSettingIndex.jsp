<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>语音设置</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript" src="${ctxStatic}/guard/main.js"></script>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
</style>
</head>
<body>
	<sys:message content="${message}" />
	<div id="content" class="row-fluid">
		<div id="left" class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle">语音设置<i
					class="icon-refresh pull-right" onclick="refreshTree();"></i></a>
			</div>
			<div id="ztree" class="ztree"></div>
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right">
			<iframe id="officeContent" src="${ctx}/sys/ttsSetting/form?voiceType=connect"
				width="100%" height="91%" frameborder="0"></iframe>
		</div>
	</div>
	<script type="text/javascript">
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : '0'
				}
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					var id = treeNode.id == '0' ? 'connect' : treeNode.id;
					$('#officeContent').attr(
							"src",
							"${ctx}/sys/ttsSetting/form?voiceType=" + treeNode.value);
				}
			}
		};

		function refreshTree() {
			var data = [{name: "交接事件", pId: "0", id: "1", pIds: "0,", value:"connect"},
			            {name: "异常报警", pId: "0", id: "2", pIds: "0,", value:"alarm"},
			            {name: "车辆事件", pId: "0", id: "3", pIds: "0,", value:"car"},
			            {name: "门禁报警事件", pId: "0", id: "4", pIds: "0,", value:"doorAlarm"},
			            {name: "门禁出入事件", pId: "0", id: "5", pIds: "0,", value:"doorControl"},
			            {name: "维保员事件", pId: "0", id: "6", pIds: "0,", value:"safeGuard"}]
			$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
		}
		refreshTree();

		var leftWidth = 180; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize() {
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({
				"overflow-x" : "hidden",
				"overflow-y" : "hidden"
			});
			mainObj.css("width", "auto");
			frameObj.height(strs[0] - 5);
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width(
					$("#content").width() - leftWidth - $("#openClose").width()
							- 5);
			$(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>