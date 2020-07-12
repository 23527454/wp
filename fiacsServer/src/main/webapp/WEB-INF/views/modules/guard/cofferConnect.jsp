<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>交接事件</title>
<meta name="decorator" content="default" />
<link
	href="${ctxStatic}/jquery-easyui-1.4.2/themes/bootstrap/easyui.css"
	rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js'></script>
<script type='text/javascript'
	src='${ctxStatic}/jquery-easyui-1.4.2/jquery.easyui.min.js'></script>
<script type="text/javascript">
	//loading('加载数据，请稍等...');
	$(document).ready(
			function() {
				
				$("#searchForm").validate({
					ignore: ".ignore",
					submitHandler : function(form) {
						if(!$("#officeSelect").val()){
							top.$.jBox.tip("请选择中心金库");
							return;
						}
						//loading('正在提交，请稍等...');
						form.submit();
					}
				});
				
				var dgStaff = $('#dgStaff').datagrid({ });
				var dgComm = $('#dgComm').datagrid({ });
				var dgBox = $('#dgBox').datagrid({ });
				var dgCar = $('#dgCar').datagrid({ });
				var dgLine = $('#dgLine').datagrid({ });
				
				//默认选中
				$("select[name='taskType'] option[value='${taskType }']").attr({selected:true}).change();
				$("select[name='officeId'] option[value='${officeId }']").attr({selected:true}).change();
				
				/* var lineList = dgLine.datagrid('getSelections');
				if(lineList.length > 0){
					alert("只能选择一条线路!");
					return;
				}  */
				
				$('a[rel=popover]').popover({
					  container:'body',
					  html: true,
					  trigger: 'hover',
					  placement: 'auto top',
					  content: function(){
						  return '<img src="'+$(this).data('img') + '" width="256px" height="256px"/>';
					  }
				});
				
				$("#submit").click( function(){
					var eventDetailList = dgStaff.datagrid('getSelections');
					var commissionerList = dgComm.datagrid('getSelections');
					var moneyBoxList = dgBox.datagrid('getSelections');
					var carList = dgCar.datagrid('getSelections');
					var lineList = dgLine.datagrid('getSelections');
					
					/* if(lineList.length > 0){
						alert(lineList[0].id);
						var dateStr=$("[name=dateStr]").val();
					    $.ajax({
						  type: 'POST',
						  url: "${ctx}/guard/coffer/connect", 
						  data: {
							  date:dateStr,
							  lineId:lineList[0].id
						  },
						  dataType: "json",
						  success: function(data){
							  
						  }
						});
						return;
					}  */
					
					if(eventDetailList.length == 0){
						alert("请选择押款员!");
						return;
					}
					
					if(commissionerList.length == 0){
						alert("请选择交接专员!");
						return;
					}
					if(moneyBoxList.length == 0){
						alert("请选择款箱!");
						return;
					}
					/* if(carList.length == 0){
						if(confirm("没有选择车辆! 是否强行执行?")){
						}else{
							return;
						}
					} */
					/* if(lineList.length == 0){
						alert("请选择线路!");
						return;
					} */
					
					var data = {};
					data.eventDetailList = eventDetailList;
					data.commissionerList = commissionerList;
					data.moneyBoxList = moneyBoxList;
					data.carList = carList;
					data.lineList = lineList;
					data.taskType=$("[name=taskType]").val();
					data.officeId = $("[name=officeId]").val();
					$.ajax({
				        type: "POST",
				        url: "${ctx}/guard/coffer/save",
				        data: $.customParam(data),
				        dataType: "json"
				    }).done(function(response) {
				    	if(response.result == 'SUCCESS'){
					    	top.$.jBox.tip('生成事件成功！');
					    	// window.location.reload();
				    	}else{
				    		top.$.jBox.tip(response.message);
				    	}
				    }).fail(function() {
				    	top.$.jBox.tip('提交至服务器失败，请检查网络连接！');
				    });
					
					
				});
				//$("#cofferConnectContainer").show();
				//closeLoading();
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
    
    function selectLineId(){
    	var line = $('#dgLine').datagrid('getSelected');
    	if(line){
    		$("#lineValue").val(line.id);
    	}
    }
</script>
</head>
<body>
	<div id='loadingDiv' style="position: absolute; z-index: 1000; top: 0px; left: 0px;   width: 100%; height: 100%; background: white; text-align: center;">  
	    <h1 style="top: 48%; position: relative;">  
	        <font color="#15428B">努力加载中···</font>  
	    </h1>  
	</div>  
  
	<form:form id="searchForm" method="post"
			class="form-inline"  action="${ctx}/guard/coffer/connect" cssStyle="margin:5px;" onsubmit="return selectLineId();">
		<input type="hidden" name="isFromSearchBtn" value="1"/>
		<div class="form-group">
			<label>中心金库：</label>
			<select  id ="officeSelect" name="officeId" class="form-control input-sm" style="min-width: 100px;">
				<c:forEach items="${zxjkOfficeList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label>事件日期：</label>
			<input name="date" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate" style="height:auto"
				value="${date}" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label><font class="red">*</font>任务类型：</label>
			<select  name="taskType" class="form-control input-sm">
				<option value="0">派送</option>
				<option value="1">回收</option>
				<option value="2">临时派送</option>
				<option value="3">临时回收</option>
				<option value="4">贵金属派送</option>
			</select>
		</div>
		<div class="form-group">
				<input id="lineValue"  type="hidden"  name="lineId"/>
		</div>
		<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>

		<div  class="form-group pull-right">
				<input id="submit" class="btn btn-success center-block" type="button" value="生成事件"/>
		</div>		
	</form:form>
		
	<div class="easyui-layout" id="layout">
       

		
        <div data-options="region:'west',split:true"  style="width:50%;">
        	<div class="easyui-layout" data-options="fit:true">
        	<div data-options="region:'north',split:true,title:'线路'"  style="height:25%;">
					<table id="dgLine"  data-options="singleSelect:true,collapsible:true" >
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'lineName'" width="50%">线路名</th>
									<th data-options="field:'area.name'" width="50%">区域</th>
									<th data-options="field:'id',hidden:true"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${lineList}" var="line">
									<tr>
									<td></td>
									<td>${line.lineName}</td>
									<td>${line.area.name}</td>
									<td>${line.id}</td>
									</tr>
								</c:forEach>
									
								</tbody>
						</table>
       		 	</div>
        		<div data-options="region:'center'" title="款箱<span>* (共${fn:length(moneyBoxEventDetailList)}条)</span>">
	            	<table id="dgBox"  data-options="singleSelect:false,collapsible:true" >
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'moneyBox.boxCode'" width="25%">款箱编码</th>
									<th data-options="field:'office.name'" width="25%">所属网点</th>
									<th data-options="field:'cardNum'" width="25%">款箱卡号</th>
									<th data-options="field:'time'" width="25%">时间</th>
									<th data-options="field:'id'">款箱序号</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${moneyBoxEventDetailList}" var="item">
								    <tr>
								    	<td></td>
									    <td>
							    			${item.moneyBox.boxCode}
									    </td>
									    <td>
							    			${item.moneyBox.office.name}
									    </td>
										<td>${item.cardNum}</td>
										<td>${item.time}</td>
										<td>${item.id}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				
			</div>
        </div>
         <div data-options="region:'center'" >
        	<div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north',split:true" title="押款员<span>*(共${fn:length(eventDetailList)}条)</span>" style="height:37%">
                	<table id="dgStaff"  data-options="singleSelect:false,collapsible:true" >
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'name'" width="20%">姓名</th>
									<th data-options="field:'fingerId'" width="20%">指纹号</th>
									<th data-options="field:'image1'" width="20%">人员照片</th>
									<th data-options="field:'image2'" width="20%">抓拍照</th>
									<th data-options="field:'time'" width="20%">时间</th>
									<th data-options="field:'id',hidden:true"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${eventDetailList}" var="c">
									<tr>
									<td></td>
									<td>${c.staff.name}</td>
									<td>${c.fingerNumLabel}</td>
									<td><a href="javascript:" rel="popover" data-img="${ctx}/guard/image/staff?id=${c.staff.staffImageList[0].id}">查看</a></td>
									<td><a  href="javascript:" rel="popover" data-img="${ctx}/guard/image?id=${c.id}">查看</a></td>
									<td>${c.time}</td>
									<td>${c.id}</td></tr>
								</c:forEach>
							</tbody>
						</table>
                </div>
                <div data-options="region:'center'" title="专员<span>*(共${fn:length(commissionerList)}条)</span>" style="height:37%">
                	<table id="dgComm"  data-options="singleSelect:false,collapsible:true" >
							<thead>
								<tr>	
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'name'" width="25%">姓名</th>
									<th data-options="field:'fingerId'" width="25%">指纹号</th>
									<th data-options="field:'image1'" width="25%">工作照</th>
									<th data-options="field:'time'" width="25%">时间</th>
									<th data-options="field:'id',hidden:true"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${commissionerList}" var="c">
									<tr>
									<td></td>
									<td>${c.staff.name}</td>
									<td>${c.fingerNumLabel}</td>
									<td><a rel="popover"  data-img="${ctx}/guard/image/staff?id=${c.staff.staffImageList[0].id}">查看</a></td>
									<td>${c.time}</td>
									<td>${c.id}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
                </div>
                <div data-options="region:'south',split:true,title:'车辆<span>*(共${fn:length(carList)}条)</span>'"  style="height:25%;">
                	<table id="dgCar"  data-options="singleSelect:true,collapsible:true" >
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'cardNum'" width="20%">车辆卡号</th>
									<th data-options="field:'carplate'" width="20%">车牌号</th>
									<th data-options="field:'admin'" width="20%">负责人</th>
									<th data-options="field:'image'" width="20%">照片</th>
									<th data-options="field:'time'" width="20%">时间</th>
									<th data-options="field:'id',hidden:true"></th>
									<th data-options="field:'equipmentId',hidden:true"></th>
									<th data-options="field:'equipSn',hidden:true"></th>
									<th data-options="field:'carId',hidden:true"></th>
									<th data-options="field:'taskId',hidden:true"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${carList}" var="carEvent">
									<tr>
									<td></td>
									<td>${carEvent.cardNum}</td>
									<td>${carEvent.carplate}</td>
									<td>${carEvent.admin}</td>
									<td><a rel="popover"  data-img="${ctx}/guard/image/car?id=${carEvent.carId}">查看</a></td>
									<td>${carEvent.time}</td>
									<td>${carEvent.id}</td>
									<td>${carEvent.equipmentId}</td>
									<td>${carEvent.equipSn}</td>
									<td>${carEvent.carId}</td>
									<td>${carEvent.taskId}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
                
       		 	</div>
            </div>
        </div>
        
    </div>
    </div>
    <script type="text/javascript">
		

		var leftWidth = 0; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize() {
			var strs = getWindowSize().toString().split(",");
			
			$("#layout").width(	strs[1] - 5);
			$("#layout").height(strs[0] - 65);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>