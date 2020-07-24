 <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员信息管理</title>
<meta name="decorator" content="default" />
<meta http-equiv="Pragma" content="no-cache">  
<meta http-equiv="Cache-Control" content="no-cache">  
<meta http-equiv="Expires" content="0">  
<link href="${ctxStatic}/custom/bootstrap-wizard/bootstrap-wizard.css"
	rel="stylesheet" />
<link href="${ctxStatic}/custom/chosenNew/chosen.css" rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/custom/chosenNew/chosen.jquery.js'></script>
<script src="${ctxStatic}/custom/bootstrap-wizard/bootstrap-wizard.js"></script>
<script src="${ctxStatic}/custom/staffForm.js"></script>
<script src="${ctxStatic}/base64/base64.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/fileinput.js'></script>
<script type="text/javascript" src="${ctxStatic}/custom/spectrum.js"></script>
<script type='text/javascript' src='${ctxStatic}/custom/docs.js'></script>
<script type='text/javascript' src='${ctxStatic}/custom/toc.js'></script>
<script type='text/javascript' src='${ctxStatic}/webcam/webcam.min.js'></script>
<style type="text/css">
. {
	width: 50px;
}
#officeName{
    width:118px;
}
 #webcam { border: 1px solid #666666; width: 320px; height: 240px; }
 #photoscam { border: 1px solid #666666; width: 320px; height: 240px; }
 .btncam { width: 100px; height: auto; margin: 5px 20px; }
 .btncam input[type=button] { width: 80px; height: 50px; line-height: 50px; margin: 3px; }
</style>
<script type="text/javascript">
    var camRowId=0;
    var camdata_uri;
    //拍照
    function savePhoto(){
         Webcam.snap( function(data_uri) {
        $('#imgcam').attr('src',data_uri);
        camdata_uri=data_uri;
       } );
    }

    function ensurePhoto(){
        if(!camdata_uri){
          alert("请先拍照！");
          return;
        }
        $.ajax({
          type: "post",
          url: "/web/webCamServlet?t="+new Date().getTime(),
          data: {type: "pixel", image: camdata_uri},
          dataType: "html",
          success: function(data){
                     pos = 0;
                     $("#imgcam").attr("src", "");
                     $("#avatarImage"+camRowId+"_file").attr("src", "${ctx}/guard/image/webCamStaff?filePath="+data);
                     $("#staffImageList"+camRowId+"_imagePath").val(data);
                     $('#webCamModal').modal("hide");
                     Webcam.reset();
              }
         });
    }
</script>
<script type="text/javascript">

