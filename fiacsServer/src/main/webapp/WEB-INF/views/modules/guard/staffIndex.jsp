<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网点列表</title>
<meta name="decorator" content="default" />
<meta http-equiv="Pragma" content="no-cache">  
<meta http-equiv="Cache-Control" content="no-cache">  
<meta http-equiv="Expires" content="0">  
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
				<a class="accordion-toggle">公司网点列表<i
					class="icon-refresh pull-right" onclick="refreshTree();"></i></a>
			</div>
			<div id="search" class="input-group" style="padding:10px 0 0 10px;">
				<input type="text" class="form-control input-sm empty" id="key" name="key" maxlength="50" placeholder="请输入关键字">
				<span class="input-group-btn">
					<button class="btn btn-default btn-sm" id="btn" onclick="searchNode()"><i class="glyphicon glyphicon-search"></i></button>
				</span>
			</div>
			<div class="ztree">
				<div id="ztree"></div>
				<div id="ztree2" style="display: none"></div>
			</div><!-- ztree id -->
			<input htmlEscape="false" id="id" class="input-xlar网点类型ge required" style="display: none" /> <input
				htmlEscape="false" id="name"
				class="input-xlarge required" style="display: none" />
				<input
				htmlEscape="false" id="status"
				class="input-xlarge required" style="display: none" />
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right">
			<iframe id="officeContent" width="100%"
				height="91%" frameborder="0"></iframe>
		</div>
	</div>
	<script type="text/javascript">
		var key, lastValue = "",  nodeList = [], nodeList2=[], type = getQueryString("type", "${url}");
		var tree;
		var tree2;
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
				//	alert(treeNode.id)
					/* if(treeNode.id=="0"){
						return;
					} */
				//alert(treeNode.id);
					if(treeId=="ztree"){
						tree2.cancelSelectedNode();
					}else if(treeId=="ztree2"){
						tree.cancelSelectedNode();
					}
					var id = treeNode.id == '0' ? '0' : treeNode.id;
					
					if(treeNode.status=='company'){
						
						$('#officeContent').attr(
								"src",
								"${ctx}/guard/staff/list?company.id=" + id);
					
					}else if(treeNode.status=='office'){
						$('#officeContent').attr(
								"src",
								"${ctx}/guard/staff/list?office.id=" + id);
					}else{
						$('#officeContent').attr(
								"src",
								"${ctx}/guard/staff/list?office.id=" + id);
					}
					$('#id').val(id);
					$('#name').val(treeNode.name);
					$('#status').val(treeNode.status);
				}
			}
		};

		function refreshTree() {
			$.getJSON("${ctx}/sys/office/treeData?time="+new Date(), function(data1) {
				tree =$.fn.zTree.init($("#ztree"), setting, data1);
				var nodeList = tree.getNodes();　　　　　　 //展开第一个根节点
				for(var i = 0; i < nodeList.length; i++) { //设置节点展开第二级节点
					tree.expandNode(nodeList[i], true, false, true);
					var nodespan = nodeList[i].children;
					for(var j = 0; j < nodespan.length; j++) { //设置节点展开第三级节点
						tree.expandNode(nodespan[j], true, false, true);
					}
				}
				//tree.expandAll(true);
				
				var nodeId;
				var nodePids;
				if(data1 && data1.length != 0){//默认选中节点
					var defaultSelectedNodeId= data1[0].id;
					var node = tree.getNodeByParam("id", defaultSelectedNodeId);
					var node_p=tree.getNodeByParam("id",data1[0].pId);
					tree.selectNode(node, true);
					tree.expandNode(node_p, false, false, true);
					nodeId = node.id;
					nodePids = node.pIds;
					$('#id').val(nodeId);
					$('#name').val(node.name);
					$('#status').val(node.status);
				}
				
				if(nodeId){
					$('#officeContent').attr("src", "${ctx}/guard/staff/list?office.id=" + nodeId);
				}
				$.getJSON("${ctx}/guard/company/treeData?isAll=false&time="+new Date(), function(data2) {
					tree2=$.fn.zTree.init($("#ztree2"), setting, data2);
					var nodeList = tree.getNodes();　　　　　　 //展开第一个根节点
					for(var i = 0; i < nodeList.length; i++) { //设置节点展开第二级节点
						tree2.expandNode(nodeList[i], true, false, true);
						var nodespan = nodeList[i].children;
						for(var j = 0; j < nodespan.length; j++) { //设置节点展开第三级节点
							tree2.expandNode(nodespan[j], true, false, true);
						}
					}
					//tree2.expandAll(true);

					if(!nodeId){
						var nodeId2;
						var nodePids2;
						if(data2 && data2.length != 0){//默认选中节点
							var defaultSelectedNodeId= data2[0].id;
							var node2 = tree2.getNodeByParam("id", defaultSelectedNodeId);
							tree2.selectNode(node, true);
							nodeId2 = node2.id;
							nodePids2 = node2.pIds;
							$('#id').val(nodeId2);
							$('#name').val(node2.name);
							$('#status').val(node2.status);
						}
						
						$('#officeContent').attr("src", "${ctx}/guard/staff/list?company.id=" + nodeId2);
						
					}
				});
				
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
			var nodes2 = tree2.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				showAllNode2(nodes2);
				return;
			}
			hideAllNode(nodes);
			hideAllNode2(nodes2);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			nodeList2 = tree2.getNodesByParamFuzzy(keyType, value);
			updateNodes(nodeList);
			updateNodes2(nodeList2);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
			}
		}
		//隐藏所有节点
		function hideAllNode2(nodes2){			
			nodes2 = tree2.transformToArray(nodes2);
			for(var i=nodes2.length-1; i>=0; i--) {
				tree2.hideNode(nodes2[i]);
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
		
		//显示所有节点
		function showAllNode2(nodes2){			
			nodes2 = tree2.transformToArray(nodes2);
			for(var i=nodes2.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes2[i].getParentNode()!=null){
						tree2.expandNode(nodes2[i],false,false,false,false);
					}else{
						tree2.expandNode(nodes2[i],true,true,false,false);
					}
					tree2.showNode(nodes2[i]);
					showAllNode(nodes2[i].children);
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
		
		//更新节点状态
		function updateNodes2(nodeList2) {
			tree2.showNodes(nodeList2);
			for(var i=0, l=nodeList2.length; i<l; i++) {
				//展开当前节点的父节点
				tree2.showNode(nodeList2[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList2[i].getParentNode()!=null){
					tree2.expandNode(nodeList2[i].getParentNode(), true, false, false);
					nodeList2[i] = nodeList2[i].getParentNode();
					tree2.showNode(nodeList2[i].getParentNode());
				}
				//显示根节点
				tree2.showNode(nodeList2[i].getParentNode());
				//展开根节点
				tree2.expandNode(nodeList2[i].getParentNode(), true, false, false);
			}
		}
		
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>