<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网点列表</title>
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
	_margin-top: 0px;
	padding: 10px 0 0 10px;
}
</style>
</head>
<body>
	<sys:message content="${message}" />
	<div id="content" class="row-fluid">
		<div id="left" class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle">区域列表<i
					class="icon-refresh pull-right" onclick="refreshTree();"></i></a>
			</div>
			<div id="search" class="input-group" style="padding:10px 0 0 10px;">
				<input type="text" class="form-control input-sm empty" id="key" name="key" maxlength="50" placeholder="请输入关键字">
				<span class="input-group-btn">
					<button class="btn btn-default btn-sm" id="btn" onclick="searchNode()"><i class="glyphicon glyphicon-search"></i></button>
				</span>
			</div>			<div id="ztree" class="ztree"></div>
			<input htmlEscape="false" id="area_id"
				class="input-xlarge required" style="display: none" /> <input
				htmlEscape="false" id="area_name"
				class="input-xlarge required" style="display: none" />
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right">
			<iframe id="officeContent" src="${ctx}/guard/line/list"
				width="100%" height="91%" frameborder="0"></iframe>
		</div>
	</div>
	<script type="text/javascript">
	var key, lastValue = "",  nodeList = [], type = getQueryString("type", "${url}");
	var tree, setting = {
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
					$('#officeContent').attr(
							"src",
							"${ctx}/guard/line/list?area.id=" + treeNode.id);
					$('#area_id').val(treeNode.id);
					$('#area_name').val(treeNode.name);
				}
			}
		};

		function refreshTree() {
			$.getJSON("${ctx}/sys/area/treeData?time="+new Date(), function(data) {
				tree=$.fn.zTree.init($("#ztree"), setting, data);
				tree.expandAll(true);
			});
			key = $("#key");
			key.bind('keydown', function (e){if(e.which == 13){searchNode();}});
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
		//搜索节点
		function searchNode() {
			// 取得输入的关键字的值
			var value = $.trim(key.get(0).value);
			// 按名字查询
			var keyType = "name";<%--
			if (key.hasClass("empty")) {
				value = "";
			}--%>
			// 如果和上次一次，就退出不查了。
			if (lastValue === value) {
				return;
			}
			// 保存最后一次
			lastValue = value;
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				return;
			}
			hideAllNode(nodes);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			updateNodes(nodeList);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						tree.expandNode(nodes[i],false,false,false,false);
					}else{
						tree.expandNode(nodes[i],true,true,false,false);
					}
					tree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			tree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				//展开当前节点的父节点
				tree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					tree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					tree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				tree.showNode(nodeList[i].getParentNode());
				//展开根节点
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>