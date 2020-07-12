<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱调拨管理</title>
<meta name="decorator" content="default"/>
<link
	href="${ctxStatic}/jquery-easyui-1.4.2/themes/bootstrap/easyui.css"
	rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js'></script>
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/jquery.easyui.min.js'></script>
<!-- jQuery easyui datagrid filter-->
<%@include file="/WEB-INF/views/include/treeview.jsp"%>	
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
.table-responsive {
  height: 100%;
}
.user-table{
  border-right:1px solid #ddd;
  border-left:1px solid #ddd;
  padding: 0px;
  border-top:0px;
  border-bottom:0px;
}


</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				var dg1;
				dg1 = $('#dg').datagrid({
					onDblClickRow: function(index,row){

						$('#dg').datagrid('deleteRow',index);
						if(row.boxType == '调拨款箱'){
							$('#dg3').datagrid('insertRow',{
								index: 0,	// index start with 0
								row: row
							});
						}else{
							$('#dg2').datagrid('insertRow',{
								index: 0,	// index start with 0
								row: row
							});
						}
					}
				});
				var dg2;
				dg2 = $('#dg2').datagrid({
					onDblClickRow: function(index,row){
						$('#dg2').datagrid('deleteRow',index);
						$('#dg').datagrid('insertRow',{
							index: 0,	// index start with 0
							row: row
						});
					}
				});
				var dg3;
				dg3 = $('#dg3').datagrid({
					onDblClickRow: function(index,row){
						$('#dg3').datagrid('deleteRow',index);
						$('#dg').datagrid('insertRow',{
							index: 0,	// index start with 0
							row: row
						});
					}
				});

				$("#inputForm").validate({
					submitHandler: function(form){
					}
				});	
				
				var warnningMsg = $("#warnningMsg").val();
				top.$.jBox.closeTip();
				if(warnningMsg){
					 top.$.jBox.tip(warnningMsg);
				}
			});
	function onSubmit(){
		var equipmentId = $("#equipmentId").val();
		 if(!equipmentId){
			 top.$.jBox.tip('只有营业网点或者子网点才能款箱调拨并且必须要绑定设备！');
			 return;
		 }
		if(!$("#inputForm").valid()){
			return;
		}
		
		 var selectRows = $('#dg').datagrid('getSelections');
		 if(selectRows.length == 0){
				top.$.jBox.tip('请选择网点款箱！');
				return;
		 }
		 var param = {};
		 param.equipmentId = equipmentId;
		 param.rows = selectRows;
		 param.classTaskInfo={};
		 param.classTaskInfo.id = $("#classTaskInfoId").val();
		 param.alloType=$("#allo_type").val();
		 param.scheduleDate = $("#schedule_date").val();
		 loading('正在提交，请稍等...');
		 $.ajax({
		        type: "POST",
		        url: "${ctx}/guard/moneyBoxAllot/saveAllot",
		        data: $.customParam(param),
		        dataType: "json"
		    }).done(function(response) {
		    	closeLoading();
		    	top.$.jBox.tip('调拨成功！');
		    }).fail(function() {
		    	closeLoading();
		    	top.$.jBox.tip('提交至服务器失败，请检查网络连接！');
		    });
	}
	
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
	
	function lengName(id, row, index) {
		if (typeof (row.id) == "undefined") {
			return id;
		}
		return row.line.lineName;
	}
	
	function setBZMessage() {
		// 设置人员信息
		var rows = $('#dgtwo').datagrid('getSelections');
		if(rows && rows.length==0){
			$("#classTaskInfoId").val('');
			$("#classTaskfo_name").val('');
			return;
		}
		$("#classTaskInfoId").val(rows[0].id);
		$("#classTaskfo_name").val(rows[0].name);
		$('#myModalbz').modal("hide");
	}

</script>

