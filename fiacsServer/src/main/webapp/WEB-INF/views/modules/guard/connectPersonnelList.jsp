<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员交接明细</title>
<meta name="decorator" content="default" />
<style>
@media print {
	a[href]:after {
		content: none !important;
	}
}
</style>
<script type="text/javascript">
	$(document).ready(function() {

	});

	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function getRadio() {
		e = event.srcElement;
		if (e.type == "radio" && e.name == "tRadio") {
			var myDiv = document.getElementById("lab");
			var mytext = document.getElementById("text");
			if (e.value == 1) {
				myDiv.innerText = "姓名";
				$("#text").val(1);
			} else if (e.value == 2) {
				myDiv.innerText = "工号";
				$("#text").val(2);
			} else if (e.value == 3) {
				myDiv.innerText = "指纹号";
				$("#text").val(3);
			}
		}
	}

	function preview(oper) {
		if (oper < 10) {
			var headstr = "<html><head><title>交接事件列表</title></head><body>";
			var footstr = "</body>";
			bdhtml = window.document.body.innerHTML;//获取当前页的html代码 
			sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域 
			eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域 
			prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html 
			prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html 
			window.document.body.innerHTML = headstr + prnhtml + footstr;
			window.print();
			window.document.body.innerHTML = bdhtml;
		} else {
			window.print();
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/connectPersonnel/">人员交接明细列表</a></li>
		<shiro:hasPermission name="guard:connectPersonnel:edit">
			<li><a href="${ctx}/guard/connectPersonnel/form">人员交接明细添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="connectPersonnel"
		action="${ctx}/guard/connectPersonnel/" method="post"
		class="breadcrumb form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<span onclick="getRadio();"> <input name="tRadio" type="radio"
				value="1" checked="checked" />姓名 <input name="tRadio" type="radio"
				value="2" />工号 <input name="tRadio" type="radio" value="3" />指纹号
			</span>
		</div>
		<form:input path="text" htmlEscape="false" class="form-control"
			id="text" value="${connectPersonnel.text}" style="display:none;" />
		<div class="form-group">
			<label id="lab">姓名</label>
			<form:input path="textS" htmlEscape="false" class="form-control" />
		</div>
		<div class="form-group">
			<label>区域：</label>
			<sys:treeselect id="area" name="area.id"
				value="${connectPersonnel.area.id}" labelName="area.name"
				labelValue="${connectPersonnel.area.name}" title="区域"
				url="/sys/area/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${connectPersonnel.office.id}" labelName="office.name"
				labelValue="${connectPersonnel.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="input-small" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>任务类型：</label>
			<form:select path="taskType" class="input-medium">
				<form:option value="" label="所有类型" />
				<form:options items="${fns:getDictList('task_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="time" type="text"
				readonly="readonly" maxlength="20" class="form-control Wdate"
				value="${connectPersonnel.time}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="timeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control Wdate"
				value="${connectPersonnel.time}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>排序类型：</label>
			<form:select path="sortType" class="input-medium">
				<form:option value="1" label="网点" />
				<form:option value="2" label="时间" />
				<form:option value="3" label="押款员" />
				<form:option value="4" label="任务类型" />
				<form:option value="5" label="班组" />
				<form:option value="6" label="班次" />
				<form:option value="7" label="区域" />
			</form:select>
		</div>
		<div class="form-group">
			<label><input name="sort" type="checkbox"
				value="${connectPersonnel.sort}"
				onclick="this.value=this.checked?1:-1" />升序 </label>
		</div>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		<input type="button" class="btn btn-primary" name="Button" value="打印"
			onclick="preview(1)">

	</form:form>
	<sys:message content="${message}" />
	<!--startprint1-->
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>网点</th>
				<th>时间</th>
				<th>押款员</th>
				<th>任务类型</th>
				<th>班组</th>
				<th>班次</th>
				<th>区域</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="connectPersonnel">
				<tr>
					<td><a
						href="${ctx}/guard/connectPersonnel/form?id=${connectPersonnel.id}&staff_id=${connectPersonnel.staff.id}">
							${connectPersonnel.office.name} </a></td>
					<td>${connectPersonnel.time}</td>
					<td>${connectPersonnel.staff.name}</td>
					<td>${fns:getDictLabel(connectPersonnel.taskType, 'task_type', '')}
					</td>
					<td>${connectPersonnel.classTaskfo.name}</td>
					<td>${connectPersonnel.classTaskfo.id}</td>
					<td>${connectPersonnel.area.id}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--endprint1-->
	<div class="pagination">${page}</div>
</body>
</html>