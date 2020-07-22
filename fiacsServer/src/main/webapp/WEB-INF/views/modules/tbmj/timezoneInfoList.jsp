<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>门禁时区管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery-session/jquery-session.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出门禁时区数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/tbmj/timezoneInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/tbmj/timezoneInfo/list");
							}
						}, {
							buttonsFocus : 1
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					});
			$("#btnDownload").click(
					function() {
						var accessParaInfoId=$("#accessParaInfoId").val();
						if(accessParaInfoId==null || accessParaInfoId==""){
							alert("请先选择一扇门!");
						}else{
						top.$.jBox.confirm("确认要同步该门禁所有时区吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/tbmj/timezoneInfo/download");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/tbmj/timezoneInfo/list");
							}
						}, {
							buttonsFocus : 1
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
						}

					});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

        /*function toAdd() {
			var id=$("#aId").val();
			var timeZoneNum=$("#timeZoneNum").val();
			alert(timeZoneNum);
			alert(id);
			window.location.href="${ctx}/tbmj/timezoneInfo/form3?accessParaInfoId="+id;
		}*/
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/tbmj/timezoneInfo/">门禁时区列表</a></li>
		<%--<li class="active"><a href="#" onclick="toAdd();">门禁时区添加</a></li>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="timezoneInfo" action="${ctx}/tbmj/timezoneInfo/" method="post" class="breadcrumb form-search">
		<input type="hidden" name="accessParaInfoId" id="accessParaInfoId" value="${accessParaInfoId}">
		<input type="hidden" name="equipmentId" id="equipmentId" value="${equipmentId}">
		<input type="hidden" name="doorPos" id="doorPos" value="${doorPos}">
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
			<li class="btns"><input id="btnDownload" class="btn btn-primary" type="button" value="同步" /></li>
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
				<shiro:hasPermission name="tbmj:timezoneInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>

		<c:if test="${timezone!=null}">
			<tr>
				<td>星期一</td>
				<td>${fn:substring(timezone.mon,0,5)}</td>
				<td>${fn:substring(timezone.mon,6,11)}</td>
				<td>${fn:substring(timezone.mon,12,17)}</td>
				<td>${fn:substring(timezone.mon,18,23)}</td>
				<td>${fn:substring(timezone.mon,24,29)}</td>
				<td>${fn:substring(timezone.mon,30,35)}</td>
				<td>${fn:substring(timezone.mon,36,41)}</td>
				<td>${fn:substring(timezone.mon,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=1">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期二</td>
				<td>${fn:substring(timezone.tue,0,5)}</td>
				<td>${fn:substring(timezone.tue,6,11)}</td>
				<td>${fn:substring(timezone.tue,12,17)}</td>
				<td>${fn:substring(timezone.tue,18,23)}</td>
				<td>${fn:substring(timezone.tue,24,29)}</td>
				<td>${fn:substring(timezone.tue,30,35)}</td>
				<td>${fn:substring(timezone.tue,36,41)}</td>
				<td>${fn:substring(timezone.tue,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=2">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期三</td>
				<td>${fn:substring(timezone.wed,0,5)}</td>
				<td>${fn:substring(timezone.wed,6,11)}</td>
				<td>${fn:substring(timezone.wed,12,17)}</td>
				<td>${fn:substring(timezone.wed,18,23)}</td>
				<td>${fn:substring(timezone.wed,24,29)}</td>
				<td>${fn:substring(timezone.wed,30,35)}</td>
				<td>${fn:substring(timezone.wed,36,41)}</td>
				<td>${fn:substring(timezone.wed,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=3">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期四</td>
				<td>${fn:substring(timezone.thu,0,5)}</td>
				<td>${fn:substring(timezone.thu,6,11)}</td>
				<td>${fn:substring(timezone.thu,12,17)}</td>
				<td>${fn:substring(timezone.thu,18,23)}</td>
				<td>${fn:substring(timezone.thu,24,29)}</td>
				<td>${fn:substring(timezone.thu,30,35)}</td>
				<td>${fn:substring(timezone.thu,36,41)}</td>
				<td>${fn:substring(timezone.thu,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=4">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期五</td>
				<td>${fn:substring(timezone.fri,0,5)}</td>
				<td>${fn:substring(timezone.fri,6,11)}</td>
				<td>${fn:substring(timezone.fri,12,17)}</td>
				<td>${fn:substring(timezone.fri,18,23)}</td>
				<td>${fn:substring(timezone.fri,24,29)}</td>
				<td>${fn:substring(timezone.fri,30,35)}</td>
				<td>${fn:substring(timezone.fri,36,41)}</td>
				<td>${fn:substring(timezone.fri,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=5">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期六</td>
				<td>${fn:substring(timezone.sat,0,5)}</td>
				<td>${fn:substring(timezone.sat,6,11)}</td>
				<td>${fn:substring(timezone.sat,12,17)}</td>
				<td>${fn:substring(timezone.sat,18,23)}</td>
				<td>${fn:substring(timezone.sat,24,29)}</td>
				<td>${fn:substring(timezone.sat,30,35)}</td>
				<td>${fn:substring(timezone.sat,36,41)}</td>
				<td>${fn:substring(timezone.sat,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=6">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
			<tr>
				<td>星期日</td>
				<td>${fn:substring(timezone.sun,0,5)}</td>
				<td>${fn:substring(timezone.sun,6,11)}</td>
				<td>${fn:substring(timezone.sun,12,17)}</td>
				<td>${fn:substring(timezone.sun,18,23)}</td>
				<td>${fn:substring(timezone.sun,24,29)}</td>
				<td>${fn:substring(timezone.sun,30,35)}</td>
				<td>${fn:substring(timezone.sun,36,41)}</td>
				<td>${fn:substring(timezone.sun,42,47)}</td>
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<td>
						<a href="${ctx}/tbmj/timezoneInfo/form?id=${timezone.id}&weekNum=7">修改</a>
					</td>
				</shiro:hasPermission>
			</tr>
		</c:if>


		<%--<c:forEach items="${list}" var="doorTimezone"  varStatus="varStatus">
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
				<shiro:hasPermission name="tbmj:timezoneInfo:edit"><td>
					<a href="${ctx}/tbmj/timezoneInfo/form?id=${doorTimezone.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>--%>

		</tbody>
	</table>
	<%--<div class="pagination">${page}</div>--%>
</body>
</html>