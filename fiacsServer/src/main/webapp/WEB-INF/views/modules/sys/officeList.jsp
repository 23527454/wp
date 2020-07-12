<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@ page isELIgnored="false"%>
<script type="text/javascript">
		$(document).ready(function() {
			var office_id;
			var office_name;
			
			var officeTree = parent.tree;
			var nodes = officeTree.getSelectedNodes();
			if(nodes){
				office_id = nodes[0].id;
				office_name= nodes[0].name;
			}
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = 0;
			
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if(row.parentId == '0'){//跟节点不能删除
					row.deletable = true;
				}
				if(row.type== '4' || row.type =='2'){//子营业网点，中心金库是leaf
					row.leaf = true;
				}
				if (row.id == office_id){
					$("#treeTableList").append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
						}, pid: rootId, row: row
					}));
				}
			}
			addRow("#treeTableList", tpl, data, office_id, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if (row.parentId == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
						}, pid: pid, row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
		
		function deleteOffice(id, selectedOfficeId){
			confirmx('要删除该机构及所有子机构项吗？', function(){
				$.ajax({
					type : "GET",
					url : "${ctx}/sys/office/delete?id="+id+"&selectedOfficeId="+selectedOfficeId
				}).done(function(res) {
					var data = JSON.parse(res);
					if(data.status == 'SUCCESS'){
						if(id == selectedOfficeId){
							parent.refreshTree();
						}else{
							parent.refreshTree(selectedOfficeId);
// 							location = "${ctx}/sys/office/list?id="+selectedOfficeId;
						}
					}else{
						 top.$.jBox.tip(data.message, 'error');
					}
				}).fail(function() {
				});
			});
		}
	</script>
</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/sys/office/list">网点列表</a></li>
		<shiro:hasPermission name="sys:office:edit">
			<c:if test="${office.type!= '4' && office.type !='2' }">
				<li><a href="${ctx}/sys/office/form?parent.id=${office.id}&selectedOfficeId=${selectedOfficeId}"
					id="add">网点添加</a></li>
			</c:if>
		</shiro:hasPermission>
	</ul>
	<sys:message content="${message}" />
	<table id="treeTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>网点名称</th>
				<th>归属区域</th>
				<th>网点编码</th>
				<th>网点类型</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:office:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/office/form?id={{row.id}}&selectedOfficeId=${selectedOfficeId}">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:office:edit"><td>
				<a href="${ctx}/sys/office/form?id={{row.id}}&selectedOfficeId=${selectedOfficeId}">修改</a>
				{{^row.deletable}}
				<a href="javascript:" onclick="deleteOffice('{{row.id}}', '${selectedOfficeId}')">删除</a>
				{{/row.deletable}}
                {{^row.leaf}}
					<a href="${ctx}/sys/office/form?parent.id={{row.id}}&selectedOfficeId=${selectedOfficeId}" id="a{{row.id}}">添加下级机构</a>
				{{/row.leaf}} 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>