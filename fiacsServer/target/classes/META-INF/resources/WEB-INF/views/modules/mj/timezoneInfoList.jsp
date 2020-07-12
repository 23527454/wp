<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>门禁时区管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出门禁时区数据吗？", "系统提示", function(
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
		<li class="active"><a href="${ctx}/mj/timezoneInfo/">门禁时区列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="timezoneInfo" action="${ctx}/mj/timezoneInfo/" method="post" class="breadcrumb form-search">
		<form:hidden path="accessParaInfo.id" ></form:hidden>
		<ul class="ul-form">
		<li><label>时区类型：</label>
			<form:select path="timeZoneType" cssClass="form-control input-sm">
				<form:options items="${fns:getDictList('time_zone_type')}"
							  itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</li>
		<li><label>时区号：</label>
			<form:select path="timeZoneNum" cssClass="form-control input-sm">
				<form:options items="${fns:getDictList('time_zone_num')}"
							  itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
	</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>星期</th>
				<th>时段一开始</th>
				<th>时段一结束</th>
				<th>时段二开始</th>
				<th>时段二结束</th>
				<th>时段三开始</th>
				<th>时段三结束</th>
				<th>时段四开始</th>
				<th>时段四结束</th>
				<shiro:hasPermission name="mj:timezoneInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="doorTimezone"  varStatus="varStatus">
			<tr>
				<td>
					<c:forEach items="${fns:getDictList('week_number')}" var="week">
						<c:if test="${week.value eq doorTimezone.weekNumber}">${week.label}</c:if>
					</c:forEach>
				</td>
				<td>
						${doorTimezone.timeStart1}
				</td>
				<td>
						${doorTimezone.timeEnd1}
				</td>
				<td>
						${doorTimezone.timeStart2}
				</td>
				<td>
						${doorTimezone.timeEnd2}
				</td>
				<td>
						${doorTimezone.timeStart3}
				</td>
				<td>
						${doorTimezone.timeEnd3}
				</td>
				<td>
						${doorTimezone.timeStart4}
				</td>
				<td>
						${doorTimezone.timeEnd4}
				</td>
				<shiro:hasPermission name="mj:timezoneInfo:edit"><td>
					<a href="${ctx}/mj/timezoneInfo/form?id=${doorTimezone.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		<%--<c:if test="${accessParaInfo!=null}">
			<tr>
				<td>
						${accessParaInfo.doorRelayTime}
				</td>
				<td>
						${accessParaInfo.doorDelayTime}
				</td>
				<td>
						${accessParaInfo.enterOperaTime}
				</td>
				<td>
						${accessParaInfo.checkOperaTime}
				</td>
				<td>
						${accessParaInfo.authType == 1?'指纹':accessParaInfo.authType == 2?'人脸':accessParaInfo.authType == 3?'密码':accessParaInfo.authType == 4?'人脸+指纹':accessParaInfo.authType == 5?'指纹+密码':'人脸或指纹或密码'}
				</td>
				<shiro:hasPermission name="mj:timezoneInfo:edit"><td>
					<a href="${ctx}/mj/timezoneInfo/form?id=${timezoneInfo.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:if>--%>

		<%--<c:forEach items="${list}" var="accessParaInfo">
			<tr>
				<td><a href="${ctx}/mj/accessParaInfo/form?id=${accessParaInfo.id}">
					${accessParaInfo.doorPos eq '1'?'1号门':accessParaInfo.doorPos eq '2'?'2号门':accessParaInfo.doorPos eq '3'?'3号门':'4号门'}
				</a></td>
				<td>
					${accessParaInfo.doorRelayTime}
				</td>
				<td>
					${accessParaInfo.doorDelayTime}
				</td>
				<td>
					${accessParaInfo.enterOperaTime}
				</td>
				<td>
						${accessParaInfo.checkOperaTime}
				</td>
				<td>
						${accessParaInfo.authType == 1?'指纹':accessParaInfo.authType == 2?'人脸':accessParaInfo.authType == 3?'密码':accessParaInfo.authType == 4?'人脸+指纹':accessParaInfo.authType == 5?'指纹+密码':'人脸或指纹或密码'}
				</td>
				<shiro:hasPermission name="mj:accessParaInfo:edit"><td>
    				<a href="${ctx}/mj/accessParaInfo/form?id=${accessParaInfo.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>--%>
		</tbody>
	</table>
	<%--<div class="pagination">${page}</div>--%>
</body>
</html>