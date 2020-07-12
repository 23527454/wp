<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱出入库信息</title>
<meta name="decorator" content="default" />
<link
	href="${ctxStatic}/jquery-easyui-1.4.2/themes/bootstrap/easyui.css"
	rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js'></script>
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/jquery.easyui.min.js'></script>
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
	
	function closeLoadingDiv() {  
        $("#loadingDiv").fadeOut("normal", function () {  
            $(this).remove();  
        });  
    }  
    var no;  
    $.parser.onComplete = function () {  
        if (no) clearTimeout(no);  
        no = setTimeout(closeLoadingDiv, 1000);  
    }  
    
    function setLineMessage() {
		// 设置线路信息
		var rows = $('#dgthere').datagrid('getSelections');
		if (rows.length == 1) {
			//编号
			var id = rows[0].id;
			//线路名称
			var name = rows[0].lineName;
			$("#lineMessage").val(name);
			$("#line").val(id);

			$('#myModalThere').modal("hide");
		} else {
			alert("只能选择一条线路");
		}
	}
    
    function myModalThere() {
		// 获取线路列表
		$('#myModalThere').modal({
			keyboard : true
		});
		$('#myModalThere').one('shown.bs.modal', function() {
			var dgtwo;
			dgtwo = $('#dgthere').datagrid({
				url : "${ctx}/guard/line/listData", // JSON数据路径
				loadMsg : "正在努力加载数据，请稍后...", // 遮罩层
				async : true,
				fit : false,// 自动大小
				fitColumns : false, // 是否固定列
				singleSelect : true,// 是否选择多行
				striped : true, // True 就把行条纹化。（即奇偶行使用不同背景色）
				nowrap : false, // 文字满后自动换行
				multiSort : false,
				idField : 'id',
				fitColumns : false,
				remoteSort : false,// 定义是否从服务器给数据排序
				rownumbers : false, // 行号
				pagination : true, // 分页显示
				onBeforeLoad : function(param) {
					param.pageNo = param.page;
					param.pageSize = param.rows;
					delete (param.page);
					delete (param.rows);
				},
				loadFilter : function(data) {
					return {
						total : data.count,
						rows : data.list
					};
				},
				pageSize : 20,// 每页显示的记录条数，默认为10
				pageList : [ 10, 20, 30, 40, 50 ],// 可以设置每页记录条数的列表
				enableFilter : true
			});
			var p = dgtwo.datagrid('getPager');
			$(p).pagination({
				pageSize : 20,
				beforePageText : '第',// 页数文本框前显示的汉字
				afterPageText : '页    共 {pages} 页',
				buttons : "#footer",
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
		});
	}
</script>
</head>
<body>
<div id='loadingDiv' style="position: absolute; z-index: 1000; top: 0px; left: 0px;   width: 100%; height: 100%; background: white; text-align: center;">  
	    <h1 style="top: 48%; position: relative;">  
	        <font color="#15428B">努力加载中···</font>  
	    </h1>  
	</div>  
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">款箱出入库信息</a></li>
	</ul>
	<br />
	<form id="searchForm"
		action="${ctx}/guard/coffer/summary" method="post"
		class="breadcrumb form-search form-inline">
		<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>线路名称：</label>
					<div class="col-xs-7">
						<span class="input-group" style="display: inline-table;vertical-align: middle;">
							<input name="line.lineName" htmlEscape="false"
							onclick="myModalThere();" id="lineMessage" readonly="true"
							class="form-control input-sm required error-input-sub" />
							<span class="input-group-btn">
							   <ahref="javascript:void(0)" onclick="myModalThere()" class="btn btn-default btn-sm " style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
							</span>
						</span>
					</div>
				</div>
		 <div class="form-group">
			<input id="line" name="lineId" style="display:none;">
		</div>
        <div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>
	</form>
	<div class="row">
		<div class="col-xs-12">
			<table id="dg2"  class="table table-striped table-bordered table-condensed" >
							<thead>
								<tr>
									<th data-options="field:'linename'" width="30%">线路名称</th>
									<th data-options="field:'areaname'" width="30%">所属区域</th>
									<th data-options="field:'outplan'" width="10%">计划出库</th>
									<th data-options="field:'outbox'" width="10%">实际出库</th>
									<th data-options="field:'inplan'" width="10%">计划入库</th>
									 <th data-options="field:'inbox'" width="10%">实际入库</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${data}" var="data">
									<tr>
									<td>${data.line.lineName}</td>
									<td>${data.line.area.name}</td>
								<td>${fn:length(data.allot)+fn:length(data.order)}</td>
							
								<td>${fn:length(data.outBox)}</td>
								<td>${fn:length(data.returnBox)}</td>
								<td>${fn:length(data.inBox)}</td>
							</tr>
								</c:forEach>
							</tbody>
			</table>
		</div>
	</div>
	<div class="modal fade" id="myModalThere" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">线路信息选择</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 800px;">
					<form id="dgFormthere" enctype="multipart/form-data"
						style="width: 600px;">
						<!--车辆列表-->
						<table id="dgthere" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'id'" sortable="true" width="15%">编号</th>
									<th data-options="field:'lineName'" sortable="true" width="80%">线路名称</th>
								</tr>
							</thead>
						</table>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" name="filesubmit"
								onclick="setLineMessage();" value="确定">确定</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>