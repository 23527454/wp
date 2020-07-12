<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>线路列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
<!-- jQuery easyui datagrid filter-->
<script type="text/javascript"
	src="${ctxStatic}/jquery-easyui-1.4.2/grid/datagrid-filter.js"></script>
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
				<a class="accordion-toggle">线路列表<i
					class="icon-refresh pull-right" onclick="refreshTree();"></i></a>
			</div>
			<div id="ztree" class="ztree"></div>
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right">
			<iframe id="officeContent" src=""
				width="100%" height="91%" frameborder="0"></iframe>
		</div>
	</div>
	<script type="text/javascript">
	var setting = {
			check : {
				enable : true
			},
			view : {
				selectedMulti : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onCheck : zTreeOnCheck
			}
		};

		var tree2;
		function refreshTree() {
			$.getJSON("${ctx}/guard/line/treeData?time="+new Date(), function(data) {
				tree2 = $.fn.zTree.init($("#ztree"), setting, data);
				// 不选择父节点
				tree2.setting.check.chkboxType = {
					"Y" : "ps",
					"N" : "ps"
				};
				tree2.expandAll(true);
				tree2.checkAllNodes(true);
				
				zTreeOnCheck();
			});
		}

		function zTreeOnCheck(){
			var nodes = tree2.getCheckedNodes(true);
			var msg = "";
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id != '0') {
					msg = nodes[i].id + "," + msg;
				}
			}
			msg = msg.substring(0, msg.length - 1);
			
			$('#officeContent').attr(
					"src",
					"${ctx}/guard/lineEvent/list?nodes=" + msg);
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