var photosNumber = 1;
var familyNumber = 1;
var warkNumber = 1;
	$(document).ready(function() {
		$("[name=radio]").click(function(){
			
			var value = $("[name=radio]:checked").val();
			if (value == 0) {
				$("#radio_hidden").val(0);
			} else if (value == 1) {
				$("#radio_hidden").val(1);
				//隐藏机构
				$("#office_div").show();
				$("#company_div").hide();
				$("#officeName").removeClass("ignore");
				$("#companyName").addClass("ignore");
			} else if (value == 2) {
				$("#radio_hidden").val(2);
			}
			if(value == 0 || value == 2){
				// 隐藏网点
				$("#company_div").show();
				$("#office_div").hide();
				$("#companyName").removeClass("ignore");
				$("#officeName").addClass("ignore");
			}
			if($("#oldstafftype").val()==value){
				
				onAreaChanged($('#areaId').val(),$("#FPnoOne_FingerNum_hidden").val(),value,1);
			} else{
				
				onAreaChanged($('#areaId').val(),null,value,1);
			} 
		});
		$("#fingerCodeOne").val(atob($("#fingerCodeOne").val()));
		$("#fingerCodeTwo").val(atob($("#fingerCodeTwo").val()));
		//add 2019-9-21
		$("#fingerCodeThree").val(atob($("#fingerCodeThree").val()));
		$("#oldstafftype").val($("#radio_hidden").val());
		var fingerCodeOne=$("#fingerCodeOne").val();
		var fingerCodeTwo=$("#fingerCodeTwo").val();
		var fingerCodeThree=$("#fingerCodeThree").val();
		var FPnoOne=$("#FPnoOne").val();

		if(fingerCodeOne != "" &&  typeof(fingerCodeOne)!="undefined" ){
		$("#fingerCode3").val(fingerCodeOne);
		$("#fingerCode4").val(fingerCodeTwo);
		}
		$("#FPno").val(FPnoOne);
	
		if(FPnoOne!="" && typeof(FPnoOne)!="undefined"&&FPnoOne!=null) {
	
			$("#areaButton").addClass("disabled");
			$("#select").attr("disabled", true); 
			}
		var WorkList=$("#staffExWorkList0").val();
		var ExFamilyList=$("#staffExFamilyList0").val();
		var ImageList=$("#staffImageList0").val();
		if(WorkList!="" && typeof(WorkList)!="undefined") {
			document.getElementById("warkResume").style.display="";
			$("#warkResumeB").val("隐藏工作简历");
			warkNumber++;
			}
		
		if(ExFamilyList!="" && typeof(ExFamilyList)!="undefined") {
			document.getElementById("family").style.display="";
			$("#familyB").val("隐藏家庭成员");
			familyNumber++;
			}
		
		if(ImageList!="" && typeof(ImageList)!="undefined") {
			document.getElementById("photos").style.display="";
			$("#photosB").val("隐藏人员照片");
			photosNumber++;
		}
		
		if (document.all['radio_hidden'].value == '0') {
			document.all['radio'][0].checked = true;
		} else if (document.all['radio_hidden'].value == '1') {
			document.all['radio'][1].checked = true;
		} else if (document.all['radio_hidden'].value == '2') {
			document.all['radio'][2].checked = true;
		}else {
			document.all['radio'][0].checked = true;
			$("#radio_hidden").val("0");
		}

		var zt_id = parent.$("#id").val();
		var zt_name = parent.$("#name").val();
		var zt_status = parent.$("#status").val();

		if(zt_status=="office"){
			$("#selectedOfficeId").val(zt_id);
		}else if(zt_status=="company"){
			$("#selectedCompanyId").val(zt_id);
		}
		
		var nameUser= $("#name").val();
		
		if(nameUser== "" || typeof(fingerCodeOne)=="undefined"){
			if(zt_id !="" &&  typeof (zt_id) != "undefined"){
				if(zt_status=="office"){
					//显示网点
					document.getElementById("office_div").style.display="";
					document.getElementById("company_div").style.display="none";
					
					document.all['radio'][1].checked = true;
					document.all['radio'][0].disabled=true;
					document.all['radio'][2].disabled=true;
					$("#radio_hidden").val("1");			
					$.ajax({
				        type: "GET",
				        url: "${ctx}/sys/office/getArea?id="+zt_id
				    }).done(function(response) {
						var data = JSON.parse(response);
						var data_areaId= data.areaId;
						$("#infoAreaID").val(data_areaId);
						$("#areaId").val(data_areaId);
						onAreaChanged(data_areaId,null,$("#radio_hidden").val());
				    });
				}else if(zt_status=="company"){									
					document.all['radio'][1].disabled=true;
					// 隐藏网点
					document.getElementById("company_div").style.display="";
					document.getElementById("office_div").style.display="none";
					$.ajax({
				        type: "GET",
				        url: "${ctx}/guard/company/getArea?id="+zt_id
				    }).done(function(response) {
						var data = JSON.parse(response);
						var data_areaId= data.areaId;
						$("#infoAreaID").val(data_areaId);
						$("#areaId").val(data_areaId);
						onAreaChanged(data_areaId,null,$("#radio_hidden").val());
				    });
				}
			}
		}else{
			var officeId = '${staff.office.id}';
			if(officeId){
				document.all['radio'][1].checked = true;
				document.all['radio'][0].disabled=true;
				document.all['radio'][2].disabled=true;
				$.ajax({
			        type: "GET",
			        url: "${ctx}/sys/office/getArea?id="+officeId
			    }).done(function(response) {
					var data = JSON.parse(response);
					var data_areaId= data.areaId;
					$("#infoAreaID").val(data_areaId);
					$("#areaId").val(data_areaId);
					onAreaChanged(data_areaId, $("#FPnoOne_FingerNum_hidden").val(),$("#radio_hidden").val());
			    });
			}else{
				var companyId = '${staff.company.id}';
/* 				var companyId = '${staff.company.area.id}'; */
				document.all['radio'][1].disabled=true;
				$.ajax({
			        type: "GET",
			        url: "${ctx}/guard/company/getArea?id="+companyId
			    }).done(function(response) {
					var data = JSON.parse(response);
					var data_areaId= data.areaId;
					$("#infoAreaID").val(data_areaId);
					$("#areaId").val(data_areaId);
					/* onAreaChanged(data_areaId, $("#FPnoOne_FingerNum_hidden").val()); */
					 onAreaChanged(data_areaId, $("#FPnoOne_FingerNum_hidden").val(),$("#radio_hidden").val()); 
			    });
			}
			
		}
		

		var se=$("#radio_hidden").val();
		if(se=="0" || se=="2") {
			// 隐藏所属机构ID
			$("#company_div").show();
			$("#office_div").hide();
			$("#companyName").removeClass("ignore");
			$("#officeName").addClass("ignore");
		}else{
			//隐藏网点
			$("#office_div").show();
			$("#company_div").hide();
			$("#officeName").removeClass("ignore");
			$("#companyName").addClass("ignore");
		}
			
		$("#inputForm").validate({
			ignore: ".ignore",
			submitHandler : function(form) {
				if($("#staffImageList tr").length == 0){
					top.$.jBox.tip("至少需要上传1张人员照片");
					return;
				}
				//维保员需要身份证照片和推荐信照片 add 2019-9-17
				<%--if(se=="2"){
                    if($("#staffImageList tr").length <3){
                        top.$.jBox.tip("维保员至少需要上传1张照片，2张证件照片(身份证，推荐信)");
                        return;
                    }
				}--%>
				loading('正在提交，请稍等...');
				form.submit();
			}
		});
		
		$("#FPnoOne").change(function(){
			$("#FPno").val($(this).find("option:selected").val());
			$('#workNum').val((Number($("[name=radio]:checked").val())+1)+"1"+$("#FPnoOne").find("option:selected").val());
		});
		
		$("#startDate").blur(function(){
			var startDate = $("#startDate").val();
			var yearLater = $("#yearLater").val();
			var year = startDate.split("-");
			
			$("#endDate").val((parseInt(year)+parseInt(yearLater))+"-"+year[1]+"-"+year[2])
		});



		$("#btnCopy").on('click',function () {
			window.location.href="${ctx}/guard/staff/copy?"+$("#inputForm").serialize();
		});

		$("#btnPaste").on('click',function () {
			window.location.href="${ctx}/guard/staff/paste?"+$("#inputForm").serialize();
		});
		
	});
	
	function fingerNumWait(){
		
		$("#FPnoOne").html('');
		
		$("#FPnoOne").append("<option value='生成中'>生成中</option>");
		//	$("#fselect").append("<option value='"+item.value+"'>"+item.label+"</option>");
		$("#FPnoOne").eq(0).attr({selected:true});
		
	}
	function onAreaChanged(data_areaId, fingerNum,staffType,operate){
		$("#infoAreaID").val(data_areaId);
		if(!data_areaId){
			return;
		}
		if($("#oldareaid").val()&&$("#oldareaid").val()==data_areaId&&!operate){
			return;
		}
		$("#oldareaid").val(data_areaId);
		if(!fingerNum){
			fingerNum = '';
		}
		fingerNumWait();
		var getTimestamp=new Date().getTime();
		$.ajax({
	        type: "GET",
	        url: "${ctx}/guard/staff/listAvailableFingers?id="+data_areaId +"&fingerNum="+fingerNum+"&staffType="+staffType+"&time="+getTimestamp
	    }).done(function(data) {
	    	var value = "";
			$("#FPnoOne").html('');
			$.each(data, function(idx, item){
				$("#FPnoOne").append("<option value='"+item.value+"'>"+item.label+"</option>");
			});
			if(fingerNum){
				$("#FPnoOne").find("option[value='"+fingerNum+"']").prop("selected",true);
				fingerNum1 = $("#FPnoOne").find("option:selected").text();
				fingerNum12 = $("#FPnoOne").find("option:selected").val();
				$("#FPnoOne").prepend("<option value='"+fingerNum12+"'>"+fingerNum1+"</option>");
				$("#FPnoOne").find("option[value='"+fingerNum+"']").prop("selected",true);
				$("#FPnoOne").find('option').eq(0).remove();

			}else{
				$("#FPnoOne").eq(0).attr({selected:true});
			}
            $('#workNum').val((Number($("[name=radio]:checked").val())+1)+"1"+$("#FPnoOne").find("option:selected").val());
		});
	}
	
	function getFingerNum(){
		//return Number($("#FPnoOne").find("option:selected").val()%1000);
		return 500;
	}
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function photosButton(){
		if(photosNumber%2==0){
			document.getElementById("photos").style.display="none";
			$("#photosB").val("人员照片");
		}else{
			document.getElementById("photos").style.display="";
			$("#photosB").val("隐藏人员照片");
		}
		photosNumber++;
		}
	
	function familyButton(){
		if(familyNumber%2==0){
			document.getElementById("family").style.display="none";
			$("#familyB").val("家庭成员");
		}else{
		document.getElementById("family").style.display="";
		$("#familyB").val("隐藏家庭成员");
		}
		familyNumber++;
		}
	
	function warkResumeButton(){
		if(warkNumber%2==0){
			document.getElementById("warkResume").style.display="none";
			$("#warkResumeB").val("工作简历");
		}else{
			document.getElementById("warkResume").style.display="";
			$("#warkResumeB").val("隐藏工作简历");
		}
		warkNumber++;
		}
	
	function myModalbz() {
		var area_id =$("#infoAreaID").val();
		if(area_id != "" &&  typeof(area_id)!="undefined"){
				$('#myModalbz').modal({
					keyboard : true
				});
		}else{
			alert("请先选择网点或公司！");
		}
	}

	function myModalWebCam(curRowId){
        	$('#webCamModal').modal({
                backdrop:'static',
                keyboard : true
    		});
    		$("#imgcam").attr("src", "");
    		camRowId = curRowId;
    		camdata_uri='';
    		Webcam.set({
                width: 320,
                height: 240,
                image_format: 'jpeg',
                jpeg_quality: 90
            });
            Webcam.attach( '#webcam' );
	}

	function setBZMessage() {
		var fingerCode3=$("#fingerCode3").val();
		var fingerCode4=$("#fingerCode4").val();
		var FPno=$("#FPno").val();
		if(fingerCode3 != '' &&  typeof(fingerCode3)!="undefined" && fingerCode4 != '' &&  typeof(fingerCode4)!="undefined" && FPno != '' &&  typeof(FPno)!="undefined" ){
			$("#fingerCodeOne").val(fingerCode3);
			$("#fingerCodeTwo").val(fingerCode4);
			$('#myModalbz').modal("hide");
		}else{
			alert("请信息补充完全再进行确认");
		}
	}
	
	function officeTreeselectCallBack(nodes,v, h, f){
		return officeOrCompanyTreeselectCallBack(nodes,v, h, f);
	}
	
	function companyTreeselectCallBack(nodes,v, h, f){
		return officeOrCompanyTreeselectCallBack(nodes,v, h, f);
	}
	
	function officeOrCompanyTreeselectCallBack(nodes,v, h, f){
		if(!nodes  || nodes.length == 0){
			return;
		}
		var selectedNode = nodes[0];
		if(v=="ok"){
			var id = selectedNode.id;
			if(selectedNode.status == 'company'){
				$.ajax({
			        type: "GET",
			        url: "${ctx}/guard/company/getArea?id="+id
			    }).done(function(response) {
					var data = JSON.parse(response);
					var data_areaId= data.areaId;
					
					$("#areaId").val(data_areaId);
// 					if (data_areaId != "" && typeof(data_areaId)!="undefined") {
						onAreaChanged(data_areaId,null,$("#radio_hidden").val());
// 					}
			    });
			}else if(selectedNode.status == 'office'){
				$.ajax({
			        type: "GET",
			        url: "${ctx}/sys/office/getArea?id="+id
			    }).done(function(response) {
					    var data = JSON.parse(response);
					    var data_areaId= data.areaId;
						$("#areaId").val(data_areaId);
                        onAreaChanged(data_areaId,null,$("#radio_hidden").val());
			    });
			}
		
		}
		return true;
	}
	
	
	function sub()
	{
		$("#fingerCodeOne").val(btoa($("#fingerCodeOne").val()));
		$("#fingerCodeTwo").val(btoa($("#fingerCodeTwo").val()));
		$("#fingerCodeThree").val(btoa($("#fingerCodeThree").val()));

		$("#inputForm").submit();
	}

	function copy() {
		alert("join.................");

	}
	
	　//图像加载出错时的处理
	function errorImg(img) {
		img.src = "${ctxStatic}/images/staff_img.jpg";
		img.onerror = null;
	}

	function staffImgDel(idx){
	    $('staffImageList'+idx+'_delFlag').val(1);
	    $('staffImageList'+idx).style.display='none';
	}
	//
	function addRow(list, idx, tpl, row) {
		$(list).append(Mustache.render(tpl, {
			idx : idx,
			delBtn : true,
			row : row
		}));
		var rowId;
		if(row=="" || typeof (row) == "undefined"){
			rowId= "0" ;
		}else{
			rowId=row.id+"";
		}
		if(rowId=="" || typeof (rowId) == "undefined"){
				rowId= "0" ;
		}
		
		var preview2 = '<img id="avatarImage'+idx+'_file" src="${ctx}/guard/image/staff?id=' + rowId
		+ '" onerror="errorImg(this)" alt="点击选取人员照片" style="height:80px">';
			$("#avatar"+idx+"_file").fileinput({
				overwriteInitial : false,
				maxFileSize : 1500,
				showCaption : false,
				showBrowse : false,
				autoReplace : true,
				browseOnZoneClick : true,
				removeLabel : '',
				showRemove : false,
				removeTitle : '取消或重新修改',
				elErrorContainer : '#kv-avatar-errors-2',
				msgErrorClass : 'alert alert-block alert-danger',
				defaultPreviewContent : preview2,
				removeFromPreviewOnError : true,
				layoutTemplates : {
					main2 : '{preview} {remove} {browse}'
				},
				allowedFileExtensions : [ "jpg","bmp","gif","png", "jpeg", "ico"],
				previewSettings : {
					image : {
						width : "100px",
						height : "auto"
					}
				}
			});
			
			
			//头像上传预览
		    $("#staffImageList"+idx+"_up").change(function() {
		    console.log("======");
		        var $file = $(this);
		        var fileObj = $file[0];
		        var windowURL = window.URL || window.webkitURL;
		        var dataURL;
		        var $img = $("#staffImageList"+idx+"_img_id");
		        if(fileObj && fileObj.files && fileObj.files[0]){
			        dataURL = windowURL.createObjectURL(fileObj.files[0]);
			        var subDataUrl= dataURL.substring(dataURL.length-3,dataURL.length);
			        if(subDataUrl!="jpg"){
			        	alert("只能选择JPG格式的图片");			        	
			        }
			        $img.attr('src',dataURL);
		        }else{
			        dataURL = $file.val();
			        var element = document.getElementById("staffImageList"+idx+"_img_id");
			        element.src = dataURL;
			        $("#staffImageList"+idx+"_imagePath").val(dataURL);
		        }
		    });
			
		$(list + idx).find("select").each(function() {
			$(this).val($(this).attr("data-value"));
		});
		$(list + idx).find("input[type='checkbox'], input[type='radio']").each(
				function() {
					var ss = $(this).attr("data-value").split(',');
					for (var i = 0; i < ss.length; i++) {
						if ($(this).val() == ss[i]) {
							$(this).attr("checked", "checked");
						}
					}
				});
	}

	//字母数字
    jQuery.validator.addMethod("alnum", function(value, element) {
  	  return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
    }, "只能包括英文字母和数字");
	
 // 身份证号码验证 
    jQuery.validator.addMethod("isIdCardNo", function(value, element) { 
    	var msg;
    	msg = this.optional(element) || idCardNoUtil.checkIdCardNo(value);  
    	var identifyType=$("#identifyType").val();
    	if(identifyType=="1"){
    		return true;
    	}
    	if(msg==true){
    		if (value.length == 15) {
    			var birDayCode = '19' + value.substring(6, 12);
    		} else if (value.length == 18) {
    			var birDayCode = value.substring(6, 14);
    		}
    		var yyyy = birDayCode.substring(0, 4);
			var mm = birDayCode.substring(4, 6);
			var dd = birDayCode.substring(6);
			var data=yyyy+"-"+mm+"-"+dd;
			var xdata = new Date(data);
			$("#birthday").val(data);
			 
			
		     if(Getsex(value) == 'F'){
		    	$("#sex").val('1').trigger("change"); 
		    }else{
				$("#sex").val('0').trigger("change"); 
		    } 
    	}
     	return msg;
    }, "请输入合法身份证号"); 
 
    function changeFPnoOne(){
    	$("#FPnoOne").val('');
    }
    
    function Getsex(psidno){
        var sexno,sex;
        if(psidno.length==18){
            sexno=psidno.substring(16,17)
        }else if(psidno.length==15){
            sexno=psidno.substring(14,15)
        }else{
            return 'M';
        }
        var tempid=sexno%2;
        if(tempid==0){
            sex='F';
        }else{
            sex='M';
        }
        return sex;
    }
