<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>款箱管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/tbmoneylockers/">款箱列表</a></li>
		<shiro:hasPermission name="guard:tbmoneylockers:edit"><li><a href="${ctx}/guard/tbmoneylockers/form">款箱添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tbmoneylockers" action="${ctx}/guard/tbmoneylockers/" method="post" class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

			<div class="form-group"><label>id：</label>
				<form:input path="id" htmlEscape="false" class="form-control"/>
			</div>
			<div class="form-group"><label>款箱卡号：</label>
				<form:input path="szcardnumber" htmlEscape="false" maxlength="12" class="form-control"/>
			</div>
			<div class="form-group"><label>款箱编码：</label>
				<form:input path="szlockercode" htmlEscape="false" maxlength="16" class="form-control"/>
			</div>
			<div class="form-group"><label>关联网点：</label>
				<sys:treeselect id="area" name="area.id" value="${tbmoneylockers.area.id}" labelName="area.name" labelValue="${tbmoneylockers.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</div>
			<div class="form-group"><label>款箱类型：</label>
				<form:select path="uclockertype" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ulocker_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>款箱状态：</label>
				<form:select path="uclockerstatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('clocker_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>款箱序号</th>
				<th>款箱卡号</th>
				<th>款箱编码</th>
				<th>关联网点</th>
				<th>款箱类型</th>
				<th>款箱状态</th>
				<th>更新日期</th>
				<th>备注</th>
				<shiro:hasPermission name="guard:tbmoneylockers:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tbmoneylockers">
			<tr>
				<td><a href="${ctx}/guard/tbmoneylockers/form?id=${tbmoneylockers.id}">
					${tbmoneylockers.id}
				</a></td>
				<td>
					${tbmoneylockers.nlockersn}
				</td>
				<td>
					${tbmoneylockers.szcardnumber}
				</td>
				<td>
					${tbmoneylockers.szlockercode}
				</td>
				<td>
					${tbmoneylockers.area.name}
				</td>
				<td>
					${fns:getDictLabel(tbmoneylockers.uclockertype, 'ulocker_type', '')}
				</td>
				<td>
					${fns:getDictLabel(tbmoneylockers.uclockerstatus, 'clocker_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${tbmoneylockers.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tbmoneylockers.remarks}
				</td>
				<shiro:hasPermission name="guard:tbmoneylockers:edit"><td>
    				<a href="${ctx}/guard/tbmoneylockers/form?id=${tbmoneylockers.id}">修改</a>
					<a href="${ctx}/guard/tbmoneylockers/delete?id=${tbmoneylockers.id}" onclick="return confirmx('确认要删除该款箱吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>