</head>
<body>
	<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/moneyBoxAllot/" target="mainFrame">款箱调拨列表</a></li>
		<li class="active"><a
			href="#">款箱调拨</a></li>
		<li class="pull-right"><input type="button" onclick="onSubmit()" class="btn btn-primary ${equipmentId==null?'disabled':'' }" value="提交调拨"/></li>
	</ul>
	<input type="hidden" id="warnningMsg" value="${warnningMsg }"/>
	<input id="equipmentId" value="${equipmentId }" style="display:none;">
	<br />
	<form:form class="form-horizontal"  id="inputForm">
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>网点：</label>
			<div class="col-xs-2">
				<input type="text"  name="officeName" value="${office.name }" readonly="true" 	class="form-control input-sm " />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>班组：</label>
			<div class="col-xs-2">
				<div class="input-group">
					<input type="hidden" name="classTaskInfoId" id="classTaskInfoId" value="${classTaskInfo.id}"/>
					<input type="text"  name="classTaskInfoName" htmlEscape="false" value="${classTaskInfo.name}"
						id="classTaskfo_name" onclick="myModalTwo();" readonly="true"
						class="form-control input-sm required" />
					<span class="input-group-btn" onclick="myModalTwo();">
						<a href="javascript:" class="btn btn-default btn-sm" style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
					</span>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>调拨类型：</label>
			<div class="col-xs-2">
				<select id="allo_type" class="form-control input-sm required">
					<c:forEach items="${fns:getDictList('allo_type')}" var="var">
						<option value="${var.value }">${var.label }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label  class="control-label col-xs-2"><font class="red">*</font>派送日期：</label>
			<div class="col-xs-2">
			<input id="schedule_date" name="scheduleDate" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate required" style="height:auto"
				value="${currentDate}" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</div>
		</div>
	</form:form>
	
	<div class="easyui-layout" id="layout" >
     <div data-options="region:'center',title:'网点款箱'">
         <table id="dg"  data-options="singleSelect:false,collapsible:true" >
			<caption>网点款箱 </caption>
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'boxCode'" width="20%">款箱编码</th>
					<th data-options="field:'cardNum'" width="20%">款箱卡号</th>
					<th data-options="field:'boxType'" width="20%">款箱类型</th>
					<th data-options="field:'office.name'" width="20%">所属网点</th>
					<th data-options="field:'id'" width="19%">款箱序号</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${exclusive}" var="moneyBox">
					<tr>
						<td>&nbsp;</td>
						<td>${moneyBox.boxCode}</td>
						<td>${moneyBox.cardNum}</td>
						<td>早晚尾箱</td>
						<td>${moneyBox.office.name}</td>
						<td>${moneyBox.id}</td>
					</tr>
				</c:forEach>
				<c:forEach items="${tmp}" var="moneyBox">
					<tr>
						<td>&nbsp;</td>
						<td>${moneyBox.boxCode}</td>
						<td>${moneyBox.cardNum}</td>
						<td>调拨款箱</td>
						<td>${moneyBox.office.name}</td>
						<td>${moneyBox.id}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
     </div>
	</div>
	<script type="text/javascript">
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : '0'
				}
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					var id = treeNode.id == '0' ? '' : treeNode.id;
					$('#officeContent').attr(
							"src",
							"${ctx}/guard/moneyBoxAllot/form?officeId=" + id
									+ "&office.name=" + treeNode.name);
				}
			}
		};

		function refreshTree() {
			$.getJSON("${ctx}/sys/office/treeData", function(data) {
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
			});
		}
		refreshTree();

		var leftWidth = 180; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		
		function wSize() {
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({
				"overflow-x" : "hidden",
				"overflow-y" : "hidden"
			});
			mainObj.css("width", "auto");
			frameObj.height(strs[0] - 5);
			$("#layout").width(	$("#content").width());
			$("#layout").height(strs[0] - 5);
			$("#left").css({
				"overflow-x" : "hidden",
				"overflow-y" : "hidden"
			});
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width(
					$("#content").width() - leftWidth - $("#openClose").width()
							- 5);
			$(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>















<%-- 
	<form:form id="inputForm" modelAttribute="moneyBoxAllot"
		action="${ctx}/guard/moneyBoxAllot/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2">款箱编号：<span
				class="help-inline"><font color="red">*</font> </span></label>
			<div class="col-xs-4">
				<form:input path="moneyBoxId" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">设备ID：<span
				class="help-inline"><font color="red">*</font> </span></label>
			<div class="col-xs-4">
				<form:input path="equipmentId" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">任务ID：<span
				class="help-inline"><font color="red">*</font> </span></label>
			<div class="col-xs-4">
				<form:input path="taskScheduleId" htmlEscape="false"
					class="input-xlarge required" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">调拨时间：</label>
			<div class="col-xs-4">
				<input name="allotTime" type="text" readonly="readonly"
					maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${moneyBoxAllot.allotTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:moneyBoxAllot:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form> --%>

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
	</div>
</body>
</html>