</script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/custom/spectrum.css">
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/staff/list?office.id=${selectedOfficeId}&company.id=${selectedCompanyId}">人员信息列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/staff/form?id=${staff.id}&selectedOfficeId=${selectedOfficeId}&selectedCompanyId=${selectedCompanyId}">人员信息<shiro:hasPermission
					name="guard:staff:edit">${not empty staff.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:staff:edit">查看</shiro:lacksPermission></a></li>
	</ul>


	<form:form id="inputForm" modelAttribute="staff"
		action="${ctx}/guard/staff/save" enctype="multipart/form-data"
		method="post" class="form-horizontal"><div class="row">
		<div style="margin-left:10px;margin-top:10px;">
			<shiro:hasPermission name="guard:staff:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					   value="保 存"/>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				   onclick="history.go(-1)" />
			<shiro:hasPermission name="guard:staff:edit">
				<input id="btnCopy" class="btn btn-primary" type="button" style="margin-left: 5%"
					   value="复 制"/>&nbsp;
				<input id="btnPaste" class="btn btn-primary" type="button"
					   value="粘 贴"/>&nbsp;
			</shiro:hasPermission>
		</div>
	</div>
		<br/>
		<form:hidden path="id" />
		<input type="hidden" name="selectedOfficeId" id="selectedOfficeId"/>
		<input type="hidden" name="selectedCompanyId" id="selectedCompanyId"/>
		<input type="hidden" name="oldstafftype" id="oldstafftype"/>
		<input type="hidden" name="oldareaid" id="oldareaid"/>
		<sys:message content="${message}" />

		<h4 class="section-title" data-toggle="collapse" data-target="#staffBasicInfo"  aria-expanded="false" aria-controls="staffBasicInfo">基本信息</h4>
		<section class="collapse in" id="staffBasicInfo">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>人员姓名：</label>
						<div class="col-xs-5">
							<form:input path="name" htmlEscape="false" maxlength="18"
								id="name" class="form-control input-sm required"
								data-validate="validateName" style="width:158px;"/>
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>证件类型：</label>
						<div class="col-xs-5">
							<form:select path="identifyType" id="identifyType"
								class="form-control input-sm required form-control" style="width:160px;">
								<form:options items="${fns:getDictList('identify_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>证件号码：</label>
						<div class="col-xs-5">
							<form:input path="identifyNumber" htmlEscape="false"
								maxlength="20" class="form-control input-sm required alnum isIdCardNo"
								data-validate="validateIdentify" style="width:158px;"/>
						</div>
						<form:errors path="identifyNumber" cssClass="error"></form:errors>
					</div>
				</div>
			</div>
			<div class="row">	
			<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">性别：</label>
						<div class="col-xs-5">
							<form:select path="sex" class="form-control input-sm"
								style="width:160px;">
								<form:options items="${fns:getDictList('sex_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>人员工号：</label>
						<div class="col-xs-5">
							<form:input path="workNum" htmlEscape="false" maxlength="10"
								class="form-control input-sm required digits" data-validate="validateWorkNum" style="width:158px;"/>
						</div>
						<form:errors path="workNum" cssClass="error"></form:errors>
					</div>
				</div>
                <div class="col-xs-4">
                   <div class="form-group">
                       <label class="control-label col-xs-4">胁迫密码：</label>
                       <div class="col-xs-5">
                           <form:input path="fingerInfoList[0].coercePwd" htmlEscape="false"
                               class="form-control input-sm"  style="width:158px;"/>
                       </div>
                   </div>
               </div>
			</div>
			<div class="row">
			<div class="col-xs-4">
					<div class="form-group" style="display: none">
						<label class="control-label"><font class="red">*</font>所属区域：</label>
						<div class="controls">
							<form:input path="fingerInfoList[0].areaId" htmlEscape="false"
								class="form-control input-sm required" id="infoAreaID" name="infoAreaID"  style="width:158px;"/>
						</div>
					</div>
					<input id="areaId" name="area.id" value="${staff.area.id}" type="hidden">
					
					<div class="form-group" id="office_div">
						<label class="control-label col-xs-4"><font class="red">*</font>所属网点：</label>
						<div class="col-xs-5" id="office_div" style="width:158px;">
							<sys:treeselect id="office" name="office.id"
								value="${staff.office.id}" labelName="office.name"
								labelValue="${staff.office.name}" title="网点"
								selectIds="${staff.office.id}"
								url="/sys/office/treeData" cssClass="required" allowClear="true"
								notAllowSelectRoot="true"/>
						</div>
					</div>
					<div class="form-group" id="company_div">
						<label class="control-label col-xs-4"><font class="red">*</font>所属公司：</label>
						<div class="col-xs-5">
							<sys:treeselect id="company" name="company.id"
								value="${staff.company.id}" labelName="company.shortName"
								selectIds="${staff.company.id}"
								labelValue="${staff.company.shortName}" title="第三方公司"
								url="/guard/company/treeData" cssClass="required"
								allowClear="true" notAllowSelectRoot="true" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>工作状态：</label>
						<div class="col-xs-5">
							<form:select path="workStatus" class="form-control input-sm required"
								style="width:160px;">
								<form:options items="${fns:getDictList('nWork_tatus')}"
									itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>人员状态：</label>
						<div class="col-xs-5">
							<input type="hidden" id="staffStatus" name="">
							<input id="staffStatus"
								value="${fns:getDictLabel(staff.status, 'person_status', '')}"
								htmlEscape="false" maxlength="64" class="form-control input-sm required"
								style="width: 160px;" readonly="true" style="width:158px;"/>
						</div>
					</div>
				</div>
				
			</div>
			<div class="row">
			<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>指&nbsp;纹 &nbsp;号：</label>
						<div class="col-xs-5">
							<form:input path="fingerInfoList[0].id" htmlEscape="false" type="hidden" class="form-control input-sm " data-validate="validateCompanyId" />
							<input type="hidden" value="${staff.fingerInfoList[0].fingerNum}" id="FPnoOne_FingerNum_hidden"/>
							 <select  id="FPnoOne" name="fingerInfoList[0].fingerNum" class="input-sm required" style="width: 160px;display: none">
							
							</select>
				
						</div>
						<form:errors path="fingerInfoList[0].fingerNum" cssClass="error"></form:errors>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>起始时间：</label>
						<div class="col-xs-5">
							<input name="fingerInfoList[0].startDate" type="text"
								maxlength="20" class="form-control input-sm Wdate required" style="height: auto;width:158px;" readonly="readonly"
								id="startDate"
								value="${startDate }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
								data-validate="validateStartDate" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>截止时间：</label>
						<div class="col-xs-5">
							<input name="fingerInfoList[0].endDate" type="text"  style="height: auto;width:158px;" maxlength="20"
								class="form-control input-sm Wdate required" readonly="readonly" id="endDate"
								value="${endDate }"
								
								data-validate="validateEndDate" 
								 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
						</div>
						
						<input  type="text" id="yearLater" value="${yearLater}" style="display: none"/>
					</div>
				</div>
				
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">密&nbsp;&nbsp;&nbsp;码：</label>
						<div class="col-xs-5">
							<form:input path="fingerInfoList[0].pwd" htmlEscape="false"
										class="form-control input-sm" style="width:158px;"/>
						</div>
					</div>
				</div>

				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>人员类型：</label>

						<div class="radio-inline" style="margin-top:-8px;">
							<form:input path="staffType" id="radio_hidden" type="hidden" />
							<label class="control-label"><input type="radio" name="radio" value="0">押款员</label>
							<label class="control-label"><input type="radio" name="radio" value="1"> 交接员</label>
							<label class="control-label"><input type="radio" name="radio" value="2"> 维保员</label>
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>接警号码：</label>
						<div class="col-xs-5">

							<form:input path="phoneNum" htmlEscape="false" maxlength="18"
										id="phoneNum" class="form-control input-sm required"
										data-validate="validateName" style="width:158px;"/>

							<form:errors path="phoneNum" cssClass="error"></form:errors>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">接警手机级别：</label>
						<div class="col-xs-5">
							<form:select path="alarmGrade" class="form-control input-sm required"
										 style="width:160px;">
								<form:options items="${fns:getDictList('alarm_grade')}"
											  itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
				</div>

				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4"><font class="red">*</font>门权限：</label>
						<div class="col-xs-5">
							<form:select path="doorPom" class="form-control input-sm"
										 style="width:160px;">
								<form:options items="${fns:getDictList('yes_no')}"
											  itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
			<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">指纹录入：</label>
						<div class="col-xs-5">
							<div style="display: none">
								<input value="${staff.staffImageList[0].imageType}" maxlength="30"
									class="form-control input-sm form-control" id="staffImageList0" /> <input
									value="${staff.staffExFamilyList[0].name}" maxlength="30"
									class="form-control input-sm form-control" id="staffExFamilyList0" /> <input
									value="${staff.staffExWorkList[0].workName}" maxlength="30"
									class="form-control input-sm form-control" id="staffExWorkList0" /> 
									<input
									name="fingerInfoList[0].fingerTemplate" value="${template}"
									id="fingerCodeOne" />
									 <input name="fingerInfoList[0].backupFp"
									value="${backup}" id="fingerCodeTwo" />
									<input name="fingerInfoList[0].coerceTemplate"
									value="${cTemplate}" id="fingerCodeThree" />
									<input 
									value="MAIN" id="fingerCodeType" />
							</div>
							<input type="button" value="指纹录入" class="btn btn-success btn-sm"
											onclick="myModalbz();" />
						</div>
					</div>
				</div>
				<div class="col-xs-8">
					<div class="form-group">
						<label class="control-label  col-xs-2" ><font class="red">*</font>人员照片：</label> 
						<div id="photos" style="display: block;"  class="col-xs-5">
							<div style="width: 100%; height: auto; overflow-y: auto;">
								<table id="contentTable"
									class="table table-striped table-bordered table-condensed"
									style="width: 430; height: 100%;">
									<thead>
										<tr>
											<th class="hide"></th>
											<th>照片类型</th>
											<th>照片路径</th>
											<shiro:hasPermission name="guard:staff:edit">
												<th width="10">&nbsp;</th>
												<th width="10">&nbsp;</th>
											</shiro:hasPermission>
										</tr>
									</thead>
									<tbody id="staffImageList">
									</tbody>
									<shiro:hasPermission name="guard:staff:edit">
										<tfoot>
											<tr>
												<td colspan="7"><a href="javascript:"
													onClick="addRow('#staffImageList', staffImageRowIdx, staffImageTpl);staffImageRowIdx = staffImageRowIdx + 1;"
													class="btn">新增 </a></td>
											</tr>
										</tfoot>
									</shiro:hasPermission>
								</table>
							</div>
						</div>
					</div>
				 </div>
			</div>
		</section>
		
		<h4 class="section-title collapsed" data-toggle="collapse" data-target="#staffAdditionInfo" aria-expanded="false" aria-controls="staffAdditionInfo">补充信息</h4>
		<section class="collapse" id="staffAdditionInfo">
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">出生日期：</label>
						<div class="col-xs-5">
							<input name="birthday" type="text" readonly="readonly" 
								maxlength="20" class="form-control input-sm Wdate "  id="birthday"
								value="<fmt:formatDate value="${staff.birthday}" pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
						</div>
					</div>
				</div>

				<div class="col-xs-4">
					<div class="form-group ">
						<label class="control-label col-xs-4">职务：</label>
						<div class="col-xs-5">
							<form:input path="dupt" htmlEscape="false" maxlength="64"
								class="form-control input-sm" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">民族：</label>
						<div class="col-xs-5">
							<form:input path="nation" htmlEscape="false" maxlength="12"
								class="form-control input-sm" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">学历：</label>
						<div class="col-xs-5">
							<form:input path="education" htmlEscape="false" maxlength="10"
								class="form-control input-sm" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">家庭住址：</label>
						<div class="col-xs-5">
							<form:input path="address" htmlEscape="false" maxlength="64"
								class="form-control input-sm" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">IC卡号：</label>
						<div class="col-xs-5">
							<form:input path="fingerInfoList[0].cardNum" htmlEscape="false"
								class="form-control input-sm" />
						</div>
						<form:errors path="fingerInfoList[0].cardNum" cssClass="error"></form:errors>
					</div>
				</div>
			</div>
			<div class="row">
                <div class="col-xs-4">
                    <div class="form-group">
                        <label class="control-label col-xs-4">工作岗位：</label>
                        <div class="col-xs-5">
                            <form:input path="work" htmlEscape="false" maxlength="10"
                                class="form-control input-sm" />
                        </div>
                    </div>
                </div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">联系方式：</label>
						<div class="col-xs-5">
							<form:input path="phone" htmlEscape="false" maxlength="18"
								class="form-control input-sm digits" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">电子邮箱：</label>
						<div class="col-xs-5">
							<form:input path="email" htmlEscape="false" maxlength="32"
								class="form-control input-sm email" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label col-xs-4">部门：</label>
						<div class="col-xs-5">
							<form:input path="dept" htmlEscape="false" maxlength="64"
								class="form-control input-sm" />
						</div>
					</div>
				</div>

				
			</div>
			<div class="row">
				<div class="col-xs-8">
					<div class="form-group">
						<label class="control-label col-xs-2">备注信息：</label>
						<div class="col-xs-5">
							<form:textarea path="remarks" htmlEscape="false" rows="3"
								maxlength="255" class="form-control" />
						</div>
					</div>
				</div>
				<div class="col-xs-4">
				</div>
			</div>
		</section>
		
		<h4 class="section-title collapsed" data-toggle="collapse" data-target="#staffOtherInfo" aria-expanded="false" aria-controls="staffOtherInfo">其他信息</h4>
		<section class="collapse" id="staffOtherInfo">
			<div class="row">
				<div class="col-xs-8">
					<label class="control-label  col-xs-2" >家庭成员：</label> 
					<div id="family" style="display: block;" class="col-xs-8">
						<div
							style="width: 100%; height: auto; overflow-y: auto; overflow-x: auto;">
							<table id="contentTable"
								class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
										<th class="hide"></th>
										<th>姓名</th>
										<th>年龄</th>
										<th>关系</th>
										<th>工作单位</th>
										<th>联系方式</th>
										<shiro:hasPermission name="guard:staff:edit">
											<th width="10">&nbsp;</th>
										</shiro:hasPermission>
									</tr>
								</thead>
								<tbody id="staffExFamilyList">
								</tbody>
								<shiro:hasPermission name="guard:staff:edit">
									<tfoot>
										<tr>
											<td colspan="7"><a href="javascript:"
												onclick="addRow('#staffExFamilyList', staffExFamilyRowIdx, staffExFamilyTpl);staffExFamilyRowIdx = staffExFamilyRowIdx + 1;"
												class="btn">新增</a></td>
										</tr>
									</tfoot>
								</shiro:hasPermission>
							</table>
						</div>
					</div>
				 </div>
			</div>	
			<div class="row">
				<div class="col-xs-8">
					<label class="control-label  col-xs-2" >工作简历：</label> 
					<div id="warkResume" style="display: block;" class="col-xs-8">
						<div
							style="width: 100%; height: auto; overflow-y: auto; overflow-x: auto;">
							<table id="contentTable"
								class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
										<th class="hide"></th>
										<th>工作单位</th>
										<th>工作时间段</th>
										<th>部门</th>
										<th>职务</th>
										<th>证明人</th>
										<shiro:hasPermission name="guard:staff:edit">
											<th width="10">&nbsp;</th>
										</shiro:hasPermission>
									</tr>
								</thead>
								<tbody id="staffExWorkList">
								</tbody>
								<shiro:hasPermission name="guard:staff:edit">
									<tfoot>
										<tr>
											<td colspan="7"><a href="javascript:"
												onclick="addRow('#staffExWorkList', staffExWorkRowIdx, staffExWorkTpl);staffExWorkRowIdx = staffExWorkRowIdx + 1;"
												class="btn">新增</a></td>
										</tr>
									</tfoot>
								</shiro:hasPermission>
							</table>
						</div>
					</div>
				</div>
			</div>
		</section>
		
		<!-- 模态框（Modal） -->
		<div class="modal fade" id="myModalbz" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 650px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">指纹信息</h4>
					</div>
					<!-- 表单 -->
					<div class="modal-body" style="width: 650px;">
						<form id="dgForm" enctype="multipart/form-data"
							style="width: 650px;">
							<!--车辆列表-->
							<div class="row">
								<div class="col-xs-5">
										<!-- <object codebase="ZAZFingerActivexT.cab#version=1.0.0.4"
											classid="clsid:87772C8D-3C8C-4E55-A886-5BA5DA384424"
											id="ZAZFingerActivex" name="ZAZFingerActivex" width="256"
											height="320" accesskey="a" tabindex="0" title="finger">
											<embed width="256" height="320"></embed>
										</object> -->
										<object classid="clsid:BF963EA2-F8E2-4B44-B918-D438FD2B3E9E" width="256" height="320" id="ADST2200AX" name="ADST2200AX" accesskey="a" tabindex="0" title="finger">
										   <param name="_Version" value=65536>
										   <param name="_ExtentX" value=10874>
										   <param name="_ExtentY" value=8811>
										   <param name="_StockProps" value=0>
										</object>
									<div id="ZAZFingerActivex_browser" style="width: 256px; height: 308px; display: none;background-color:#ccc;margin-top: 10px;">
										<div style="padding-top: 48%;padding-left: 15%;">
										浏览器不兼容，请使用IE浏览器
										</div>
									</div>
								</div>
								<div class="col-xs-7" >
									<fieldset>
										<legend  style="font-size: 12px;margin: 0;padding: 0px;"><small>正确的采集方式(推荐食指，中指，无名指)</small></legend>
										<img alt="" src="${ctxStatic}/images/IDB_CORRECT_FP.bmp" style="float: left;">
										<div style="float: left; font-size: 12px; width: 162px; height: 76px; margin-left: 10px; border: 1px solid #555555; padding: 10px;">
											<small>收支平压于指纹采集窗口上，指纹纹心尽量对正窗口中心。</small>
										</div>
									</fieldset>
									
									<fieldset>
										<legend  style="font-size: 12px;margin: 0;padding: 0px;"><small>几种错误的采集方式</small></legend>
										<img alt="" src="${ctxStatic}/images/IDB_WRONG_COLLECT_01.bmp" style="float: left; margin-right: 20px;">
										<img alt="" src="${ctxStatic}/images/IDB_WRONG_COLLECT_02.bmp" style="float: left;margin-right: 20px;">
										<img alt="" src="${ctxStatic}/images/IDB_WRONG_COLLECT_03.bmp" style="float: left;margin-right: 20px;">
										<img alt="" src="${ctxStatic}/images/IDB_WRONG_COLLECT_04.bmp" style="float: left;margin-right: 20px;">
									</fieldset>
								</div>
							</div>
							<hr/>
							<div class="row">
								<div class="col-md-4">
								<span style="font-weight:bold;font-size=25px;" id="fingerMsg"></span>
								</div>
								<div class="col-md-8">
									<div class="form-group">
										<div class="controls" style="float:right;">
											<input htmlEscape="false" class="form-control input-sm "
												id="fingerCode3" readonly="true" name="fingerCode3"
												type="hidden" /> <input type="button" name="btnGetFinger3"
												id="btnGetFinger3" value="录主指纹" onclick="displaymessage3()"
												class="btn btn-success btn-sm" /> &nbsp; <input type="button" name="btnGetFinger4"
												id="btnGetFinger4" value="录备指纹" onclick="displaymessage4()"
												class="btn btn-success btn-sm" /> &nbsp; <input type="button" name="btnGetFinger5"
												id="btnGetFinger5" value="录胁迫指纹" onclick="displaymessage5()"
												class="btn btn-success btn-sm" />
											      &nbsp;
												<!-- <input type="button"
												name="btnSearchFp" id="btnSearchFp" value="校验指纹"
												onclick="SearchFp()" class="btn btn-primary btn-sm" /> -->
										</div>
									</div>
									<div class="form-group" style="display: none">
										<label class="control-label">注册指纹</label>
										<div class="controls">
											<input htmlEscape="false" class="form-control input-sm "
												id="FPno" readonly="true" name="FPno" /> <input
												type="button" name="btnAddFp" id="btnAddFp" value="注册指纹"
												onclick="Addfp()" class="btn btn-success btn-sm" />
										</div>
									</div>
								</div>
								<!-- <div class="col-md-1">
									<input htmlEscape="false" class="form-control input-sm "
												id="fingerCode4" name="fingerCode4" readonly="true"
												type="hidden" /> <input type="button" name="btnFPdel" id="btnFPdel"
												value="删除指纹" onclick="Delfp()"
											class="btn btn-warning btn-sm" /> 
								</div> -->
							</div>
<!-- 							<div class="modal-footer"> -->
<!-- 							<button type="button" class="btn btn-primary" -->
<!-- 								data-dismiss="modal">关闭</button> -->
<!-- 							</div> -->
						</form>
					</div>
				</div>
			</div>
		</div>
	</form:form>

	<!-- 模态框（Modal） -->
    	<div class="modal fade" id="webCamModal" tabindex="-1" role="dialog"
    		aria-labelledby="myModalLabel" aria-hidden="true">
    		<div class="modal-dialog" style="width: 800px;">
    			<div class="modal-content">
    				<div class="modal-header">
    					<button type="button" class="close" data-dismiss="modal"
    						aria-hidden="true">&times;</button>
    					<h4 class="modal-title" id="myModalLabel">摄像头拍照</h4>
    				</div>
    				<!-- 表单 -->
    				<div class="modal-body" style="width: 780px;">
                        <div class="row" style="width:760px;">
                            <div class="col-xs-5">
                            <div id="webcam"></div>
                            </div>
                            <div class="col-xs-2">
                                 <div class="btncam">
                                     <input type="button" value="拍照" id="saveBtn" onclick="savePhoto();"/>
                                 </div>
                                  <div class="btncam">
                                      <input type="button" value="确认" id="saveBtn1" onclick="ensurePhoto();"/>
                                  </div>
                                  <div class="btncam">
                                    <input type="button" data-dismiss="modal" value="关闭"></input>
                                </div>
                             </div>
                             <div class="col-xs-5">
                             <div id="photoscam">
                                 <img src="" id="imgcam">
                             </div>
                             </div>
                         </div>
    				</div>
    			</div>
    		</div>
    	</div>
</div>


	<!-- 照片 -->
	<script type="text/template" id="staffImageTpl">//<!--
						<tr id="staffImageList{{idx}}">
							<td class="hide">
								<input id="staffImageList{{idx}}_id" name="staffImageList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="staffImageList{{idx}}_delFlag" name="staffImageList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="staffImageList{{idx}}_imageType" name="staffImageList[{{idx}}].imageType" data-value="{{row.imageType}}" class="form-control input-sm ">
									<c:forEach items="${fns:getDictList('image_type')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>

							<!--[if lt IE 10]>
								<div class="picbtn"><img id="staffImageList{{idx}}_img_id" src= "${ctx}/guard/image/staff?id={{row.id}}" onerror="errorImg(this)"  style="width:100px" /></div>
								<input type="file" name="staffImageList[{{idx}}].file" id="staffImageList{{idx}}_up">
							<![endif]-->
							
							<!--[if gte IE 10]>
								<div class="kv-avatar" style="width: 100px">
									<input id="avatar{{idx}}_file" name="staffImageList[{{idx}}].file" type="file" class="file-loading">
								</div>
							<![endif]-->

							<!--[if !IE]><!-->
							<div class="kv-avatar" style="width: 100px">
								<input id="avatar{{idx}}_file" name="staffImageList[{{idx}}].file" type="file" class="file-loading">
								</div>
							<!--<![endif]-->
								<p class="help-block">点击上面图片上传新图片。</p>
								<input id="staffImageList{{idx}}_imagePath" name="staffImageList[{{idx}}].imagePath" value="{{row.imagePath}}" type="hidden" maxlength="255" />
							</td>
							<shiro:hasPermission name="guard:staff:edit">
							<td class="text-center" width="10">
                                {{#delBtn}}<span class="close" onclick="myModalWebCam('{{idx}}')" title="拍照"><i class="glyphicon glyphicon-camera"></i></span>{{/delBtn}}
                            </td>
							<td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffImageList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
	<script type="text/javascript">
						var staffImageRowIdx = 0, staffImageTpl = $("#staffImageTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(staff.staffImageList)};
							for (var i=0; i<data.length; i++){
								addRow('#staffImageList', staffImageRowIdx, staffImageTpl, data[i]);
								staffImageRowIdx = staffImageRowIdx + 1;
							}
						});
					</script>
	<script type="text/template" id="staffExFamilyTpl">//<!--
						<tr id="staffExFamilyList{{idx}}">
							<td class="hide">
								<input id="staffExFamilyList{{idx}}_id" name="staffExFamilyList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="staffExFamilyList{{idx}}_delFlag" name="staffExFamilyList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_name" name="staffExFamilyList[{{idx}}].name" type="text" value="{{row.name}}" maxlength="32" class="form-control input-sm  "  data-validate="validateRowName"/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_age" name="staffExFamilyList[{{idx}}].age" type="text" value="{{row.age}}" class="form-control input-sm  "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_relation" name="staffExFamilyList[{{idx}}].relation" type="text" value="{{row.relation}}" maxlength="20" class="form-control input-sm  "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_work" name="staffExFamilyList[{{idx}}].work" type="text" value="{{row.work}}" maxlength="64" class="form-control input-sm  "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_phone" name="staffExFamilyList[{{idx}}].phone" type="text" value="{{row.phone}}" maxlength="18" class="form-control input-sm  "/>
							</td>
							<shiro:hasPermission name="guard:staff:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffExFamilyList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
	<script type="text/javascript">
						var staffExFamilyRowIdx = 0, staffExFamilyTpl = $("#staffExFamilyTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(staff.staffExFamilyList)};
							for (var i=0; i<data.length; i++){
								addRow('#staffExFamilyList', staffExFamilyRowIdx, staffExFamilyTpl, data[i]);
								staffExFamilyRowIdx = staffExFamilyRowIdx + 1;
							}
						});
					</script>
	<script type="text/template" id="staffExWorkTpl">//<!--
						<tr id="staffExWorkList{{idx}}">
							<td class="hide">
								<input id="staffExWorkList{{idx}}_id" name="staffExWorkList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="staffExWorkList{{idx}}_delFlag" name="staffExWorkList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_workName" name="staffExWorkList[{{idx}}].workName" type="text" value="{{row.workName}}" maxlength="32" class="form-control input-sm " data-validate="validateWorkName"/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_workTime" name="staffExWorkList[{{idx}}].workTime" type="text" value="{{row.workTime}}" maxlength="64" class="form-control input-sm "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_dept" name="staffExWorkList[{{idx}}].dept" type="text" value="{{row.dept}}" maxlength="64" class="form-control input-sm "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_dupt" name="staffExWorkList[{{idx}}].dupt" type="text" value="{{row.dupt}}" maxlength="64" class="form-control input-sm "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_certifier" name="staffExWorkList[{{idx}}].certifier" type="text" value="{{row.certifier}}" maxlength="64" class="form-control input-sm "/>
							</td>
							<shiro:hasPermission name="guard:staff:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffExWorkList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					
					
	<script type="text/javascript">
		var isIE = /msie/.test(navigator.userAgent.toLowerCase());
		if(!isIE){ 
			$("#ADST2200AX").hide();
			$("#ZAZFingerActivex_browser").show();
		}
	
		var staffExWorkRowIdx = 0, staffExWorkTpl = $("#staffExWorkTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		$(document).ready(function() {
			var data = ${fns:toJson(staff.staffExWorkList)};
			for (var i=0; i<data.length; i++){
				addRow('#staffExWorkList', staffExWorkRowIdx, staffExWorkTpl, data[i]);
				staffExWorkRowIdx = staffExWorkRowIdx + 1;
			}
		});
		
		$('#myModalbz').on('show.bs.modal', function (e) {
			  // 执行一些动作...
			var ADST2200AX = document.getElementById("ADST2200AX");
			
			try{
				var w_nRet = ADST2200AX.ADST2200AX_Initialize();
				
				if(w_nRet==-4){
					alert("未检测到指纹录入设备");
					return e.preventDefault();
					
				}
				if(w_nRet!=0&&w_nRet!=-6){
					alert("指纹设备启动失败");
					//一般都是ADST2200AX在init状态下  再次调用init方法导致
					ADST2200AX.ADST2200AX_UnInitialize();
					return e.preventDefault();
				}
			}catch(e){
				if(!isIE){
					alert("请使用IE浏览器，并且正确安装ADST控件");
				}else{
					alert("请正确安装ADST控件");
				}
				return e.preventDefault();
			}
		});
		
		$('#myModalbz').on('hidden.bs.modal', function () {
			  // 执行一些动作...
			var ADST2200AX = document.getElementById("ADST2200AX");
			try{
				var w_nRet = ADST2200AX.ADST2200AX_UnInitialize();
			}catch(e){
			}
		});
	var encoll = 1;
	</script>
	<!-- 插件监听事件 -->
	<SCRIPT type="text/javascript" FOR="ADST2200AX" EVENT="OnADST2200AX_Event(p_nEventID,p_nRetStatus,p_nRetVal)" >
	var ADST2200AX = document.getElementById("ADST2200AX");
  	   if(p_nEventID==25){
  		   if(p_nRetStatus!=0){
  			   console.error("指纹数据库连接失败！");
  		   }
  		   //指纹校验
  	   }else if(p_nEventID==9){
  		   if(p_nRetStatus!=0){
  			   if(p_nRetStatus==-9){
  				 textToSpeach("校验失败,没有匹配的指纹号");
  				alert("校验失败,没有匹配的指纹号");
  				ADST2200AX.ADST2200AX_FpCancel();
  				  // alert("没有匹配的指纹号");
  			   }else if(p_nRetStatus==244){
  				 textToSpeach("操作超时");
  				alert("操作超时");
  			   }
  		   }else{
  			   //当事件为校验时候
  			   textToSpeach("校验成功");
  			 alert("搜索到相同指纹ID："+p_nRetVal);
  			ADST2200AX.ADST2200AX_FpCancel();
  		   }
  		   //指纹采集
  	   }else if(p_nEventID==6){
  		 if(p_nRetStatus!=0){
			   if(p_nRetStatus==240){
				   if(encoll==1){
					   //alert("请第一次按下手指");
					   document.getElementById('fingerMsg').innerHTML = "请第一次按下手指";
					   encoll++;
					   textToSpeach("请第一次按下手指");
				   }else if(encoll==2){
					   encoll++;
					   textToSpeach("请第二次按下手指");
				   }else{
					   encoll=1;
					   textToSpeach("请第三次按下手指");
				   }
			   }else if(p_nRetStatus==241){
				   textToSpeach("请抬起手指");
				   if(encoll==2){
					   //alert("请第二次按下手指");
					   document.getElementById('fingerMsg').innerHTML = "请第二次按下手指";
				   }
				   if(encoll==3){
					  // alert("请第三次按下手指");
					   document.getElementById('fingerMsg').innerHTML = "请第三次按下手指";
					   encoll++;
				   }
				  // alert("请松开！");
			   }else if(p_nRetStatus==244){
				   textToSpeach("操作超时");
				   alert("操作超时");
				   encoll=1;
			   }else if(p_nRetStatus==245){
				   textToSpeach("采集失败,与指纹号"+p_nRetVal+"相同");
				   alert("采集失败,与指纹号"+p_nRetVal+"相同");
				   encoll=1;
			   }
		   }else{
			   textToSpeach("注册成功");
			   alert("注册成功");
			   encoll=1;
			   //区分是主指纹还是备份指纹
			   displaymessageSuccess($('#fingerCodeType').val());
		   }
  	   }
	</SCRIPT>
	
</body>
</html>
