<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作日管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
	<style>
		.restDay{
			background-color: darkseagreen;
		}
		.selDel{
			background-color: crimson;
		}
		.sel{
			background-color: lightskyblue;
		}
	</style>
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
										"${ctx}/tbmj/workdayParaInfo/export");
								$("#searchForm").submit();
								$("#searchForm").attr("action",
										"${ctx}/tbmj/workdayParaInfo/list");
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


			$("#btnAdd").on('click',function(){
				var eId=${eId};
				if(eId!=null && eId!=""){
					layer.open({
						type: 2,
						title: '假期添加', //将姓名设置为红色
						shadeClose: true,           //弹出框之外的地方是否可以点击
						offset: 'auto',
						area: ['25%', '50%'],
						content: '${ctx}/tbmj/workdayParaInfo/plan1?eId=${eId}',
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
											"${ctx}/tbmj/workdayParaInfo/download");
									$("#searchForm").submit();
									$("#searchForm").attr("action",
											"${ctx}/tbmj/workdayParaInfo/list");
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
	<li class="active"><a href="${ctx}/tbmj/workdayParaInfo/">工作日列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="workdayParaInfo" action="${ctx}/tbmj/workdayParaInfo/" method="post" class="breadcrumb form-search">
	<input type="hidden" id="eId" name="eId" value="${eId}">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden"
		   value="${page.pageSize}" />
	<ul class="ul-form">
		<li><label>月份：</label>
			<select name="mon" style="width: 100px">
				<%--<option value="" <c:if test="${mon eq ''}">selected="selected"</c:if>>全部</option>--%>
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
		<shiro:hasPermission name="tbmj:workdayParaInfo:edit">
            <input id="btnAdd" class="btn btn-primary" type="button" value="假期添加" />
			<input id="delSel" class="btn btn-primary" type="button" value="删除选中" />
			<input id="addSel" class="btn btn-primary" type="button" value="添加选中" />
		</shiro:hasPermission>
	</ul>
</form:form>
<sys:message content="${message}"/>
<c:if test="${mrl!=null}">
	<table class="layui-table" style="width:70%;height: 60%;margin: 0px auto;text-align: center;" >
		<colgroup>
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
			<col width="10%">
		</colgroup>
		<thead>
		<tr>
			<th style="text-align: center">一</th>
			<th style="text-align: center">二</th>
			<th style="text-align: center">三</th>
			<th style="text-align: center">四</th>
			<th style="text-align: center">五</th>
			<th style="text-align: center">六</th>
			<th style="text-align: center">日</th>
		</tr>
		</thead>
		<tbody id="rl">

		</tbody>
	</table>
</c:if>


<script>
    $(function () {
        var rl=$("#rl").html('${mrl}');
		var selDays="";

        $(".enableSel").on('click',function () {
			if($(this).is('.sel')){
				$(this).removeClass("sel");
				return;
			}
			$(this).addClass("sel");
			/*if($(this).is('.selDel')){
				$(this).removeClass("selDel");
				return;
			}
			if($(this).is('.restDay')){
				$(this).addClass("selDel");
			}else if($(this).is('.workDay')){
				$(this).addClass("selAdd");
				//alert("该日期是工作日，无法删除！");
			}*/
		});

		$("#delSel").on('click',function() {
			$(".sel").each(function(){
				selDays+=$(this).text()+",";
				$(this).removeClass("sel");
			});
			if(selDays==""){
				alert("请先选择数据！");
			}else{
				window.location.href="${ctx}/tbmj/workdayParaInfo/delete?nums="+selDays+"&mon=${mon}&id=${id}";
			}
			selDays="";
		});

		$("#addSel").on('click',function() {
			$(".sel").each(function(){
				selDays+=$(this).text()+",";
				$(this).removeClass("sel");
			});
			if(selDays==""){
				alert("请先选择数据！");
			}else{
				window.location.href="${ctx}/tbmj/workdayParaInfo/save2?nums="+selDays+"&mon=${mon}&id=${id}";
			}
			selDays="";
		});
    });
</script>
</body>
</html>