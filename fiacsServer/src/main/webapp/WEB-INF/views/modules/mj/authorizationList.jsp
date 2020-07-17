<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>授权信息管理</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
	<script type="text/javascript">
		$(document).ready(
				function() {
					var office_id = parent.$("#office_id").val();
					var office_type = parent.$("#office_type").val();
					var el = document.getElementById('add');
					el.setAttribute("href", "${ctx}/guard/equipment/form?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());

					var el2 = document.getElementById('door');
					el2.setAttribute("href", "${ctx}/guard/equipment/paramSetting?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());

					$("#door").click(
							function() {
								el.setAttribute("href", "${ctx}/guard/equipment/paramSetting?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());

							});

					$("#btnExport").click(
							function() {
								top.$.jBox.confirm("确认要导出设备数据吗？", "系统提示", function(
										v, h, f) {
									if (v == "ok") {
										$("#searchForm").attr("action",
												"${ctx}/guard/equipment/export");
										$("#searchForm").submit();
										$("#searchForm").attr("action",
												"${ctx}/guard/equipment/list");
									}
								}, {
									buttonsFocus : 1
								});
								top.$('.jbox-body .jbox-icon').css('top', '55px');
							});

					var message="${message}";
					if(message!='')
					{
						top.$.jBox.tip("${message}","",{persistent:true,opacity:0});$("#messageBox").show();
					}
				});
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action", "${ctx}/mj/authorization/list");
			$("#searchForm").submit();
			return false;
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/mj/authorization/">授权信息列表</a></li>
	<%--<shiro:hasPermission name="mj:authorization:edit">
		<li><a href="${ctx}/mj/authorization/form?accessParaInfoId=${accessParaInfoId}">授权信息添加</a></li>
	</shiro:hasPermission>--%>
</ul>
<form:form id="searchForm" modelAttribute="authorization"
		   action="${ctx}/mj/authorization/" method="post"
		   class="breadcrumb form-search form-inline">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />

	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<input type="hidden" id="accessParaInfoId" name="accessParaInfoId" value="${accessParaInfoId}"/>

	<div class="form-group">
		<label>姓名：</label>
		<input id="name" name="name" htmlEscape="false" maxlength="10" value="${name}"
					class="form-control input-sm" />&nbsp;&nbsp;
	</div>
	<div class="form-group">
		<label>工号：</label>
		<input id="workNum" name="workNum" htmlEscape="false" maxlength="10" value="${workNum}"
					class="form-control input-sm" />&nbsp;&nbsp;
	</div>

	<div class="form-group">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
		<shiro:hasPermission name="mj:authorization:edit">
			<input id="btnAdd" class="btn btn-primary" type="button" value="人员添加" />
		</shiro:hasPermission>
	</div>
</form:form>
<sys:message content="${message}" />
<table id="contentTable"
	   class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<th>姓名</th>
		<th>机构</th>
		<th>工号</th>
		<th>通行时区</th>
		<th>人员门禁分组</th>
		<th>允许查库</th>
		<shiro:hasPermission name="mj:authorization:edit"><th>操作</th></shiro:hasPermission>
	</tr>
	</thead>
	<tbody>
	<c:if test="${page.list!=null}">
		<c:forEach items="${page.list}" var="a" varStatus="varStatus">
			<tr>
				<td>
						${a.staff.name}
				</td>
				<td>
						${a.office.name}
				</td>
				<td>
						${a.staff.workNum}
				</td>
				<td>
						${a.timezoneInfoNum}
				</td>
				<td>
					<c:forEach items="${fns:getDictList('staff_group')}" var="type">
						<c:if test="${type.value eq a.staffGroup}">${type.label}</c:if>
					</c:forEach>
				</td>
				<td>
					<c:forEach items="${fns:getDictList('yes_no')}" var="type">
						<c:if test="${type.value eq a.checkPom}">${type.label}</c:if>
					</c:forEach>
				</td>
				<shiro:hasPermission name="mj:authorization:edit"><td>
					<a href="${ctx}/mj/authorization/form?id=${a.id}&accessParaInfoId=${accessParaInfoId}">修改</a>
					<a href="${ctx}/mj/authorization/delete?id=${a.id}">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</c:if>
	</tbody>
</table>
<div class="pagination">${page}</div>




<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>
	$("#btnAdd").click(function(){
		var accessPaaraInfoId=${accessParaInfoId};
		if(accessPaaraInfoId!=null && accessPaaraInfoId!=""){
			layer.open({
				type: 2,
				title: '员工选择', //将姓名设置为红色
				shadeClose: true,           //弹出框之外的地方是否可以点击
				offset: 'auto',
				area: ['60%', '85%'],
				content: '${ctx}/guard/staff/showAddPage?accessParaInfoId=${accessParaInfoId}'
				/*btn: ['确定'],
                btnAlign: 'l',
                yes: function(index){
                    //当点击‘确定’按钮的时候，获取弹出层返回的值
                    var res = window["layui-layer-iframe" + index].callbackdata();
                    //打印返回的值，看是否有我们想返回的值。
                    console.log(res);
                    $("#selStaffIds").val(res.selIds);
                    $("#selStaffNames").val(res.selNames);
                    //最后关闭弹出层
                    layer.close(index);
                }*/
			});
		}else{
			layer.open({
				title: '温馨提示'
				,content: '请先选择一个门！'
			});
		}

	});
</script>

</body>
</html>