<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>班组信息管理</title>
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
				var taskType = ${fns:toJson(classTaskInfo.tasktype)};
				var dataCar = ${fns:toJson(classTaskInfo.classCarInfoList)};
				for(var i=0;i<dataCar.length;i++){
					var $table = $("#tzgjz_tab tr");
					var len = $table.length;
					$("#tzgjz_tab") //车辆信息
							.append(
									"<tr id=classCarInfoList"
											+ (len - 1)
											+ "><td align=\'center\'>"
											+ "<input align=\'center\' id=\'classCarInfoList["
											+ (len - 1)
											+ "].carId\' name=\'classCarInfoList["
											+ (len - 1)
											+ "].carId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].carId
											+ "\'/>"
											+  "<div style='width:30px;'>"+(i+1)+"</div>"
											+ "<input align=\'center\' id=\'classCarInfoList["
											+ (len - 1)
											+ "].delete\' name=\'classCarInfoList["
											+ (len - 1)
											+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
											+ 0
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'classCarInfoList["
											+ (len - 1)
											+ "].name\' name=\'classCarInfoList["
											+ (len - 1)
											+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].name
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'classCarInfoList["
											+ (len - 1)
											+ "].carplate\' name=\'classCarInfoList["
											+ (len - 1)
											+ "].carplate\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].carplate
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'classCarInfoList["
											+ (len - 1)
											+ "].cardNum\' name=\'classCarInfoList["
											+ (len - 1)
											+ "].cardNum\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].cardNum + "\'/>" + "</td></tr>");
				}
				
				var dataPerson= ${fns:toJson(classTaskInfo.classPersonInfoList)};
				for(var n=0;n<dataPerson.length;n++){
					var $table = $("#tzgjz_tabTwo tr");
					var len = $table.length;
					$("#tzgjz_tabTwo")
							.append(
									"<tr id=classPersonInfoList"
											+ (len - 1)
											+ "><td align=\'center\'>"
											+ "<input align=\'center\' id=\'classPersonInfoList["
											+ (len - 1)
											+ "].personId\' name=\'classPersonInfoList["
											+ (len - 1)
											+ "].personId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataPerson[n].personId
											+ "\'/>"
											+  "<div style='width:30px;'>"+(n+1)+"</div>"
											+ "<input align=\'center\' id=\'classPersonInfoList["
											+ (len - 1)
											+ "].delete\' name=\'classPersonInfoList["
											+ (len - 1)
											+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
											+ 0
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'classPersonInfoList["
											+ (len - 1)
											+ "].name\' name=\'classPersonInfoList["
											+ (len - 1)
											+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataPerson[n].name
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'classPersonInfoList["
											+ (len - 1)
											+ "].fingerNum\' name=\'classPersonInfoList["
											+ (len - 1)
											+ "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataPerson[n].fingerNum
											+ "\'/>"
											+  dataPerson[n].fingerNumLabel
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'classPersonInfoList["
											+ (len - 1)
											+ "].identifyNumber\' name=\'classPersonInfoList["
											+ (len - 1)
											+ "].identifyNumber\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataPerson[n].identifyNumber + "\'/>"
											+ "</td></tr>");
				}
				
				var bzname = $("#bzName").val();
				if (bzname == "" || typeof(bzname) == "undefined") {
					$("#lineMessage").val("");
				}
				
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									}
								});
			});

	function myModal() {
		// 获取车辆列表
		$('#myModal').modal({
			keyboard : true
		});

		$('#myModal').one('shown.bs.modal', function() {
			var classTaskid=$("#classTaskid").val();
			//var areaid=$("#areaid").val();
			var areaid=$("#area_id").val();
			//return;
			var dg;
			dg = $('#dg').datagrid({
				url : "${ctx}/guard/car/listData?classTaskId="+classTaskid+"&area.id="+areaid, // JSON数据路径
				loadMsg : "正在努力加载数据，请稍后...", // 遮罩层
				async : true,
				fit : false,// 自动大小
				fitColumns : false, // 是否固定列
				singleSelect : false,// 是否选择多行
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
			var p = dg.datagrid('getPager');
			$(p).pagination({
				pageSize : 20,
				beforePageText : '第',// 页数文本框前显示的汉字
				afterPageText : '页    共 {pages} 页',
				buttons : "#footer",
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			/*
			 * onBeforeRefresh:function(){ $(this).pagination('loading');
			 * alert('before refresh'); $(this).pagination('loaded'); }
			 */
			});
		});
	}

	function myModal2() {
		// 获取车辆列表

		var carName = $("#carName").val();
		var name = $.trim(carName);
			var classTaskid=$("#classTaskid").val();
			var areaid=$("#area_id").val();
			var dg;
			dg = $('#dg').datagrid({
				url : "${ctx}/guard/car/listData?classTaskId="+classTaskid+"&name="+name+"&area.id="+areaid,// JSON数据路径
				loadMsg : "正在努力加载数据，请稍后...", // 遮罩层
				async : true,
				fit : false,// 自动大小
				fitColumns : false, // 是否固定列
				singleSelect : false,// 是否选择多行
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
			var p = dg.datagrid('getPager');
			$(p).pagination({
				pageSize : 20,
				beforePageText : '第',// 页数文本框前显示的汉字
				afterPageText : '页    共 {pages} 页',
				buttons : "#footer",
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			/*
			 * onBeforeRefresh:function(){ $(this).pagination('loading');
			 * alert('before refresh'); $(this).pagination('loaded'); }
			 */
			});
	}
	function setCarMessage() {
		// 设置车辆信息
		var rows = $('#dg').datagrid('getSelections');
		if (rows.length > 10) {
			top.$.jBox.tip("最多能分配10辆车");
		}
		var $table = $("#tzgjz_tab tr");
		var len = $table.length;
		for (var n = 0; n < len; n++) {
			$("tr[id=\'classCarInfoList" + n + "\']").remove();
		}
		for (var i = 0; i < rows.length; i++) {
			var idx = (i+1);
			//编号
			var id = rows[i].id;
			//车牌号
			var carplate = rows[i].carplate;
			var cardNum = rows[i].cardNum;
			var name = rows[i].name;
			var $table = $("#tzgjz_tab tr");
			var len = $table.length;
			$("#tzgjz_tab")
					.append(
							"<tr id=classCarInfoList"
									+ (len - 1)
									+ "><td align=\'center\'>"
									+ "<input align=\'center\' id=\'classCarInfoList["
									+ (len - 1)
									+ "].carId\' name=\'classCarInfoList["
									+ (len - 1)
									+ "].carId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ id
									+ "\'/>"
									+ "<div style='width:50px;'>"+(i+1)+"</div>"
									+ "<input align=\'center\' id=\'classCarInfoList["
									+ (len - 1)
									+ "].delete\' style=\'display: none\' name=\'classCarInfoList["
									+ (len - 1)
									+ "].delete\' type=\'text\' value=\'"
									+ 0
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input id=\'classCarInfoList["
									+ (len - 1)
									+ "].name\' name=\'classCarInfoList["
									+ (len - 1)
									+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ name
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input id=\'classCarInfoList["
									+ (len - 1)
									+ "].carplate\' name=\'classCarInfoList["
									+ (len - 1)
									+ "].carplate\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ carplate
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input id=\'classCarInfoList["
									+ (len - 1)
									+ "].cardNum\' name=\'classCarInfoList["
									+ (len - 1)
									+ "].cardNum\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ cardNum + "\'/>" + "</td></tr>");
		}
		$('#myModal').modal("hide");

	}

	function myModalTwo2() {
		var keyName = $("#keyName").val();
		var name = $.trim(keyName);
		var areaid=$("#area_id").val();
		var dgtwo;
		var classTaskid=$("#classTaskid").val();
		dgtwo = $('#dgtwo').datagrid({
			url : "${ctx}/guard/staff/listClassTaskInfoData?classTaskId="+classTaskid+"&name="+name+"&area.id="+areaid, // JSON数据路径
			loadMsg : "正在努力加载数据，请稍后...", // 遮罩层
			async : true,
			fit : false,// 自动大小
			fitColumns : false, // 是否固定列
			singleSelect : false,// 是否选择多行
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
	}
	function myModalTwo() {
		// 人员列表
		$('#myModalTwo').modal({
			keyboard : true
		});
		
		$('#myModalTwo').one('shown.bs.modal', function() {
			var dgtwo;
			var classTaskid=$("#classTaskid").val();
			var areaid=$("#area_id").val();
			dgtwo = $('#dgtwo').datagrid({
				url : "${ctx}/guard/staff/listClassTaskInfoData?classTaskId="+classTaskid+"&area.id="+areaid, // JSON数据路径
				loadMsg : "正在努力加载数据，请稍后...", // 遮罩层
				async : true,
				fit : false,// 自动大小
				fitColumns : false, // 是否固定列
				singleSelect : false,// 是否选择多行
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

	function setStaffMessage() {
		// 设置人员信息
		var rows = $('#dgtwo').datagrid('getSelections');
		if (rows.length > 20) {
			top.$.jBox.tip("最多能分配20个人员");
			return;
		}
		var $table = $("#tzgjz_tabTwo tr");
		var len = $table.length;
		for (var n = 0; n < len; n++) {
			$("tr[id=\'classPersonInfoList" + n + "\']").remove();
		}
		for (var i = 0; i < rows.length; i++) {
			//指纹号
			var fingerNumLabel = rows[i].fingerInfoList[0].fingerNumLabel;
			var fingerNum =  rows[i].fingerInfoList[0].fingerNum;
			//编号
			var id = rows[i].id;
			//人员名称
			var name = rows[i].name;
			//证件号码 
			var identifyNumber = rows[i].identifyNumber;
			var $table = $("#tzgjz_tabTwo tr");
			var len = $table.length;
			$("#tzgjz_tabTwo")
					.append(
							"<tr id=classPersonInfoList"
									+ (len - 1)
									+ "><td align=\'center\'>"
									+ "<input align=\'center\' id=\'classPersonInfoList["
									+ (len - 1)
									+ "].personId\' name=\'classPersonInfoList["
									+ (len - 1)
									+ "].personId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ id
									+ "\'/>"
									+ "<div style='width:50px;'>"+(i+1)+"</div>"
									+ "<input align=\'center\' id=\'classPersonInfoList["
									+ (len - 1)
									+ "].delete\' name=\'classPersonInfoList["
									+ (len - 1)
									+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
									+ 0
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'classPersonInfoList["
									+ (len - 1)
									+ "].personName\' name=\'classPersonInfoList["
									+ (len - 1)
									+ "].personName\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ name
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'classPersonInfoList["
									+ (len - 1)
									+ "].fingerNum\' name=\'classPersonInfoList["
									+ (len - 1)
									+ "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ fingerNum
									+ "\'/>"
									+ fingerNumLabel
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'classPersonInfoList["
									+ (len - 1)
									+ "].identifyNumber\' name=\'classPersonInfoList["
									+ (len - 1)
									+ "].identifyNumber\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ identifyNumber + "\'/>"
									+ "</td></tr>");
		}
		$('#myModalTwo').modal("hide");
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

	function setLineMessage() {
		// 设置线路信息
		var rows = $('#dgthere').datagrid('getSelections');
		if (rows.length == 1) {
			//编号
			var id = rows[0].id;
			//线路名称
			var name = rows[0].lineName;
			var area_id = rows[0].area.id;
			$("#area_id").val(area_id);
			$("#lineMessage").val(name);
			$("#line").val(id);

			$('#myModalThere').modal("hide");
		} else {
			alert("只能选择一条线路");
		}
	}

	function gradeChange() {
		var lab = $("#label").val();
		if (lab == 0) {
			document.getElementById("inter").readOnly = true;
			$("#inter").val("0");
		} else if (lab == 1) {
			document.getElementById("inter").readOnly = false;
			$("#inter").val("1");
		}
	}

	function fingerNum(id, row, index) {
		if (typeof (row.id) == "undefined") {
			return id;
		}
		return row.fingerInfoList[0].fingerNumLabel;
	}
</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/classTaskInfo/list">班组信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/classTaskInfo/form?id=${classTaskInfo.id}">班组信息<shiro:hasPermission
					name="guard:classTaskInfo:edit">${not empty classTaskInfo.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:classTaskInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="classTaskInfo"
		action="${ctx}/guard/classTaskInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<input value="${classTaskInfo.id}" id="classTaskid"
			style="display: none">
		<input value="${classTaskInfo.area.id}" id="areaid"
			style="display: none">
		<div class="row">
			<div class="col-xs-5">
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>班组名称：</label>
					<div class="col-xs-5">
						<form:input path="name" htmlEscape="false" maxlength="32"
							id="bzName" class="form-control input-sm required" />
					</div>
					<form:errors path="name" cssClass="error"></form:errors>
				</div>

				<form:input path="area.id" htmlEscape="false" id="area_id"
					class="input-xlarge required" style="display: none" />

				<form:input path="line.id" htmlEscape="false" id="line"
					class="input-xlarge required" style="display: none" />

				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>线路名称：</label>
					<div class="col-xs-5">
						<span class="input-group" style="display: inline-table;vertical-align: middle;">
							<form:input path="line.lineName" htmlEscape="false"
							onclick="myModalThere();" id="lineMessage" readonly="true"
							class="form-control input-sm required error-input-sub" />
							<span class="input-group-btn">
							   <ahref="javascript:void(0)" onclick="myModalThere()" class="btn btn-default btn-sm " style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
							</span>
						</span>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>车辆确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyCar" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_car')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>款箱确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyLocker" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_locker')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>专员确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyInterMan" class="form-control input-sm required"
							onchange="gradeChange()" id="label">
							<form:options items="${fns:getDictList('inter_man')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>最少专员数量：</label>
					<div class="col-xs-5">
						<form:input path="interManNum" htmlEscape="false" id="inter"
							class="form-control input-sm required digits" readOnly="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>最少押款员数量：</label>
					<div class="col-xs-5">
						<form:input path="patrolManNum" htmlEscape="false"
							class="form-control input-sm required digits" readOnly="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4"><font class="red">*</font>超时时间：</label>
					<div class="col-xs-5">
						<div class="input-group">
							<form:input path="taskTimeout" htmlEscape="false"
								class="form-control input-sm required digits" value="60" />
							<span class="input-group-addon input-sm">分钟</span>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-xs-4">备注：</label>
					<div class="col-xs-5"  >
						<form:textarea path="remarks" htmlEscape="false" rows="4"
							maxlength="255" class="form-control input-sm " style="width:100%;" />
					</div>
				</div>
			</div>
			<div class="col-xs-6">
				<button type="button" class="btn btn-success btn-sm" onclick="myModal();" id="car_but">增删车辆</button>
				<div id="car_tab">
					<table id="tzgjz_tab" 
						class="table table-striped table-bordered table-condensed"
						style="width: 80%;">
						<tr>
							<td style="width: 100px;" align="center">序号</td>
							<td width="80" align="center">车辆名称</td>
							<td style="width: 100px;" align="center">车牌号</td>
							<td width="80" align="center">车辆卡号</td>
						</tr>
					</table>
				</div>
				<br /> 
				<div class="form-group">
					<div class="col-md-2">
					<input type="button" value="增删押款员" onclick="myModalTwo();"
						id="staff_but" class="btn btn-success btn-sm" />
					</div>
					<div class="col-md-5">
						<form:errors path="patrolManNum" cssClass="error"></form:errors>
					</div>
				</div>
				<div id="staff_tab">
					<table id="tzgjz_tabTwo"
						class="table table-striped table-bordered table-condensed"
						style="width: 80%;">
						<tr>
							<td width="50" align="center">序号</td>
							<td width="60" align="center">姓名</td>
							<td width="60" align="center">指纹号</td>
							<td width="120" align="center">证件号</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:classTaskInfo:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">车辆信息选择</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 800px;">
				
				<input id="carName" placeholder="请输入车辆名称" style="width: 200px;margin-bottom: 5px;">
				<input type="button" value="搜索" onclick="myModal2()"/>
				
					<form id="dgForm" enctype="multipart/form-data"
						style="width: 600px;">
						<!--车辆列表-->
						<table id="dg" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'name'" sortable="true" width="25%">车辆名称</th>
									<th data-options="field:'carplate'" sortable="true" width="15%">车牌号</th>
									<th data-options="field:'cardNum'" sortable="true" width="20%">车辆卡号</th>
									<th data-options="field:'admin'" sortable="true" width="15%">负责人</th>
									<th data-options="field:'phone'" sortable="true" width="20%">联系方式</th>
								</tr>
							</thead>
						</table>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" name="filesubmit"
								onclick="setCarMessage();" value="确定">确定</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModalTwo" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">人员信息选择</h4>
				</div>
				
				<!-- 表单 -->
				<div class="modal-body" style="width: 800px;">
				<input id="keyName" placeholder="请输入姓名" style="width: 200px;margin-bottom: 5px;">
				<input type="button" value="搜索" onclick="myModalTwo2()"/>
					<form id="dgFormtwo" enctype="multipart/form-data"
						style="width: 600px;">
						<!--车辆列表-->
						<table id="dgtwo" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'id'" sortable="true" width="15%">编号</th>
									<th data-options="field:'name'" sortable="true" width="20%">人员名称</th>
									<th data-options="field:'fingerInfoList'" sortable="true"
										formatter="fingerNum" width="20%">指纹号</th>
									<th data-options="field:'identifyNumber'" sortable="true"
										width="23%">证件号码</th>
									<th data-options="field:'phone'" sortable="true" width="20%">联系方式</th>
								</tr>
							</thead>
						</table>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" name="filesubmit"
								onclick="setStaffMessage();" value="确定">确定</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- 模态框（Modal） -->
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