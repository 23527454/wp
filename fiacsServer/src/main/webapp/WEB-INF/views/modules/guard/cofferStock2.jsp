<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱出入库信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
			});
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/coffer/list?lineId=${lineId}">款箱出入库信息</a></li>
	</ul>
	<br />
	<div class="row">
		<div class="col-xs-6">
			<h3>今日出库数量：${outCount }</h3>
			<table id="dg2"  class="table table-striped table-bordered table-condensed" >
							<thead>
								<tr>
									<th data-options="field:'boxCode'" width="25%">款箱编码</th>
									<th data-options="field:'cardNum'" width="25%">时间</th>
									<th data-options="field:'boxType'" width="25%">网点</th>
									<th data-options="field:'id'" width="25%">类型</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${allotList}" var="allot">
									<tr><td>${allot.moneyBox.boxCode}</td>
									<td><fmt:formatDate value="${allot.allotTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${allot.office.name}</td>
							<td>调拨</td></tr>
								</c:forEach>
								<c:forEach items="${orderList}" var="order">
									<tr><td>${order.moneyBox.boxCode}</td>
									<td><fmt:formatDate value="${order.allotReturnTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${order.office.name}</td><td>预约</td></tr>
								</c:forEach>
							</tbody>
			</table>
		</div>
		<div class="col-xs-6">
			<h3>今日入库数量：${inCount }</h3>
			<table id="dg3"  class="table table-striped table-bordered table-condensed" >
							<thead>
								<tr>
									<th data-options="field:'boxCode'" width="25%">款箱编码</th>
									<th data-options="field:'cardNum'" width="25%">时间</th>
									<th data-options="field:'boxType'" width="25%">网点</th>
									<th data-options="field:'id'" width="25%">类型</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${returnList}" var="re">
									<tr>
									<td>${re.moneyBox.boxCode}</td>
									<td><fmt:formatDate value="${re.returnTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td>${re.office.name}</td><td>上缴</td></tr>
								</c:forEach>
							</tbody>
			</table>
		</div>
	</div>
</div>
</body>
</html>