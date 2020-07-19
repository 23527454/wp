<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作日管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
	<script src="${ctxStatic}/jquery-session/jquery-session.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(
					function() {
						top.$.jBox.confirm("确认要导出工作日数据吗？", "系统提示", function(
								v, h, f) {
							if (v == "ok") {
								$("#searchForm").attr("action",
										"${ctx}/mj/workdayParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/mj/workdayParaInfo/list");
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

		$(function() {
			$("#all").on('click',function() {
				$("input[name='restIndex']").prop("checked", this.checked);
			});

			$("input[name='restIndex']").on('click',function() {
				var $restIndex = $("input[name='restIndex']");
				$("#all").prop("checked" , $restIndex.length == $restIndex.filter(":checked").length ? true :false);
			}); 

			$("#delSel").on('click',function() {
				var pid_array = new Array();
				$('input[id="restIndex"]:checked').each(function(){
					pid_array.push($(this).val());//向数组中添加元素
				});
				var pids=pid_array.join(",");
				if(pids==""){
					layer.open({
						title: '温馨提示'
						,content: '请先选择一条数据！'
					});
				}else{
					window.location.href="${ctx}/mj/workdayParaInfo/delete?ids="+pids;
				}
			});

			$("#btnAdd").on('click',function(){
				var eId=${eId};
				if(eId!=null && eId!=""){
					layer.open({
						type: 2,
						title: '假期添加', //将姓名设置为红色
						shadeClose: true,           //弹出框之外的地方是否可以点击
						offset: 'auto',
						area: ['25%', '50%'],
						content: '${ctx}/mj/workdayParaInfo/plan1?eId=${eId}',
					});
				}else{
					layer.open({
						title: '温馨提示'
						,content: '请先选择一个设备！'
					});
				}
			});
			$("#btnDownload").click(
					function() {
						var eId=$("#eId").val();
						if(eId==null || eId==""){
							alert("请先选择一个设备!");
						}else{
							top.$.jBox.confirm("确认要同步该设备所有假日吗？", "系统提示", function(
									v, h, f) {
								if (v == "ok") {
									$("#searchForm").attr("action",
											"${ctx}/mj/workdayParaInfo/download");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
											"${ctx}/mj/workdayParaInfo/list");
								}
							}, {
								buttonsFocus : 1
							});
							top.$('.jbox-body .jbox-icon').css('top', '55px');
						}

					});
		});



	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/mj/workdayParaInfo/">工作日列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="workdayParaInfo" action="${ctx}/mj/workdayParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" id="eId" name="eId" value="${eId}">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<ul class="ul-form">
		<li><label>月份：</label>
			<select name="mon" style="width: 100px">
				<option value="" <c:if test="${mon eq ''}">selected="selected"</c:if>>全部</option>
				<option value="1" <c:if test="${mon eq '1'}">selected="selected"</c:if>>1月</option>
				<option value="2" <c:if test="${mon eq '2'}">selected="selected"</c:if>>2月</option>
				<option value="3" <c:if test="${mon eq '3'}">selected="selected"</c:if>>3月</option>
				<option value="4" <c:if test="${mon eq '4'}">selected="selected"</c:if>>4月</option>
				<option value="5" <c:if test="${mon eq '5'}">selected="selected"</c:if>>5月</option>
				<option value="6" <c:if test="${mon eq '6'}">selected="selected"</c:if>>6月</option>
				<option value="7" <c:if test="${mon eq '7'}">selected="selected"</c:if>>7月</option>
				<option value="8" <c:if test="${mon eq '8'}">selected="selected"</c:if>>8月</option>
				<option value="9" <c:if test="${mon eq '9'}">selected="selected"</c:if>>9月</option>
				<option value="10" <c:if test="${mon eq '10'}">selected="selected"</c:if>>10月</option>
				<option value="11" <c:if test="${mon eq '11'}">selected="selected"</c:if>>11月</option>
				<option value="12" <c:if test="${mon eq '12'}">selected="selected"</c:if>>12月</option>
			</select>
		</li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="btns"><input id="btnDownload" class="btn btn-primary" type="button" value="同步"/></li>
		<%--<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>--%>
		<shiro:hasPermission name="mj:workdayParaInfo:edit">
            <input id="btnAdd" class="btn btn-primary" type="button" value="假期添加" />
            <input id="delSel" class="btn btn-primary" type="button" value="删除选中" />
		</shiro:hasPermission>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
        <th style="width: 10%"><input type="checkbox" id="all" />全选</th>
		<th>序号</th>
		<th>日期</th>
		<shiro:hasPermission name="mj:workdayParaInfo:edit"><th>操作</th></shiro:hasPermission>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${page.list}" var="day"  varStatus="varStatus">
		<tr>
            <td><input type="checkbox"id="restIndex" name="restIndex" value="${day.id}-${day.restIndex}" /></td>
			<td>
					${varStatus.count}
			</td>
			<td>
					${day.date}
			</td>
			<shiro:hasPermission name="mj:workdayParaInfo:edit"><td>
				<a href="${ctx}/mj/workdayParaInfo/delete?ids=${day.id}-${day.restIndex}">删除</a>
			</td></shiro:hasPermission>
		</tr>

	</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>