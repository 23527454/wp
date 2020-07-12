<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班信息管理</title>
<meta name="decorator" content="default" />
<link
	href="${ctxStatic}/jquery-easyui-1.4.2/themes/bootstrap/easyui.css"
	rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js'></script>
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/jquery.easyui.min.js'></script>
<script type="text/javascript">

	    function initLists(taskCarInfoList,taskPersonInfoList,taskLineInfoList ){
	    	var dataCar = taskCarInfoList;
	    	if(dataCar){
				for(var i=0;i<dataCar.length;i++){
					var $table = $("#tzgjz_tab tr");
					var len = $table.length;
					$("#tzgjz_tab tbody")
							.append(
									"<tr id='taskCarInfoList"
											+ (len - 1)
											+ "'><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskCarInfoList["
											+ (len - 1)
											+ "].carId\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].carId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].carId
											+ "\'/>"
											+ "<div style='width:50px;'>"+(i+1)+"</div>"
											+ "<input align=\'center\' id=\'taskCarInfoList["
											+ (len - 1)
											+ "].delete\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
											+ 0
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].name\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].name\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].name
											+ "\'/>"
											+ dataCar[i].name
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].carplate\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].carplate\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].carplate
											+ "\'/>"
											+ dataCar[i].carplate
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].cardNum\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].cardNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ dataCar[i].cardNum + "\'/>" 
											+ dataCar[i].cardNum 
											+ "</td></tr>");
				}
	    	}
			var dataStaff = taskPersonInfoList;
			if(dataStaff){
				for(var i=0;i<dataStaff.length;i++){
					var $table = $("#tzgjz_tabTwo tr");
					var len = $table.length;
					$("#tzgjz_tabTwo tbody").append(
							"<tr id='taskPersonInfoList"
									+ (len - 1)
									+ "'><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskPersonInfoList["
									+ (len - 1)
									+ "].personId\' name=\'taskPersonInfoList["
									+ (len - 1)
									+ "].personId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ dataStaff[i].personId
									+ "\'/>"
									+ "<div style='width:50px;'>"+(i+1)+"</div>"
									+ "<input align=\'center\' id=\'taskPersonInfoList["
									+ (len - 1)
									+ "].delete\' name=\'taskPersonInfoList["
									+ (len - 1)
									+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
									+ 0
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskPersonInfoList["
									+ (len - 1)
									+ "].personName\' name=\'taskPersonInfoList["
									+ (len - 1)
									+ "].personName\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ dataStaff[i].name
									+ "\'/>"
									+ dataStaff[i].name
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskPersonInfoList["
									+ (len - 1)
									+ "].fingerNum\' name=\'taskPersonInfoList["
									+ (len - 1)
									+ "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ dataStaff[i].fingerNum
									+ "\'/>"
									+ dataStaff[i].fingerNumLabel
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskPersonInfoList["
									+ (len - 1)
									+ "].identifyNumber\' name=\'taskPersonInfoList["
									+ (len - 1)
									+ "].identifyNumber\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ dataStaff[i].identifyNumber + "\'/>"
									+ dataStaff[i].identifyNumber
									+ "</td></tr>");	
				}
			}
			
			var dataLine = taskLineInfoList;
			if(dataLine){
				for(var i=0;i<dataLine.length;i++){
					var $table = $("#tzgjz_tabThere tr");
					var len = $table.length;
					
					var name = dataLine[i].name || dataLine[i].office.name;
					debugger;
					$("#tzgjz_tabThere tbody").append(
							"<tr id='taskLineInfoList"
									+ (len - 1)
									+ "'><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskLineInfoList["
									+ (len - 1)
									+ "].equipmentId\' name=\'taskLineInfoList["
									+ (len - 1)
									+ "].equipmentId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ dataLine[i].equipmentId
									+ "\'/>"
									+ "<div style='width:50px;'>"+(i+1)+"</div>"
									+ "<input align=\'center\' id=\'taskLineInfoList["
									+ (len - 1)
									+ "].delete\' name=\'taskLineInfoList["
									+ (len - 1)
									+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
									+ 0
									+ "\'/>"
									+ "</td><td align=\'center\'>"
									+ "<input align=\'center\' id=\'taskLineInfoList["
									+ (len - 1)
									+ "].name\' name=\'taskLineInfoList["
									+ (len - 1)
									+ "].name\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
									+ name + "\'/>"
									+ name
									+ "</td></tr>");
				}
			}
		}
		$(document).ready(function() {
			var className = $("#classTaskfo_name").val();
			$("#selectedClassTaskId").val(parent.$("#classTaskfo_id").val());
			if (!$("#primaryKey").val()) {
				initLists(${fns:toJson(taskScheduleInfo.taskCarInfoList)},
						${fns:toJson(taskScheduleInfo.taskPersonInfoList)},
						${fns:toJson(taskScheduleInfo.taskLineInfoList)});
			}
			var task = $("#task_type").val();
			if (task == "0") {
				$("#taskType0").attr("checked", "checked");
			} else if (task == "1") {
				$("#taskType1").attr("checked", "checked");
			} else if (task == "2") {
				$("#taskType2").attr("checked", "checked");
			} else if (task == "3") {
				$("#taskType3").attr("checked", "checked");
			} else if (task == "4") {
				$("#taskType4").attr("checked", "checked");
			} else {
				$("#taskType0").attr("checked", "checked");
				$("#task_type").val(0);
			}
			
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#tzgjz_tabTwo tbody tr").length < $("#patrolManNum").val()){
						top.$.jBox.tip('押运员的数量不能小于'+$("#patrolManNum").val()+'人！');
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
			});
		});
		
		function myModalTwo() {
			// 班组列表
			$('#myModalbz').modal({
				keyboard : true
			});
			$('#myModalbz').one('shown.bs.modal', function() {
				var dgtwo;
				dgtwo = $('#dgtwo').datagrid({
					url : "${ctx}/guard/classTaskInfo/listData", // JSON数据路径
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

		function setBZMessage() {
			// 设置人员信息
			var rows = $('#dgtwo').datagrid('getSelections');
			var claID= rows[0].id;   // 班组ID
			var class_id=$("#classTaskfo_id").val();
			if(class_id!=claID && claID!="" && class_id!=""){
				$("#tzgjz_tab tbody tr").empty();
				$("#tzgjz_tabTwo tbody tr").empty();
				$("#tzgjz_tabThere tbody tr").empty();
			}
			
			var claName=rows[0].name;  // 班组名称
			var lineID=rows[0].line.id;  // 线路ID
			var claVerifyCar=rows[0].verifyCar;  // 车辆验证
			var claVerifyInterMan=rows[0].verifyInterMan;  // 专员验证
			var claVerifyLocker=rows[0].verifyLocker;  // 款箱验证
			var claPatrolManNum=rows[0].patrolManNum; //押款员数量
			var claInterManNum=rows[0].interManNum; //专员数量
			var claTaskTimeout=rows[0].taskTimeout; //超时时间
			
			if(claVerifyCar=="需要"){
				$("#verifyCar").val("1");
			}else if(claVerifyCar=="不需要"){
				$("#verifyCar").val("0");
			}else{
				$("#verifyCar").val("");
			}
			if(claVerifyInterMan=="需要"){
				$("#verifyInterMan").val("1");
			}else if(claVerifyInterMan=="不需要"){
				$("#verifyInterMan").val("0");
			}else{
				$("#verifyInterMan").val("");
			}
			if(claVerifyLocker=="需要"){
				$("#verifyMoneyBox").val("1");
			}else if(claVerifyLocker=="不需要"){
				$("#verifyMoneyBox").val("0");
			}else{
				$("#verifyMoneyBox").val("");
			}
			
			$("#classTaskfo_id").val(claID);
			$("#classTaskfo_name").val(claName);
			$("#line_id").val(lineID);
			$("#patrolManNum").val(claPatrolManNum);
			$("#interManNum").val(claInterManNum);
			$("#taskTimeout").val(claTaskTimeout);
			
			$.getJSON("${ctx}/guard/classTaskInfo/ajaxGetClassTaskInfo?id="+claID,function(data){
				initLists(data.classCarInfoList, data.classPersonInfoList, data.linNodesList);
			});
			$('#myModalbz').modal("hide");
		}
		
		function myModalCar() {
			var id=$("#classTaskfo_id").val();
			if(id!="" && typeof(id)!="undefined"){
			// 班组列表
			$('#myModalCar').modal({
				keyboard : true
			});
			$('#myModalCar').one('shown.bs.modal', function() {
				var dgCar;
				
				dgCar = $('#dgCar').datagrid({
					url : "${ctx}/guard/classTaskInfo/listData?id="+id, // JSON数据路径
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
							rows : data.list[0].classCarInfoList
						};
					},
					pageSize : 20,// 每页显示的记录条数，默认为10
					pageList : [ 10, 20, 30, 40, 50 ],// 可以设置每页记录条数的列表
					enableFilter : true
				});
				var p = dgCar.datagrid('getPager');
				$(p).pagination({
					pageSize : 20,
					beforePageText : '第',// 页数文本框前显示的汉字
					afterPageText : '页    共 {pages} 页',
					buttons : "#footer",
					displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
				});
			});
			}else{
				alert("请先选择班组");
			}
		}

		function setCarMessage() {
			// 设置车辆信息
			var rows = $('#dgCar').datagrid('getSelections');
			if (rows.length <= 10) {
				var $table = $("#tzgjz_tab tr");
				var len = $table.length;
				for (var n = 0; n < len; n++) {
					$("tr[id=\'taskCarInfoList" + n + "\']").remove();
				}
				for (var i = 0; i < rows.length; i++) {
					//编号
					var id = rows[i].carId;
					//车牌号
					var carplate = rows[i].carplate;
					var cardNum = rows[i].cardNum;
					var name = rows[i].name;
					var $table = $("#tzgjz_tab tr");
					var len = $table.length;
					$("#tzgjz_tab")
							.append(
									"<tr id=taskCarInfoList"
											+ (len - 1)
											+ "><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskCarInfoList["
											+ (len - 1)
											+ "].carId\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].carId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ id
											+ "\'/>"
											+ "<div style='width:50px;'>"+(i+1)+"</div>"
											+ "<input align=\'center\' id=\'taskCarInfoList["
											+ (len - 1)
											+ "].delete\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
											+ 0
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].name\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ name
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].carplate\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].carplate\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ carplate
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input id=\'taskCarInfoList["
											+ (len - 1)
											+ "].cardNum\' name=\'taskCarInfoList["
											+ (len - 1)
											+ "].cardNum\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ cardNum + "\'/>" + "</td></tr>");
					}
					$('#myModalCar').modal("hide");
				} else {
				alert("最多选择10台车辆!");
			}
		}
		
		
		function myModalStaff() {
			var id=$("#classTaskfo_id").val();
			if(id!="" && typeof(id)!="undefined"){
			// 班组列表
			$('#myModalStaff').modal({
				keyboard : true
			});
			$('#myModalStaff').one('shown.bs.modal', function() {
				var dgStaff;
				dgStaff = $('#dgStaff').datagrid({
					url : "${ctx}/guard/classTaskInfo/listData?id="+id, // JSON数据路径
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
							rows : data.list[0].classPersonInfoList
						};
					},
					pageSize : 20,// 每页显示的记录条数，默认为10
					pageList : [ 10, 20, 30, 40, 50 ],// 可以设置每页记录条数的列表
					enableFilter : true
				});
				var p = dgStaff.datagrid('getPager');
				$(p).pagination({
					pageSize : 20,
					beforePageText : '第',// 页数文本框前显示的汉字
					afterPageText : '页    共 {pages} 页',
					buttons : "#footer",
					displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
				});
			});
			}else{
				alert("请先选择班组");
			}
		}

		function setStaffMessage() {
			// 设置人员信息
			var rows = $('#dgStaff').datagrid('getSelections');
			
			if (rows.length <= 20) {
				var $table = $("#tzgjz_tabTwo tr");
				var len = $table.length;
				for (var n = 0; n < len; n++) {
					$("tr[id=\'taskPersonInfoList" + n + "\']").remove();
				}
				for (var i = 0; i < rows.length; i++) {
					//指纹号
					var fingerNumLabel = rows[i].fingerNumLabel;
					var fingerNum = rows[i].fingerNum;
					//编号
					var id = rows[i].personId;
					//人员名称
					var name = rows[i].name;
					//证件号码 
					var identifyNumber = rows[i].identifyNumber;
					var $table = $("#tzgjz_tabTwo tr");
					var len = $table.length;
					$("#tzgjz_tabTwo")
							.append(
									"<tr id=taskPersonInfoList"
											+ (len - 1)
											+ "><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskPersonInfoList["
											+ (len - 1)
											+ "].personId\' name=\'taskPersonInfoList["
											+ (len - 1)
											+ "].personId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ id
											+ "\'/>"
											+ "<div style='width:50px;'>"+(i+1)+"</div>"
											+ "<input align=\'center\' id=\'taskPersonInfoList["
											+ (len - 1)
											+ "].delete\' name=\'taskPersonInfoList["
											+ (len - 1)
											+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
											+ 0
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskPersonInfoList["
											+ (len - 1)
											+ "].name\' name=\'taskPersonInfoList["
											+ (len - 1)
											+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ name
											+ "\'/>"
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskPersonInfoList["
											+ (len - 1)
											+ "].fingerNum\' name=\'taskPersonInfoList["
											+ (len - 1)
											+ "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ fingerNum
											+ "\'/>"
											+ fingerNumLabel
											+ "</td><td align=\'center\'>"
											+ "<input align=\'center\' id=\'taskPersonInfoList["
											+ (len - 1)
											+ "].identifyNumber\' name=\'taskPersonInfoList["
											+ (len - 1)
											+ "].identifyNumber\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
											+ identifyNumber + "\'/>"
											+ "</td></tr>");
				}
				$('#myModalStaff').modal("hide");
			} else {
				alert("最多能分配20个人员");
			}
		}
		
		
		function myModalLine() {
			var id=$("#classTaskfo_id").val();
			if(id!="" && typeof(id)!="undefined"){
			// 线路节点列表
			$('#myModalLine').modal({
				keyboard : true
			});
			$('#myModalLine').one('shown.bs.modal', function() {
				var dgLine;
				dgLine = $('#dgLine').datagrid({
					url : "${ctx}/guard/classTaskInfo/listData?id="+id, // JSON数据路径
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
							rows : data.list[0].linNodesList
						};
					},
					pageSize : 20,// 每页显示的记录条数，默认为10
					pageList : [ 10, 20, 30, 40, 50 ],// 可以设置每页记录条数的列表
					enableFilter : true
				});
				var p = dgLine.datagrid('getPager');
				$(p).pagination({
					pageSize : 20,
					beforePageText : '第',// 页数文本框前显示的汉字
					afterPageText : '页    共 {pages} 页',
					buttons : "#footer",
					displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
				});
			});
			}else{
				alert("请先选择班组");
			}
		}
		
		function setLineMessage() {
			// 线路节点信息
			var rows = $('#dgLine').datagrid('getSelections');
			var $table = $("#tzgjz_tabThere tr");
			var len = $table.length;
			for (var n = 0; n < len; n++) {
				$("tr[id=\'taskLineInfoList" + n + "\']").remove();
			}
			for (var i = 0; i < rows.length; i++) {
				//设备编号
				var id = rows[i].office.id;
				//设备名称
				var name = rows[i].office.name;
				var $table = $("#tzgjz_tabThere tr");
				var len = $table.length;
				$("#tzgjz_tabThere")
						.append(
								"<tr id=taskLineInfoList"
										+ (len - 1)
										+ "><td align=\'center\'>"
										+ "<input align=\'center\' id=\'taskLineInfoList["
										+ (len - 1)
										+ "].equipmentId\' name=\'taskLineInfoList["
										+ (len - 1)
										+ "].equipmentId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
										+ id
										+ "\'/>"
										+ "<div style='width:50px;'>"+(i+1)+"</div>"
										+ "<input align=\'center\' id=\'taskLineInfoList["
										+ (len - 1)
										+ "].delete\' name=\'taskLineInfoList["
										+ (len - 1)
										+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
										+ 0
										+ "\'/>"
										+ "</td><td align=\'center\'>"
										+ "<input align=\'center\' id=\'taskLineInfoList["
										+ (len - 1)
										+ "].name\' name=\'taskLineInfoList["
										+ (len - 1)
										+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
										+ name + "\'/>"
										+ "</td></tr>");
			}
			$('#myModalLine').modal("hide");
		}
		
		function lengName(id, row, index) {
			if (typeof (row.id) == "undefined") {
				return id;
			}
			return row.line.lineName;
		}
		
		function office_id(id, row, index) {
			if (typeof (row.id) == "undefined") {
				return id;
			}
			return row.office.id;
		}
		
		function office_name(id, row, index) {
			if (typeof (row.id) == "undefined") {
				return id;
			}
			return row.office.name;
		}
		
		function getRadio() {
			e = event.srcElement;
			if (e.type == "radio" && e.name == "radio") {
				if (e.value == 0) {
					$("#task_type").val(0);
				} else if (e.value == 1) {
					$("#task_type").val(1);
				} else if (e.value == 2) {
					$("#task_type").val(2);
				} else if (e.value == 3) {
					$("#task_type").val(3);
				} else if (e.value == 4) {
					$("#task_type").val(4);
				}
			}
		}
		
	</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/taskScheduleInfo/list?classTaskId=${selectedClassTaskId}">排班信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/taskScheduleInfo/form?id=${taskScheduleInfo.id}&selectedClassTaskId=${selectedClassTaskId}">排班信息<shiro:hasPermission
					name="guard:taskScheduleInfo:edit">${not empty taskScheduleInfo.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:taskScheduleInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="taskScheduleInfo"
		action="${ctx}/guard/taskScheduleInfo/save" method="post"
		class="form-horizontal">

		<div class="row">
			<div class="col-xs-7">
				<form:input path="classTaskId" htmlEscape="false"
					id="classTaskfo_id" class="input-xlarge required"
					style="display: none" />
				<input type="hidden" name="selectedClassTaskId" id="selectedClassTaskId"/>
				<form:input path="classTaskInfo.line.id" htmlEscape="false"
					id="line_id" class="input-xlarge required" style="display: none" />
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>班组：</label>
					<div class="col-xs-5">
						<div class="input-group">
							<form:input path="classTaskInfo.name" htmlEscape="false"
								id="classTaskfo_name" onclick="myModalTwo();" readonly="true"
								class="form-control input-sm required" />
							<span class="input-group-btn" onclick="myModalTwo();">
								<a href="javascript:" class="btn btn-default btn-sm" style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
							</span>
						</div>
					</div>
				</div>
				<form:input path="taskType" htmlEscape="false" id="task_type"
					class="input-xlarge required" style="display: none" />
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务类型：</label>
					<div class="col-xs-8">
						<div class="radio-inline">
							<span onclick="getRadio();"> 
							<input name="radio" type="radio" value="0" id="taskType0" /><label for="taskType0">派送</label>
							<input name="radio" type="radio" value="1" id="taskType1" /><label for="taskType1">回收 </label>
	                        <input name="radio" type="radio" value="2" id="taskType2" /><label for="taskType2">临时派送</label>
	                        <input name="radio" type="radio" value="3" id="taskType3" /><label for="taskType3">临时回收</label>
							<input name="radio" type="radio" value="4" id="taskType4" /><label for="taskType4">贵金属派送</label>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务执行日期：</label>
					<div class="col-xs-3">
						<input name="taskDate" type="text" readonly="readonly"
							maxlength="12" class="Wdate form-control input-sm "
							value="${taskScheduleInfo.taskDate}" id="date2"
							style="height: auto;width: 150px;"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务执行时间：</label>
					<div class="col-xs-3">
						<input name="taskTime" type="text" readonly="readonly"
							maxlength="8" class="Wdate form-control input-sm "
							value="${taskScheduleInfo.taskTime}" id="time2"
							style="height: auto;width: 150px;"
							onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>使用次数：</label>
					<div class="col-xs-5">
						<form:select path="taskTimeClass" class="form-control input-sm required">
							<form:options items="${fns:getDictList('task_time_class')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>车辆确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyCar" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_car')}"
								id="verifyCar" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>专员确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyInterMan" class="form-control input-sm required">
							<form:options items="${fns:getDictList('inter_man')}"
								id="verifyInterMan" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>款箱确认：</label>
					<div class="col-xs-5">
						<form:select path="verifyMoneyBox" class="form-control input-sm required">
							<form:options items="${fns:getDictList('verify_locker')}"
								id="verifyMoneyBox" itemLabel="label" itemValue="value"
								htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">最少专员数量：</label>
					<div class="col-xs-5">
						<form:input path="interManNum" htmlEscape="false" id="interManNum"
							class="form-control input-sm digits " readOnly="true"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">最少押款员数量：</label>
					<div class="col-xs-5">
						<form:input path="patrolManNum" htmlEscape="false"
							id="patrolManNum" class="form-control input-sm digits"  readOnly="true" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3"><font class="red">*</font>任务超时：</label>
					<div class="col-xs-5">
						<div class="input-group">
							<form:input path="taskTimeout" htmlEscape="false"
								class="form-control input-sm required digits"/>
							<span class="input-group-addon input-sm">分钟</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-3">备注：</label>
					<div class="col-xs-5">
						<form:textarea path="remarks" htmlEscape="false" rows="4"
							maxlength="255" class="input-xxlarge" style="width:100%;" />
					</div>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<div class="col-xs-12">
<!-- 					<button type="button" -->
<!-- 						class="btn btn-success btn-sm control-label" -->
<!-- 						onclick="myModalCar();" >车辆信息</button> -->
					<table id="tzgjz_tab" onclick="myModalCar();"
						class="table table-striped table-bordered table-condensed"
						style="width: 80%;">	
						<thead>
							<tr>
								<th style="width: 30px;" align="center">序号</th>
								<th align="center">车辆名称</th>
								<th align="center">车牌号</th>
								<th align="center">车辆卡号</th>
							</tr>
							<tbody>
						
							<%-- <c:forEach items="${taskScheduleInfo.taskCarInfoList}" var="item" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">1</td>							
									<td>${item.name}</td>
									<td>${item.carplate}</td>
									<td>${item.cardNum}</td>
						
					
								
				
								</tr>
							</c:forEach> --%>
							
							</tbody>
						</thead>
					</table>
					</div>	
					<form:errors path="taskCarInfoList" cssClass="error"></form:errors>
				</div>
				
				<div class="form-group">
					<div class="col-xs-12">
<!-- 						<button type="button" onclick="myModalStaff();" -->
<!-- 							class="btn btn-success btn-sm control-label">人员信息</button> -->
						<table id="tzgjz_tabTwo" onclick="myModalStaff();"
							class="table table-striped table-bordered table-condensed"
							style="width: 80%;">	
							<thead>
							<tr>
								<th style="width: 30px;" align="center">序号</th>
								<th align="center">人员名称</th>
								<th align="center">指纹号</th>
								<th align="center">证件号</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${taskScheduleInfo.taskPersonInfoList}" var="item" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">1</td>							
									<td>${item.name}</td>
									<td>${item.fingerNum}</td>
									<td>${item.identifyNumber}</td>
						
					
								
				
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
					<form:errors path="taskPersonInfoList" cssClass="error"></form:errors>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
<!-- 						<button type="button" value="" onclick="myModalLine();" -->
<!-- 							class="btn btn-success btn-sm control-label">线路信息</button> -->
						<table id="tzgjz_tabThere" onclick="myModalLine();"
							class="table table-striped table-bordered table-condensed"
							style="width: 80%;">
							<thead>
								<tr>
									<th align="center" style="width: 30px;">序号</th>
									<th align="center">营业网点名称</th>
								</tr>
							</thead>
							<tbody>
							<%-- <c:forEach items="${taskScheduleInfo.taskLineInfoList}" var="item" varStatus="varStatus">
								<tr>
									<td style="text-align: center;">1</td>							
									<td>${item.name}</td>
						
				
								</tr>
							</c:forEach> --%>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:taskScheduleInfo:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>



	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModalbz" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">班组信息选择</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 650px;">
					<form id="dgForm" enctype="multipart/form-data"
						style="width: 600px;">
						<!--班组列表-->
						<table id="dgtwo" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'id'" sortable="true" width="15%">编号</th>
									<th data-options="field:'name'" sortable="true" width="20%">班组名称</th>
									<th data-options="field:'line'" sortable="true" width="20%"
										formatter="lengName">线路名称</th>
									<th data-options="field:'verifyCar'" sortable="true"
										width="15%">是否车辆确认</th>
									<th data-options="field:'verifyInterMan'" sortable="true"
										width="15%">是否专员确认</th>
									<th data-options="field:'verifyLocker'" sortable="true"
										width="15%">是否款箱确认</th>
								</tr>
							</thead>
						</table>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" name="filesubmit"
								onclick="setBZMessage();" value="确定">确定</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModalCar" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">车辆信息选择</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 650px;">
					<form id="dgForm" enctype="multipart/form-data"
						style="width: 600px;">
						<!--班组列表-->
						<table id="dgCar" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'name'" sortable="true" width="35%">车辆名称</th>
									<th data-options="field:'carplate'" sortable="true" width="30%">车牌号</th>
									<th data-options="field:'cardNum'" sortable="true" width="30%">车辆卡号</th>
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
	<div class="modal fade" id="myModalStaff" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">人员信息选择</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 650px;">
					<form id="dgForm" enctype="multipart/form-data"
						style="width: 600px;">
						<!--班组列表-->
						<table id="dgStaff" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
<!-- 									<th data-options="field:'personId'" sortable="true" width="15%">人员编号</th> -->
									<th data-options="field:'name'" sortable="true" width="35%">人员名称</th>
									<th data-options="field:'fingerNumLabel'" sortable="true"
										width="30%">指纹号</th>
									<th data-options="field:'identifyNumber'" sortable="true"
										width="30%">证件号</th>
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
	<div class="modal fade" id="myModalLine" tabindex="-1" role="dialog"
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
					<form id="dgForm" enctype="multipart/form-data"
						style="width: 600px;">
						<!--班组列表-->
						<table id="dgLine" method="get" pagination="true"
							style="width: 100%; height: 400px"
							data-options="singleSelect:true,collapsible:true,pagination:true,pageSize:20">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'office_id'" sortable="true"
										width="15%" formatter="office_id">营业网点编号</th>
									<th data-options="field:'office_name'" sortable="true"
										width="80%" formatter="office_name">营业网点名称</th>
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