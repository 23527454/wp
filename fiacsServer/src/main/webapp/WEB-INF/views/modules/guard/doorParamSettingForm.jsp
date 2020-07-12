<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<link href="${ctxStatic}/jeDate/skin/jedate.css" rel="stylesheet">
<title>设备信息管理</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jeDate/src/jedate.js"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				var id = $("#equipment_id").val();
				$("#equipment_ids").val(id);
				if (id != "" && typeof (id) != "undefined") {
					$("#officeButton").addClass("disabled");
				}else{
// 					$("#ip").val("192.168.1.118"); 
					
					var office_id=parent.$("#office_id").val();
					var office_name=parent.$("#office_name").val();
					$("#controlPos").val(office_name);
					var office_type=parent.$("#office_type").val();
				
					if(office_id!=null && office_id!="" && office_id!="0" && typeof (office_id) != "undefined"){
						$.ajax({
					        type: "GET",
					        url: "${ctx}/guard/equipment/getNumber?id="+office_id
					    }).done(function(response) {
							var data = JSON.parse(response);
							var data_number= data.number;
							if(data_number=="0"){
								$("#officeName").val(office_name);
								$("#controlPos").val(office_name);
								//网点ID
								$("#equipment_ids").val(office_id);
								$("#officeId").val(office_id);
							}
					    }).fail(function() {
					    });
					}
			}

				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
								    rules:{
								    	serialNum:{
					                        rangelength:[16,16]
					                    },
					                    port:{
											range:[1,65535]
										},
										printServerPort:{
											range:[1,65535]
										},
										uploadEventSrvPort:{
											range:[1,65535]
										}
					                },
									messages : {
										serialNum:{
						                        rangelength: $.format("序列号长度必须为16位。")
						                    },
						                    port:{
						                    	 rangelength: $.format("端口号大小为1~65535。")
											},
											printServerPort:{
						                    	 rangelength: $.format("打印服务器端口大小为1~65535。")
											},
											uploadEventSrvPort:{
						                    	 rangelength: $.format("中心端口大小为1~65535。")
											}
									}
								});
				
				
				
			});
	
	
	
	function addRow(list, idx, tpl, row,type){
		var idxo=idx+1;
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
		
		if(type){
			jeDate("#doorTimeZonesList"+idx+"_timeFrameStart1",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameStart2",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameStart3",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameStart4",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameEnd1",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameEnd2",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameEnd3",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			jeDate("#doorTimeZonesList"+idx+"_timeFrameEnd4",{
		        minDate:"00:00",              //最小日期
		        maxDate:"23:59",              //最大日期
		        format: "hh:mm"
		    });
			$("#doorTimeZonesList"+idx+"_equipmentId").val($('#equipment_id').val());
			$("#doorTimeZonesList"+idx+"_equipSn").val($('#equipSn').val());
			
			$("#doorTimeZonesList"+idx+"_doorPos1 option[value='" + row.doorPos + "']").attr("selected", true);
			
			$("#doorTimeZonesList"+idx+"_weekNumber1 option[value='" + row.weekNumber + "']").attr("selected", true);
		}else{
			$("#accessParametersList"+idx+"_equipmentId").val($('#equipment_id').val());
			$("#accessParametersList"+idx+"_equipSn").val($('#equipSn').val());
			
			$("#accessParametersList"+idx+"_doorPos option[value='" + row.doorPos + "']").attr("selected", true);
			
			$("#accessParametersList"+idx+"_Centerpermit option[value='" + row.centerPermit + "']").attr("selected", true);
			$("#accessParametersList"+idx+"_opentype option[value='" + row.openType + "']").attr("selected", true);
			
			
		}
	}
	
	function delRow(prefix){
		var id = $(prefix+"_id");
		var delFlag = $(prefix+"_delFlag");
		if (id.val() == ""){
			$(prefix).remove();
		}else if(delFlag.val() == "0"){
			delFlag.val("1");
			//$(obj).html("&divide;").attr("title", "撤销删除");
			//$(obj).parent().parent().addClass("error");
			$(prefix).hide();
		}
	}
	
	function addDoorRow(){
		//alert($('#doorPosSelect').val());
		var xuanze = $('#doorPosSelect').val();
		var timeZone = $('#timeZoneSelect').val();
		console.log(xuanze);
		for(var i=0;i<doorTimeZonesRowIdx;i++){
			var selected = $("#doorTimeZonesList"+i+"_doorPos").val();
			var timeZoneNumber = $("#doorTimeZonesList"+i+"_timeZoneNumber").val();
			var delflag = $("#doorTimeZonesList"+i+"_delFlag").val();
			if(selected==xuanze&&timeZone==timeZoneNumber&&delflag=="0"){
				alert('次时区,已存在此门的定时设置,请点击查询');
				return;
			}else{
				$("#doorTimeZonesList"+i).hide();
			}
		}
		
		
		for(var i=1;i<8;i++){
			var row = {
					doorPos : xuanze,
					weekNumber : i,
					timeZoneNumber : timeZone,
					timeFrameStart1 : '00:00',
					timeFrameStart2 : '00:00',
					timeFrameStart3 : '00:00',
					timeFrameStart4 : '00:00',
					timeFrameEnd1 : '24:00',
					timeFrameEnd2 : '00:00',
					timeFrameEnd3 : '00:00',
					timeFrameEnd4 : '00:00'
			};
			addRow('#doorTimeZonesList', doorTimeZonesRowIdx, doorTimeZoneTpl, row,1);
			doorTimeZonesRowIdx = doorTimeZonesRowIdx + 1;
		}
	}
	 
	function deleteDoorRow(){
		var xuanze = $('#doorPosSelect').val();
		var timeZone = $('#timeZoneSelect').val();
		for(var i=0;i<doorTimeZonesRowIdx;i++){
			var selected = $("#doorTimeZonesList"+i+"_doorPos").val();
			var timeZoneNumber = $("#doorTimeZonesList"+i+"_timeZoneNumber").val();
			if(selected==xuanze&&timeZone==timeZoneNumber){
				delRow("#doorTimeZonesList"+i);
			}
		}
	}
	
	function queryDoorRow(){
		var xuanze = $('#doorPosSelect').val();
		var timeZone = $('#timeZoneSelect').val();
		var flag = true;
		for(var i=0;i<doorTimeZonesRowIdx;i++){
			var selected = $("#doorTimeZonesList"+i+"_doorPos").val();
			var timeZoneNumber = $("#doorTimeZonesList"+i+"_timeZoneNumber").val();
			var delflag = $("#doorTimeZonesList"+i+"_delFlag").val();
			if(selected==xuanze&&timeZone==timeZoneNumber&&delflag=="0"){
				flag=false;
				$("#doorTimeZonesList"+i).show();
			}else{
				$("#doorTimeZonesList"+i).hide();
			}
		}
		if(flag){
			alert('此时区,该门号没有设置');
		}
	}
	
	function parametersDownload(prefix,typeValue){
		loading('正在同步，请稍等...');
		var rowId = $(prefix+"_id").val();
		if(rowId){
			if("parameters"==typeValue){
				$.ajax({
			        type: "GET",
			        url: "${ctx}/guard/equipment/parametersDownload",
			        data : {
			        	type:typeValue,
			        	equipmentId :  $(prefix+"_equipmentId").val(),
			        	alarmTime :$(prefix+"_Alarmtime").val(),
			        	centerPermit:$(prefix+"_Centerpermit").val(),
			        	combinationNumber:$(prefix+"_Combinationnumber").val(),
			       		delayCloseTime:$(prefix+"_delayCloseTime").val(),
			    		doorPos:$(prefix+"_doorPos").val(),
        				equipSn:$(prefix+"_equipSn").val(),
        				relayActionTime:$(prefix+"_relayActiontime").val(),
        				timeZoneNumber:$(prefix+"_TimeZonenumber").val(),
        				openType:$(prefix+"_opentype").val(),
        				id:$(prefix+"_id").val()
			        }
			    }).done(function(response) {
			    	closeLoading();
			    	if(response=='success'){
						alert('同步成功');
			    	}else{
			    		alert('同步失败');
			    	}
			    }).fail(function() {
			    	closeLoading();
			    });
			}else{
				$.ajax({
			        type: "GET",
			        url: "${ctx}/guard/equipment/parametersDownload",
			        data : {
			        	type:typeValue,
			        	equipmentId :  $(prefix+"_equipmentId").val(),
			    		doorPos:$(prefix+"_doorPos").val(),
        				equipSn:$(prefix+"_equipSn").val(),
        				timeZoneNumber:$(prefix+"_timeZoneNumber").val(),
        				id:$(prefix+"_id").val(),
        				weekNumber : $(prefix+"_weekNumber").val(),
        				timeFrameStart1:$(prefix+"_timeFrameStart1").val(),
        				timeFrameStart2:$(prefix+"_timeFrameStart2").val(),
        				timeFrameStart3:$(prefix+"_timeFrameStart3").val(),
        				timeFrameStart4:$(prefix+"_timeFrameStart4").val(),
        				timeFrameEnd1:$(prefix+"_timeFrameEnd1").val(),
        				timeFrameEnd2:$(prefix+"_timeFrameEnd2").val(),
        				timeFrameEnd3:$(prefix+"_timeFrameEnd3").val(),
        				timeFrameEnd4:$(prefix+"_timeFrameEnd4").val()
			        }
			    }).done(function(response) {
			    	closeLoading();
			    	if(response=='success'){
						alert('同步成功');
			    	}else{
			    		alert('同步失败');
			    	}
			    }).fail(function() {
			    	closeLoading();
			    });
			}
		}else{
			closeLoading();
			alert('新增的行，请点击保存进行同步！');
		}
	}
</script>
</head>
<body>
	<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/equipment/">设备信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/equipment/paramSetting?id=${equipment.id}">门禁参数设置</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="equipment"
		action="${ctx}/guard/equipment/saveSetting" method="post"
		class="form-horizontal">
		<form:hidden path="id" id="equipment_id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 网点名称：</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="office.id"
					value="${equipment.office.id}" labelName="office.name"
					labelValue="${equipment.office.name}" title="网点"
					url="/sys/office/equipmentTreeData" cssClass="required"
					allowClear="false" />
			</div>
			
			<label class="control-label col-xs-2"><font class="red">*</font>序列号：</label>
			<div class="col-xs-3">
				<form:input path="serialNum" htmlEscape="false" maxlength="16" id="equipSn"
					class="form-control input-sm required alnum" readOnly="true"/>
			</div>
		</div>
		<!-- <div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>序列号：</label>
			<div class="col-xs-3">
				<form:input path="serialNum" htmlEscape="false" maxlength="16" id="equipSn"
					class="form-control input-sm required alnum" readOnly="true"/>
			</div>
			<form:errors path="serialNum" cssClass="error"></form:errors>
		</div> -->
		
		<div class="form-group">
			<label class="control-label col-xs-2">参数设置：</label>
			<div class="col-xs-7">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th style="width: 150px">&nbsp;&nbsp;门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;</th>
							<th style="width: 150px">继电器开门时间</th>
							<th style="width: 150px">延时报警时间</th>
							<th style="width: 150px">启动报警时间</th>
							<th style="width: 150px">&nbsp;时&nbsp;&nbsp;区&nbsp;&nbsp;号&nbsp;</th>
							<th style="width: 150px">组合开门人数数量</th>
							<th style="width: 150px">中心管控开门</th>
							<th style="width: 150px">开&nbsp;&nbsp;&nbsp;&nbsp;门&nbsp;&nbsp;&nbsp;&nbsp;方&nbsp;&nbsp;&nbsp;&nbsp;式</th>
							 <shiro:hasPermission name="guard:equipment:edit">
								<th style="width: 37px;">操作</th>
							</shiro:hasPermission> 
						</tr>
					</thead>
					<tbody id="accessParametersList">
					</tbody>
					<shiro:hasPermission name="guard:equipment:edit">
						<tfoot id="accessParametersTfoot" style="display: none">
							<tr>
								<td colspan="8"><a href="javascript:"
									onclick="addRow('#accessParametersList', accessParametersRowIdx, accessParametersTpl);accessParametersRowIdx = accessParametersRowIdx + 1;"
									class="btn">新增</a></td>
							</tr>
						</tfoot>
					</shiro:hasPermission>
				</table>
				<script type="text/template" id="accessParametersTpl">//<!--
						<tr id="accessParametersList{{idx}}">
							<td class="hide">
								<input id="accessParametersList{{idx}}_id" name="accessParametersList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="accessParametersList{{idx}}_delFlag" name="accessParametersList[{{idx}}].delFlag" type="hidden" value="0"/>
								<input id="accessParametersList{{idx}}_equipmentId" name="accessParametersList[{{idx}}].equipmentId" type="hidden"/>
	                            <input id="accessParametersList{{idx}}_equipSn" name="accessParametersList[{{idx}}].equipSn" type="hidden"/>
							</td>
							<td>
								<select id="accessParametersList{{idx}}_doorPos" name="accessParametersList[{{idx}}].doorPos" disabled value="{{row.doorPos}}" class="form-control input-sm">
									<c:forEach items="${fns:getDictList('door_pos')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="accessParametersList{{idx}}_relayActiontime" name="accessParametersList[{{idx}}].relayActionTime" value="{{row.relayActionTime}}" type="text" placeholder="必填" class="form-control input-sm required" />
							</td>
							<td>
								<input id="accessParametersList{{idx}}_delayCloseTime" name="accessParametersList[{{idx}}].delayCloseTime" value="{{row.delayCloseTime}}" type="text" placeholder="必填" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="accessParametersList{{idx}}_Alarmtime" name="accessParametersList[{{idx}}].alarmTime" value="{{row.alarmTime}}" type="text" placeholder="必填" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="accessParametersList{{idx}}_TimeZonenumber" name="accessParametersList[{{idx}}].timeZoneNumber" value="{{row.timeZoneNumber}}" type="text" placeholder="必填" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="accessParametersList{{idx}}_Combinationnumber" name="accessParametersList[{{idx}}].combinationNumber" value="{{row.combinationNumber}}" type="text" placeholder="必填" class="form-control input-sm required"/>
							</td>
							<td>
								<select id="accessParametersList{{idx}}_Centerpermit" name="accessParametersList[{{idx}}].centerPermit" data-value="{{row.centerPermit}}" class="form-control input-sm">
									<c:forEach items="${fns:getDictList('door_center_permit')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
                                <select id="accessParametersList{{idx}}_opentype" name="accessParametersList[{{idx}}].openType" data-value="{{row.openType}}" class="form-control input-sm">
                                    <c:forEach items="${fns:getDictList('door_open_type')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
							<td class="text-center" width="60">
							<shiro:hasPermission name="guard:equipment:edit">
								<a href="javascript:;" onclick="parametersDownload('#accessParametersList{{idx}}','parameters');">同步</a>
							</shiro:hasPermission>
							</td>

						</tr>//-->
					</script>
					<!-- <td class="text-center" width="10">
								<shiro:hasPermission name="guard:equipment:edit">
									{{#delBtn}}<span class="btn close" onclick="delRow('#accessParametersList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
								</shiro:hasPermission>
							</td> -->
				<script type="text/javascript">
						var accessParametersRowIdx = 0, accessParametersTpl = $("#accessParametersTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(equipment.accessParametersList)};
							for (var i=0; i<data.length; i++){
								addRow('#accessParametersList', accessParametersRowIdx, accessParametersTpl, data[i]);
								accessParametersRowIdx = accessParametersRowIdx + 1;
							}
							
						});
					</script>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">定时设置：</label>
			<div class="col-xs-7">
			<label>门号：</label>
			<select id="doorPosSelect" name="doorPosSelect" class="form-control input-sm" style="width:120px;">
									<c:forEach items="${fns:getDictList('door_pos')}" var="dict">
									<c:choose>
										<c:when test="${equipment.accessType==0}">
											<c:if test="${dict.value==1 || dict.value==2}">
												<option value="${dict.value}">${dict.label}</option>
											</c:if>
										</c:when>
										<c:otherwise>
										<c:if test="${dict.value!=0}">
										<option value="${dict.value}">${dict.label}</option>
										</c:if>
										</c:otherwise>
									</c:choose>
										
									</c:forEach>
								</select>
							<label>时区号：</label>
						<select id="timeZoneSelect" name="timeZoneSelect" class="form-control input-sm" style="width:120px;">
						<option value="1">1
						</option>
						<option value="2">2
						</option>
						<option value="3">3
						</option>
						<option value="4">4
						</option>
						</select>		
								<input id="queryDoorZone" class="btn btn-primary" type="button" onclick="queryDoorRow()"
						value="查询" />
								<input id="addDoorZone" class="btn btn-primary" type="button" onclick="addDoorRow()"
						value="新增" />
						
						<input id="deleteDoorZone" class="btn btn-primary" type="button" onclick="deleteDoorRow()"
						value="删除" />
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th style="width: 150px">&nbsp;&nbsp;门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;</th>
							<th style="width: 150px">&nbsp;时&nbsp;&nbsp;区&nbsp;&nbsp;号&nbsp;</th>
							<th style="width: 150px">&nbsp;&nbsp;周&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;几&nbsp;&nbsp;</th>
							<th style="width: 150px">1开始时间</th>
							<th style="width: 150px">1结束时间</th>
							<th style="width: 150px">2开始时间</th>
							<th style="width: 150px">2结束时间</th>
							<th style="width: 150px">3开始时间</th>
							<th style="width: 150px">3结束时间</th>
							<th style="width: 150px">4开始时间</th>
							<th style="width: 150px">4结束时间</th>
							 <shiro:hasPermission name="guard:equipment:edit">
								<th style="width: 80px;">操作</th>
							</shiro:hasPermission> 
						</tr>
					</thead>
					<tbody id="doorTimeZonesList">
					</tbody>
					<shiro:hasPermission name="guard:equipment:edit">
						<tfoot id="doorTimeZoneTfoot" style="display:none;">
							<tr>
								<td colspan="12"><a href="javascript:"
									onclick="addRow('#doorTimeZonesList', doorTimeZonesRowIdx, doorTimeZoneTpl,null,1);doorTimeZonesRowIdx = doorTimeZonesRowIdx + 1;"
									class="btn">新增</a></td>
							</tr>
						</tfoot>
					</shiro:hasPermission>
				</table>
				<script type="text/template" id="doorTimeZoneTpl">//<!--
						<tr id="doorTimeZonesList{{idx}}">
							<td class="hide">
								<input id="doorTimeZonesList{{idx}}_id" name="doorTimeZonesList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="doorTimeZonesList{{idx}}_delFlag" name="doorTimeZonesList[{{idx}}].delFlag" type="hidden" value="0"/>
    <input id="doorTimeZonesList{{idx}}_equipmentId" name="doorTimeZonesList[{{idx}}].equipmentId" type="hidden"/>
	                            <input id="doorTimeZonesList{{idx}}_equipSn" name="doorTimeZonesList[{{idx}}].equipSn" type="hidden"/>
								<input id="doorTimeZonesList{{idx}}_doorPos" name="doorTimeZonesList[{{idx}}].doorPos" type="hidden" value="{{row.doorPos}}"/>	
								<input id="doorTimeZonesList{{idx}}_weekNumber" name="doorTimeZonesList[{{idx}}].weekNumber" type="hidden" value="{{row.weekNumber}}"/>	
							</td>
							<td>
								<select id="doorTimeZonesList{{idx}}_doorPos1" name="doorTimeZonesList[{{idx}}].doorPos1" disabled data-value="{{row.doorPos}}" class="form-control input-sm">
									<c:forEach items="${fns:getDictList('door_pos')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeZoneNumber" name="doorTimeZonesList[{{idx}}].timeZoneNumber" value="{{row.timeZoneNumber}}" ReadOnly=true type="text" placeholder="必填" class="form-control input-sm required" />
							</td>
							<td>
								<select id="doorTimeZonesList{{idx}}_weekNumber1" name="doorTimeZonesList[{{idx}}].weekNumber1" data-value="{{row.weekNumber}}" disabled  class="form-control input-sm">
									<c:forEach items="${fns:getDictList('week_number')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameStart1" name="doorTimeZonesList[{{idx}}].timeFrameStart1" type="text" ReadOnly=true value="{{row.timeFrameStart1}}" maxLength="5" placeholder="必选"  class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameEnd1" name="doorTimeZonesList[{{idx}}].timeFrameEnd1" type="text" value="{{row.timeFrameEnd1}}" ReadOnly=true maxLength="5" placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameStart2" name="doorTimeZonesList[{{idx}}].timeFrameStart2" type="text" value="{{row.timeFrameStart2}}" ReadOnly=true maxLength="5" placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameEnd2" name="doorTimeZonesList[{{idx}}].timeFrameEnd2" type="text" value="{{row.timeFrameEnd2}}" ReadOnly=true maxLength="5" placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameStart3" name="doorTimeZonesList[{{idx}}].timeFrameStart3" type="text" value="{{row.timeFrameStart3}}" ReadOnly=true maxLength="5" placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameEnd3" name="doorTimeZonesList[{{idx}}].timeFrameEnd3" type="text" value="{{row.timeFrameEnd3}}" maxLength="5" ReadOnly=true placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameStart4" name="doorTimeZonesList[{{idx}}].timeFrameStart4" type="text" value="{{row.timeFrameStart4}}" ReadOnly=true maxLength="5" placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td>
								<input id="doorTimeZonesList{{idx}}_timeFrameEnd4" name="doorTimeZonesList[{{idx}}].timeFrameEnd4" type="text" value="{{row.timeFrameEnd4}}" maxLength="5" ReadOnly=true placeholder="必选" class="form-control input-sm required"/>
							</td>
							<td class="text-center" width="60">
							<shiro:hasPermission name="guard:equipment:edit">
								<a href="javascript:;" onclick="parametersDownload('#doorTimeZonesList{{idx}}','doorTimeZone');">同步</a>
							</shiro:hasPermission>
							</td>

						</tr>//-->
					</script>
					<!-- <td class="text-center" width="10">
								<shiro:hasPermission name="guard:equipment:edit">
									{{#delBtn}}<span class="btn close" onclick="delRow('#doorTimeZonesList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
								</shiro:hasPermission>
							</td> -->
				<script type="text/javascript">
						var doorTimeZonesRowIdx = 0, doorTimeZoneTpl = $("#doorTimeZoneTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(equipment.doorTimeZonesList)};
							for (var i=0; i<data.length; i++){
								addRow('#doorTimeZonesList', doorTimeZonesRowIdx, doorTimeZoneTpl, data[i],1);
								if(i>6){
									$('#doorTimeZonesList'+i).hide();
								}
								doorTimeZonesRowIdx = doorTimeZonesRowIdx + 1;
							}
						});
						
					</script>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:equipment:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<a id="btnCancel" href="${ctx}/guard/equipment/" class="btn btn-default" >返回</a>
			</div>
		</div>
	</form:form>
	</div>
</body>
</html>