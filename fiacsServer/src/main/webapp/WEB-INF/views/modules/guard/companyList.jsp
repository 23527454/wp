<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>第三方公司管理</title>
<meta name="decorator" content="default" />
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
		
		var status=$("#status").val();
		if(status!="" && status!=null && typeof (status) != "undefined"  ){
		parent.refreshTree();
		}
		var company_id = parent.$("#company_id").val();
		var company_name = parent.$("#company_name").val();
		if(company_id!="" && typeof (company_id) != "undefined" && company_id!=0){
		var el = document.getElementById('add');
		el.setAttribute("href", "${ctx}/guard/company/form?parent.id="+company_id);
		}
		
		var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data = ${fns:toJson(list)}, rootId = "${not empty company.id ? company.id : '0'}";
		addRow("#treeTableList", tpl, data, rootId, true);
		$("#treeTable").treeTable({expandLevel : 5});
	});
	function addRow(list, tpl, data, pid, root){
		for (var i=0; i<data.length; i++){
			var row = data[i];
			if ((${fns:jsGetVal('row.parentId')}) == pid){
				$(list).append(Mustache.render(tpl, {
					dict: {
						type: getDictLabel(${fns:toJson(fns:getDictList('company_type'))}, row.companyType)
					}, pid: (root?0:pid), row: row
				}));
				addRow(list, tpl, data, row.id);
			}
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/company/">第三方公司列表</a></li>
		<shiro:hasPermission name="guard:company:edit">
			<li><a href="${ctx}/guard/company/form" id="add">第三方公司添加</a></li>
		</shiro:hasPermission>
	</ul>
	<sys:message content="${message}" />
	<input type="hidden" value="${status}" id="status">
	<table id="treeTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>公司名称</th>
				<th>所属区域</th>
				<th>公司编码</th>
				<th>公司类型</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:area:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/guard/company/form?id={{row.id}}">{{row.shortName}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.companyCode}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="guard:company:edit"><td>
				<a href="${ctx}/guard/company/form?id={{row.id}}">修改</a>
				<a href="${ctx}/guard/company/delete?id={{row.id}}" onclick="return confirmx('要删除该公司及所有子区域项吗？', this.href)">删除</a>
				<a href="${ctx}/guard/company/form?parent.id={{row.id}}">添加下级公司</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>