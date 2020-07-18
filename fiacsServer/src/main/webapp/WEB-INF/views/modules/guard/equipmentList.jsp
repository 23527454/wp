<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>设备信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				var office_id = parent.$("#office_id").val();
				var office_type = parent.$("#office_type").val();
				var el = document.getElementById('add');
				el.setAttribute("href", "${ctx}/guard/equipment/form?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());
				
				$("#add").click(
						function() {
							el.setAttribute("href", "${ctx}/guard/equipment/form?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());
							
						});
				
				/*var el2 = document.getElementById('door');
				el2.setAttribute("href", "${ctx}/guard/equipment/paramSetting?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());*/
				
				$("#door").click(
						function() {
							el.setAttribute("href", "${ctx}/guard/equipment/paramSetting?equ_office_id="+office_id+"&equ_office_type="+office_type+"&r="+Math.random());
							
						});
				
				$("#btnExport").click(
						function() {
							alert(1);
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
		$("#searchForm").attr("action", "${ctx}/guard/equipment/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/equipment/">设备信息列表</a></li>
		<shiro:hasPermission name="guard:equipment:edit">
			<li><a href="${ctx}/guard/equipment/form" id="add">设备信息添加</a></li>
		</shiro:hasPermission>
		<%--<shiro:hasPermission name="guard:equipment:edit">
			<li><a href="${ctx}/guard/equipment/form" id="door">门禁参数设置</a></li>
		</shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="equipment"
		action="${ctx}/guard/equipment/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		
		<c:if test="${equipment.office != null and equipment.office.id != null}">
			<input name="office.id" type="hidden"value="${equipment.office.id}" />
		</c:if>
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		
		<div class="form-group">
			<label>IP地址：</label>
			<form:input path="ip" htmlEscape="false" maxlength="10"
				class="form-control input-sm" />&nbsp;&nbsp;
		</div>
		<div class="form-group">
			<label >网点类型：</label>
			<form:select path="siteType" cssClass="form-control input-sm">
				<form:option value="" label="所有类型" />
				<form:options items="${fns:getDictList('site_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>网点名称</th>
				<th>IP地址</th>
				<th>端口号</th>
				<th>门控类型</th>
				<th>序列号</th>
				<th>子网掩码</th>
				<th>网关</th>
				<th>中心上传IP地址</th>
				<th>中心端口</th>
				<th>打印服务器IP</th>
				<th>打印服务器端口</th>
				<shiro:hasPermission name="guard:equipment:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="equipment"  varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a href="${ctx}/guard/equipment/form?id=${equipment.id}">
							${equipment.office.name}</a></td>
					<td>${equipment.ip}</td>
					<td>${equipment.port}</td>
					<%-- <td>${fns:getDictLabel(equipment.siteType, 'site_type', '')}</td> --%>
					<!-- 0：一级支行；1：一级分行；2：中心金库, 3:营业网点, 4:子营业网点 -->
					<c:if test="${equipment.siteType==0}">
						<td>金库门</td>
					</c:if>
					<c:if test="${equipment.siteType==1}">
						<td>联动门</td>
					</c:if>
					<c:if test="${equipment.siteType==2}">
						<td>防护隔离门</td>
					</c:if>
					<%--<c:if test="${equipment.office.type==4}">
					<td>子营业网点</td>
					</c:if>
					<c:if test="${equipment.office.type==3}">
					<td>营业网点</td>
					</c:if>
					<c:if test="${equipment.office.type==2}">
					<td>中心金库</td>
					</c:if>
					<c:if test="${equipment.office.type==1}">
					<td>一级支行</td>
					</c:if>--%>

					<td>${equipment.serialNum}</td>
					<td>${equipment.netMask}</td>
					<td>${equipment.netGate}</td>

					<td>${equipment.uploadEventSrvIp}</td>
					<td>${equipment.uploadEventSrvPort}</td>

					<td>${equipment.printServerIp}</td>
					<td>${equipment.printServerPort}</td>
					<shiro:hasPermission name="guard:equipment:edit">
						<td><a href="${ctx}/guard/equipment/form?id=${equipment.id}">修改</a>
							<a href="${ctx}/guard/equipment/delete?id=${equipment.id}"
							onclick="return confirmx('确认要删除该设备信息吗？', this.href)">删除</a> <a
							href="${ctx}/guard/equipment/download?id=${equipment.id}"
							onclick="return confirmx('是否同步该设备', this.href)">同步</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>