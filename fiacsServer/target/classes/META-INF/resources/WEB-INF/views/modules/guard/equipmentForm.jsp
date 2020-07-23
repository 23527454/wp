<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>设备信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$("#inputForm").submit(function () {
		var siteType=$("[name='siteType']").val();
		var accessType=$("#accessType").val();
		alert(siteType+"________"+accessType);
	});

	$(document).ready(

			function() {

				var id = $("#equipment_id").val();
				$("#equipment_ids").val(id);
				if (id != "" && typeof (id) != "undefined") {
					$("#officeButton").addClass("disabled");
				} else {
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
										//var siteType=$(".required valid").val();
										//var accessType=$("#accessType").val();
										//alert(siteType+"________"+accessType);
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


				$("#btnCopy").on('click',function () {
					$.post("${ctx}/guard/equipment/copy",$("#inputForm").serialize(),function (data) {
						alert("复制成功!");
					});
				});

				$("#btnPaste").on('click',function () {
					window.location.href="${ctx}/guard/equipment/paste?"+$("#inputForm").serialize();
				});

			});


	function AreaTreeselectCallBack(id, v, h, f) {
		if (v == "ok") {
			if (id != "" && typeof (id) != "undefined") {
				$("#equipment_ids").val(id);
			}
		}
		return true;
	}

	function officeTreeselectCallBack(nodes, v, h, f) {
		if(nodes && nodes.length != 0){
			var name = nodes[0].name;
			if (v == "ok") {
				if (name != "" && typeof (name) != "undefined") {
					$("#controlPos").val(name);
	
				}
			}
		}
		return true;
	}
	
	 // IP地址验证   
    jQuery.validator.addMethod("ip", function(value, element) {    
    	 var tel = /^(?:(?:1[0-9][0-9]\.)|(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))$/;
    	    return this.optional(element) || (tel.test(value));  
          }, "请填写正确的IP地址。");
	 
	 // 网关地址验证   
    jQuery.validator.addMethod("netGate", function(value, element) {    
    	 var tel = /^(?:(?:1[0-9][0-9]\.)|(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))$/;
    	    return this.optional(element) || (tel.test(value));  
          }, "请填写正确的网关。");
	 
	 // 子网掩码验证   
    jQuery.validator.addMethod("netMask", function(value, element) {    
    	 var tel = /^(?:(?:1[0-9][0-9]\.)|(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))$/;
    	    return this.optional(element) || (tel.test(value));  
          }, "请填写正确的子网掩码。");
	 
	 
  	//字母数字
    jQuery.validator.addMethod("alnum", function(value, element) {
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
    }, "只能包括英文字母和数字");



	function modifyAccessType(data){
		var siteType=$(data).val();
		var accessType=$("#accessType").html();
		if(siteType==0 || siteType==2){
			$("#accessType").html("<option value='0'>单门</option>");
		}else if(siteType==1){
			$("#accessType").html("<option value='1'>双门互锁</option><option value='2'>四门互锁</option>");
		}else{
			alert("不存在此项类型！");
			$("#accessType").html("");
		}
	}
</script>
</head>
<body>
	<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/equipment/">设备信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/equipment/form?id=${equipment.id}">设备信息<shiro:hasPermission
					name="guard:equipment:edit">${not empty equipment.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:equipment:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />

	<form:form id="inputForm" modelAttribute="equipment"
		action="${ctx}/guard/equipment/save" method="post"
		class="form-horizontal">

		<div class="row">
			<div style="margin-left:10px;margin-top:10px;">
				<shiro:hasPermission name="guard:equipment:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						   value="保 存"/>
				</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					   onclick="history.go(-1)" />
				<shiro:hasPermission name="guard:equipment:edit">
					<input id="btnCopy" class="btn btn-primary" type="button" style="margin-left: 5%"
						   value="复 制"/>&nbsp;
					<input id="btnPaste" class="btn btn-primary" type="button"
						   value="粘 贴"/>&nbsp;
				</shiro:hasPermission>
			</div>
		</div>
		<br/>
		<form:hidden path="id" id="equipment_id" />
		<sys:message content="${message}" />
		<form:input path="equipment_ids" htmlEscape="false" maxlength="64"
			class="form-control input-sm required" id="equipment_ids" type="hidden" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 网点名称：</label>
			<div class="col-xs-3">
				<sys:treeselect id="office" name="office.id"
					value="${equipment.office.id}" labelName="office.name"
					labelValue="${equipment.office.name}" title="网点"
					url="/sys/office/equipmentTreeData" cssClass="required"
					allowClear="true" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>序列号：</label>
			<div class="col-xs-3">
				<form:input path="serialNum" htmlEscape="false" maxlength="16"
					class="form-control input-sm required alnum" />
			</div>
			<form:errors path="serialNum" cssClass="error"></form:errors>
		</div>
		<%--<div class="form-group">
			<label class="control-label col-xs-4"><font class="red">*</font>人员类型：</label>

			<div class="radio-inline" style="margin-top:-8px;">
				<form:input path="staffType" id="radio_hidden" type="hidden" />
				<label class="control-label"><input type="radio" name="radio" value="0">押款员</label>
				<label class="control-label"><input type="radio" name="radio" value="1"> 交接员</label>
				<label class="control-label"><input type="radio" name="radio" value="2"> 维保员</label>
			</div>
		</div>--%>
		<div class="form-group">
			<label class="control-label col-xs-2">门控类型:</label>
			<div class="col-xs-6">
				<div class="radio-inline">
					<form:radiobuttons path="siteType" class="required"
									   items="${fns:getDictList('site_type')}"
									   itemLabel="label" itemValue="value" htmlEscape="false"
									   delimiter="&nbsp;&nbsp;&nbsp;" onchange="modifyAccessType(this)"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>互锁方式：</label>
			<div class="col-xs-2" style="width: 250px">
				<form:select path="accessType" class="form-control input-sm required">
					<form:options items="${fns:getDictList('equip_access_type')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				<form:errors path="siteType" cssClass="error"></form:errors>
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2"><font class="red">*</font>设备类型：</label>
			<div class="col-xs-2">
				<form:select path="equipType" class="form-control input-sm required"
					style="width:50%;">
					<form:options items="${fns:getDictList('equipType')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>子网掩码：</label>
			<div class="col-xs-3">
				<form:input path="netMask" htmlEscape="false" maxlength="32"
					class="form-control input-sm required netMask" />
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>网关：</label>
			<div class="col-xs-3">
				<form:input path="netGate" htmlEscape="false" maxlength="32"
					class="form-control input-sm required netGate" />
			</div>
		</div>

		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2"><font class="red">*</font>设备位置：</label>
			<div class="col-xs-3">
				<form:input path="controlPos" htmlEscape="false" maxlength="64"
					class="form-control input-sm required" id="controlPos" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>IP地址：</label>
			<div class="col-xs-3">
				<form:input path="ip" htmlEscape="false" maxlength="32"
							class="form-control input-sm required ip" id="ip"/>
			</div>
			<form:errors path="ip" for="ip" cssClass="error"></form:errors>
		</div>
		
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>端口号：</label>
			<div class="col-xs-3">
				<form:input path="port" htmlEscape="false"
					class="form-control input-sm required digits " />
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2"><font class="red">*</font>设备状态：</label>
			<div class="col-xs-2">
				<form:select path="status" class="form-control input-sm required"
					style="width:50%;">
					<form:options items="${fns:getDictList('sb_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>打印服务器IP地址：</label>
			<div class="col-xs-3">
				<form:input path="printServerIp" htmlEscape="false" maxlength="32"
					class="form-control input-sm required ip" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>打印服务器端口：</label>
			<div class="col-xs-3">
				<form:input path="printServerPort" htmlEscape="false"
					class="form-control input-sm required digits" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>中心上传IP地址：</label>
			<div class="col-xs-3">
				<form:input path="uploadEventSrvIp" htmlEscape="false"
					maxlength="32" class="form-control input-sm required ip" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>中心端口：</label>
			<div class="col-xs-3">
				<form:input path="uploadEventSrvPort" htmlEscape="false"
					class="form-control input-sm required digits" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注：</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="255" class="input-xxlarge " />
			</div>
		</div>
	</form:form>
	</div>
</body>
</html>