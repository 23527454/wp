<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>维保员派遣管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
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
			var dataPerson= ${fns:toJson(safeGuardDispatch.dispatchPersonInfoList)};
                for(var n=0;n<dataPerson.length;n++){
                    var $table = $("#tzgjz_tabTwo tr");
                    var len = $table.length;
                    $("#tzgjz_tabTwo")
                           .append(
                                "<tr id=dispatchPersonInfoList"
                                        + (len - 1)
                                        + "><td align=\'center\'>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].staffId\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].staffId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                        + dataPerson[n].staffId
                                        + "\'/>"
                                        + "<div style='width:50px;'>"+(n+1)+"</div>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].delete\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].delete\' type=\'text\' style=\'display: none\' value=\'"
                                        + 0
                                        + "\'/>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                           + (len - 1)
                                           + "].officeId\' name=\'dispatchPersonInfoList["
                                           + (len - 1)
                                           + "].officeId\' type=\'hidden\' style=\'display: none\' value=\'"
                                           + dataPerson[n].officeId
                                           + "\'/>"
                                        + "</td><td align=\'center\'>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].name\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                        + dataPerson[n].name
                                        + "\'/>"
                                        + "</td><td align=\'center\'>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].fingerNum\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                        + dataPerson[n].fingerNum
                                        + "\'/>"
                                       + dataPerson[n].fingerNum
                                        + "</td><td align=\'center\'>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].identifyNumber\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].identifyNumber\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                        + dataPerson[n].identifyNumber + "\'/>"
                                        + "</td><td align=\'center\'>"
                                           + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                           + (len - 1)
                                           + "].phone\' name=\'dispatchPersonInfoList["
                                           + (len - 1)
                                           + "].phone\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                           + dataPerson[n].phone + "\'/>"
                                           + "</td></tr>");
                }
			$("#inputForm").validate({
                            submitHandler : function(form) {
                                if(!document.getElementById('dispatchName').value){
                                    alert('派遣任务名称不能为空');
                                    return;
                                }
                                var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
                                if(nodes2.length==0){
                                      alert('请选择派遣范围');
                                    return;
                                }
                                for(var i=0; i<nodes2.length; i++) {
                                    ids2.push(nodes2[i].id);
                                }
                                $("#officeIds").val(ids2);
                                loading('正在提交，请稍等...');
                                form.submit();
                            }
              });
			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
            					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
            						tree.checkNode(node, !node.checked, true, true);
            						return false;
            					}}};

            var zNodes2=[
            					<c:forEach items="${officeList}" var="office">{id:"${office.id}", pId:"${not empty office.parent?office.parent.id:0}", name:"${office.name}"},
            		            </c:forEach>];
            			// 初始化树结构
            			var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
            			// 不选择父节点
            			tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
            			// 默认选择节点
            			var ids2 = "${safeGuardDispatch.officeIds}".split(",");
            			for(var i=0; i<ids2.length; i++) {
            				var node = tree2.getNodeByParam("id", ids2[i]);
            				try{tree2.checkNode(node, true, false);}catch(e){}
            			}
            			// 默认展开全部节点
            			tree2.expandAll(true);
            			// 刷新（显示/隐藏）机构


	});
	function myModalTwo2() {
    		var keyName = $("#keyName").val();
    		var name = $.trim(keyName);
    		//var areaid=$("#area_id").val();
    		var dgtwo;
    		dgtwo = $('#dgtwo').datagrid({
    			url : "${ctx}/guard/staff/listClassTaskInfoData?queryType="+2+"&name="+name, //+"&area.id="+areaid JSON数据路径
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
    			$("tr[id=\'dispatchPersonInfoList" + n + "\']").remove();
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
    			var phone = rows[i].phone;
    			var officeId = rows[i].company.id;
    			var $table = $("#tzgjz_tabTwo tr");
    			var len = $table.length;
    			$("#tzgjz_tabTwo")
    					.append(
    							"<tr id=dispatchPersonInfoList"
    									+ (len - 1)
    									+ "><td align=\'center\'>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].staffId\' name=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].staffId\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
    									+ id
    									+ "\'/>"
    									+ "<div style='width:50px;'>"+(i+1)+"</div>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].delete\' name=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].delete\' type=\'text\' style=\'display: none\' value=\'"
    									+ 0
    									+ "\'/>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].officeId\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].officeId\' type=\'hidden\' style=\'display: none\' value=\'"
                                        + officeId
                                        + "\'/>"
    									+ "</td><td align=\'center\'>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].name\' name=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].name\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
    									+ name
    									+ "\'/>"
    									+ "</td><td align=\'center\'>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].fingerNum\' name=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].fingerNum\' type=\'hidden\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
    									+ fingerNum
    									+ "\'/>"
    									+ fingerNumLabel
    									+ "</td><td align=\'center\'>"
    									+ "<input align=\'center\' id=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].identifyNumber\' name=\'dispatchPersonInfoList["
    									+ (len - 1)
    									+ "].identifyNumber\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
    									+ identifyNumber + "\'/>"
    									+ "</td><td align=\'center\'>"
                                        + "<input align=\'center\' id=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].phone\' name=\'dispatchPersonInfoList["
                                        + (len - 1)
                                        + "].phone\' type=\'text\' style=\'background:transparent;text-align:center;border:0px\' value=\'"
                                        + phone + "\'/>"
                                        + "</td></tr>");
    		}
    		$('#myModalTwo').modal("hide");
    	}

    	function myModalTwo() {
        		// 人员列表
        		$('#myModalTwo').modal({
        			keyboard : true
        		});

        		$('#myModalTwo').one('shown.bs.modal', function() {
        			var dgtwo;
        		//	var areaid=$("#area_id").val();
        			dgtwo = $('#dgtwo').datagrid({
        				url : "${ctx}/guard/staff/listClassTaskInfoData?queryType="+2, //+"&area.id="+areaid JSON数据路径
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
		<li><a href="${ctx}/guard/safeGuardDispatch/">维保员派遣列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/safeGuardDispatch/form?id=${safeGuardDispatch.id}">维保员派遣<shiro:hasPermission
					name="guard:staff:edit">${not empty safeGuardDispatch.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:staff:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="safeGuardDispatch" enctype="multipart/form-data"
		action="${ctx}/guard/safeGuardDispatch/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" id="id"/>
		<sys:message content="${message}" />
		<div class="row">
		<div class="col-xs-1">
		<label class="control-label" style="margin-left:60px;width:100px;">派遣范围:</label>
		</div>
		<div class="col-xs-3">
		    <div id="officeTree" class="ztree"
        					style="margin-left:60px;margin-top:3px;float:left;overflow:auto;height:330px;"></div>
        				<form:hidden path="officeIds" id="officeIds"/>
		    </div>
		<div class="col-xs-8">
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>派遣任务名称：</label>
			<div class="col-xs-7">
			<form:input  path="dispatchName"
					class="form-control input-sm required alnum" id="dispatchName" />
			</div>
        </div>
        <div class="form-group">
            <div class="col-md-2">
            <input type="button" value="增删维保员" onclick="myModalTwo();"
                id="staff_but" class="btn btn-success btn-sm" />
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
                    <td width="70" align="center">联系方式</td>
                </tr>
            </table>
        </div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:staff:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="${not empty safeGuardDispatch.id?'修改':'保存'}" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
		</div>
		</div>
	</form:form>
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
</div>
</body>
</html>