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
<link href="${ctxStatic}/custom/chosen/chosen.css" rel="stylesheet" />
<script type='text/javascript'
	src='${ctxStatic}/custom/chosen/chosen.jquery.js'></script>
<script src="${ctxStatic}/custom/bootstrap-wizard/bootstrap-wizard.js"></script>
<script src="${ctxStatic}/custom/staffForm.js"></script>
<script src="${ctxStatic}/base64/base64.js"></script>
<style>
.tableinput {
	width: 50px;
}

select {
	z-index: 99999;
}
</style>
<script type="text/javascript">
		var wizard;
		$(document).ready(function() {
			
			$.fn.wizard.logging = false;
			
			wizard = $("#wizard-demo").wizard({
				title : "新建人员",
				showCancel : true,
				isModal : false,
				show : false,
				contentHeight : 450,
				contentWidth : 800,
				progressBarCurrent: true,
				buttons : {
					cancelText: "取消",
						nextText: "下一步",
						backText: "上一步",
						submitText: "提交",
						submittingText: "提交中..."
				}
			});
	
			$(".chzn-select").chosen();
	
			wizard.el.find(".wizard-ns-select").change(function() {
				wizard.el.find(".wizard-ns-detail").show();
			});
	
			wizard.el.find(".create-server-service-list").change(function() {
				var noOption = $(this).find("option:selected").length == 0;
				wizard.getCard(this).toggleAlert(null, noOption);
			});
	
			wizard.on("submit", function(wizard) {
				$("#fingerCode3").val(btoa($("#fingerCode3").val()));
				$("#fingerCode4").val(btoa($("#fingerCode4").val()));
				
			    $.ajax({
			        type: "POST",
			        url: "${ctx}/guard/staff/saveJson",
			        data: wizard.serialize(),
			        dataType: "json"
			    }).done(function(response) {
			        wizard.submitSuccess();         // displays the success card
			        wizard.hideButtons();           // hides the next and back buttons
			        wizard.updateProgressBar(0);    // sets the progress meter to 0
			    }).fail(function() {
			        wizard.submitError();           // display the error card
			        wizard.hideButtons();           // hides the next and back buttons
			    });
			});
			
			wizard.cards["location"].on("validate", function(card) {
				var areaID = card.el.find("#areaId");
				var name = areaID.val();
				if (name == "") {
					card.wizard.errorPopover(areaID, "所属机构为空");
					return false;
				} else {
					$("#infoAreaID").val(name);
					 if($("h3.wizard-title").text()=="新建人员"){
							$.ajax({
						        type: "GET",
						        url: "${ctx}/guard/staff/getNewFingerId?id="+name
						    }).done(function(response) {
								var data = JSON.parse(response);
								$("#FPno").val(data.id);
								}).fail(function() {
						    });
						}else{
							 if($("h3.wizard-title").text()=="修改人员"){
									if($("#FPno").val()=="")
									{
										$.ajax({
									        type: "GET",
									        url: "${ctx}/guard/staff/getNewFingerId?id="+name
									    }).done(function(response) {
											var data = JSON.parse(response);
											$("#FPno").val(data.id);
											}).fail(function() {
									    });	
									}
							 }
						}
					return true;
				}
			});
	
			wizard.on("reset", function(wizard) {
				wizard.setSubtitle("");
				wizard.el.find("#new-server-fqdn").val("");
				wizard.el.find("#new-server-name").val("");
				$('#inputForm')[0].reset();
				document.getElementById("inputForm").reset();
				$('#btnReset').click();
				staffExWorkRowIdx = 0;
				staffExFamilyRowIdx = 0;
				staffImageRowIdx = 0;
			});
	
			wizard.el.find(".wizard-success .im-done").click(function() {
				wizard.reset().close();
			});
	
			wizard.el.find(".wizard-success .create-another-server").click(
					function() {
						wizard.reset();
					});
			
			$('#myModal').on('click', function(e){
				  e.preventDefault();
				  
				  $("#staffImageList").children().remove();
				  $("#staffExFamilyList").children().remove();
				  $("#staffExWorkList").children().remove();
				  wizard.reset();
				  $("h3.wizard-title").text('新建人员');
				  wizard.show();
				  
					$("#officeButton").removeClass("disabled");
					$("#areaButton").removeClass("disabled");
				  /* $('#modal').load($(this).attr('href'),function(responseTxt,statusTxt,xhr){
					   if(statusTxt=="success"){
						   var div = document.getElementById('modal');
						   var arr = div.getElementsByTagName('script');
						   for (var n = 0; n < arr.length; n++){
							   if(arr[n].type != "text/template")
							  	 eval(arr[n].innerHTML);
						   } 
						   $(function() {
								
							});
					   }
						}); */
				});	
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						/*  document.execCommand('CreateLink',true,'name.txt');//弹出一个对话框输入URL      */
						$("#searchForm").attr("action","${ctx}/guard/staff/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action","${ctx}/guard/staff/list");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			 
		/* 	$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			}); */
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/guard/staff/list");
			$("#searchForm").submit();
        	return false;
        }
		
		function myModalbz() {
					$('#myModalbz').modal({
						keyboard : true
					});
		}
		
		function validMainenace(url,staffId,staffType){
			    $.ajax({
                    type: "GET",
                    async:false,
                    url: "${ctx}/guard/staff/validMaintenance?id="+staffId
                     }).done(function(response) {
                    //var data = JSON.parse(response);
                    top.$.jBox.confirm(response+" 确定是否审核通过","系统提示",function(v,h,f){
                        if(v=="ok"){
                        //异常弹框解决
                            if(top.$.jBox.tip.mess){
                                top.$.jBox.tip.mess=0;
                            }
                            location.href = url;
                        }
                    },{buttonsFocus:1});
                    }).fail(function() {
                });
		}
		
		
		function toAddStaff(){
			var url = '${ctx}/guard/staff/form?';
			var treeId = parent.$("#id").val();
			var status = parent.$('#status').val();
			if(treeId && status == 'office'){
				url += '&selectedOfficeId=' + treeId;
			}
			if(treeId && status == 'company'){
				url += '&selectedCompanyId=' + treeId;
			}
			window.location = url;
		}
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/guard/staff/import" method="post"
			enctype="multipart/form-data" class="form-search"
			style="padding-left: 20px; text-align: center;"
			onsubmit="loading('正在导入，请稍等...');">

			<br /> <br /> <input id="uploadFile" name="file" type="file"
				style="width: 330px" /><br /> <br /> <input id="btnImportSubmit"
				class="btn btn-primary" type="submit" value="   导    入   " /> <a
				href="${ctx}/guard/staff/import/template">下载模板</a>
		</form>
	</div>


	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModalbz" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 650px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">导入数据</h4>
				</div>
				<!-- 表单 -->
				<div class="modal-body" style="width: 800px;">

					<form id="importForm" action="${ctx}/guard/staff/import"
						method="post" enctype="multipart/form-data" class="form-search"
						style="padding-left: 20px; text-align: center; width: 600px;"
						onsubmit="loading('正在导入，请稍等...');">

						<div class="row">
							<div class="col-xs-4">
								<label>人员姓名：</label> <select name="name"
									style="width: 30%; z-index: 999999;" value="A">
									<option value="A" selected="selected">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>证件类型：</label> <select name="identifyType"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B" selected="selected">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>证件号码：</label> <select name="identifyNumber"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C" selected="selected">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-4">
								<label>人员工号：</label> <select name="workNum" style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D" selected="selected">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>人员类型：</label> <select name="staffType"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E" selected="selected">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>所属区域：</label> <select name="area_name"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F" selected="selected">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-4">
								<label>所属网点：</label> <select name="office_name"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G" selected="selected">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>所属公司：</label> <select name="company_name"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H" selected="selected">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>指&nbsp;纹&nbsp;号：</label> <select name="fingerNum" style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I" selected="selected">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-4">
								<label>人员状态：</label> <select name="status" style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J" selected="selected">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>工作状态：</label> <select name="workStatus"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K" selected="selected">K</option>
									<option value="L">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>起始时间：</label> <select name="startDate"
									style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L" selected="selected">L</option>
									<option value="M">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
							<div class="col-xs-4">
								<label>截止时间：</label> <select name="endDate" style="width: 30%;">
									<option value="A">A</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>
									<option value="E">E</option>
									<option value="F">F</option>
									<option value="G">G</option>
									<option value="H">H</option>
									<option value="I">I</option>
									<option value="J">J</option>
									<option value="K">K</option>
									<option value="L">L</option>
									<option value="M" selected="selected">M</option>
									<option value="N">N</option>
									<option value="O">O</option>
									<option value="P">P</option>
									<option value="Q">Q</option>
									<option value="R">R</option>
									<option value="S">S</option>
									<option value="T">T</option>
									<option value="U">U</option>
									<option value="V">V</option>
									<option value="W">W</option>
									<option value="X">X</option>
									<option value="Y">Y</option>
									<option value="Z">Z</option>
								</select>
							</div>
						</div>
						<br />
						<div class="row">
							<div class="col-xs-4">
								<label>导入起始行：</label><input name="sta_hang" style="width: 35px;" />
							</div>
							<div class="col-xs-4">
								<label>导入截止行：</label><input name="end_hang" style="width: 35px;" />
							</div>
							<div class="col-xs-4">
								<input name="overide" type="checkbox" id="checkbox" /><label>导入覆盖</label>
							</div>
						</div>
						<br />
						<div class="row">
							<div class="col-xs-4">
								<input id="uploadFile" name="file" type="file" />
							</div>
							<div class="col-xs-4">
								<input id="btnImportSubmit" class="btn btn-primary"
									type="submit" value="   导    入   " />
							</div>
						</div>
						<br />
						<div class="modal-footer">
							<label> 导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！ </label>
							<button type="button" class="btn btn-default" name="default"
								value="关闭">关闭</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>




	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/staff/list?office.id=${staff.office.id}&company.id=${staff.company.id}">人员信息列表</a></li>
		<shiro:hasPermission name="guard:staff:edit">
			<li><a href="javascript:void(0)" onclick="toAddStaff()">人员信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="staff"
		action="${ctx}/guard/staff/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"value="${page.pageSize}" />
		<c:if test="${staff.office != null and staff.office.id != null}">
			<input name="office.id" type="hidden"value="${staff.office.id}" />
		</c:if>
		<c:if test="${staff.company != null and staff.company.id != null}">
			<input name="company.id" type="hidden"value="${staff.company.id}" />
		</c:if>
		<div class="form-group">
			<label>姓名：</label>
			<form:input path="name" htmlEscape="false" maxlength="32" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>工号：</label>
			<form:input path="workNum" htmlEscape="false" maxlength="10" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>身份证：</label>
			<form:input path="identifyNumber" htmlEscape="false" maxlength="20" class="form-control input-sm" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			<input id="btnImport" class="btn btn-primary" type="button" value="导入" onclick="myModalbz()" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>	
				<th style="width: 30px;">序号</th>
				<th>姓名</th>
				<th>
					<c:choose>
						<c:when test="${not empty staff.office.id}">网点</c:when>
						<c:otherwise>
							公司
						</c:otherwise>
					</c:choose>
				</th>
				<th>性别</th>
				<th>工号</th>
				<th>指纹号</th>
				<th>人员类型</th>
				<th>工作状态</th>
				<th>身份证号码</th>
				<th>有效期</th>
				<th>联系方式</th>
				<shiro:hasPermission name="guard:staff:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item" varStatus="varStatus">
				<tr>
					<td style="text-align: center;">${varStatus.count}</td>
					<td><a href="${ctx}/guard/staff/form?id=${item.id}&selectedOfficeId=${staff.office.id}&selectedCompanyId=${staff.company.id}">
							${item.name} </a></td>
					<td>
						<c:choose>
							<c:when test="${not empty  staff.office.id}">${item.office.name}</c:when>
							<c:otherwise>
								${item.company.shortName}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${fns:getDictLabel(item.sex, 'sex_type', '')}</td>
					<td>${item.workNum}</td>
					<td>${item.fingerInfoList[0].fingerNumLabel}</td>
					<td>${fns:getDictLabel(item.staffType, 'staff_type', '')}</td>
					<td>${fns:getDictLabel(item.workStatus, 'nWork_tatus', '')}</td>
					<td>${item.identifyNumber}</td>
					<td><fmt:formatDate value="${item.fingerInfoList[0].endDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${item.phone}</td>
					<shiro:hasPermission name="guard:staff:edit">
						<td><a href="${ctx}/guard/staff/form?id=${item.id}">修改</a>
							<a href="${ctx}/guard/staff/delete?id=${item.id}&selectedCompanyId=${item.company.id}&selectedOfficeId=${item.office.id}" onclick="return confirmx('确认要删除该人员信息吗？', this.href)">删除</a>
							<shiro:hasPermission name="guard:staff:audit">
								<c:if test="${item.status == 0}">
								    <c:if test="${item.staffType == '2'}">
									    <a href="javascript:void(0);"
										   onclick="validMainenace('${ctx}/guard/staff/audit?status=1&id=${item.id}&selectedCompanyId=${item.company.id}&selectedOfficeId=${item.office.id}',${item.id},${item.staffType})"
										   style="color: red">审核</a>
									</c:if>
									 <c:if test="${item.staffType != '2'}">
                                        <a href="${ctx}/guard/staff/audit?status=1&id=${item.id}&selectedCompanyId=${item.company.id}&selectedOfficeId=${item.office.id}"
                                        onclick="return confirmx('确认该人员审核通过吗？', this.href)"
                                        style="color: red">审核</a>
                                    </c:if>
								</c:if>
							</shiro:hasPermission>
							<shiro:hasPermission name="guard:staff:approval">
								<c:if test="${item.status == 1}">
									<a href="${ctx}/guard/staff/approval?status=2&id=${item.id}&selectedCompanyId=${staff.company.id}&selectedOfficeId=${staff.office.id}"
										onclick="return confirmx('确认该人员审批通过', this.href)"
										style="color: red">审批</a>
								</c:if>
							</shiro:hasPermission>
							<c:if test="${(item.status == 2 || item.status == 3)&& item.staffType != '2'}">
								<a href="${ctx}/guard/staff/download?id=${item.id}&selectedCompanyId=${staff.company.id}&selectedOfficeId=${staff.office.id}"
									onclick="return confirmx('是否同步该人员', this.href)">同步</a>
							</c:if></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>


	<div class="wizard" id="wizard-demo" data-title="创建人员">
		<h1>创建人员</h1>
		<form:form modelAttribute="staff" name="inputForm" id="inputForm"
			action="${ctx}/guard/staff/save" method="post"
			class="form-horizontal">
			<div class="wizard-card" data-onValidated="setServerName"
				data-cardname="name">
				<input type="hidden"  id="selected_company_id"  value="${selected_company_id}"/>
				<input type="hidden"  id="selected_office_id" value="${selected_office_id}"/>
				
				<form:hidden path="id" />
				<h3>基本信息</h3>
				<div class="form-group">
					<label class="control-label col-xs-4">姓名：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<form:input path="name" htmlEscape="false" maxlength="18"
							class="input-sm required form-control"
							data-validate="validateName" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">性别：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<form:select path="sex" class="input-sm required form-control">
							<form:options items="${fns:getDictList('sex_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">出生日期：</label>
					<div class="col-xs-6">
						<input name="birthday" type="text" readonly="readonly"
							maxlength="20" class="input-medium Wdate "
							value="<fmt:formatDate value="${staff.birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">证件类型：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<form:select path="identifyType"
							class="input-sm required form-control">
							<form:options items="${fns:getDictList('identify_ype')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">证件号码：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<form:input path="identifyNumber" htmlEscape="false"
							maxlength="20" class="input-sm form-control"
							data-validate="validateIdentify" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">工号：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<form:input path="workNum" htmlEscape="false" maxlength="10"
							class="input-sm required form-control"
							data-validate="validateWorkNum" />
					</div>
				</div>
			</div>
			<div class="wizard-card" data-onload="" data-cardname="location">
				<h3>工作职务</h3>
				<div class="form-group col-xs-6" style="display: none">
					<label class="control-label">区域ID：<span class="help-inline"><font
							color="red">*</font> </span></label>
					<div class="controls">
						<form:input path="fingerInfoList[0].areaId" htmlEscape="false"
							class="form-control input-sm required" id="infoAreaID"
							name="infoAreaID" />
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label class="control-label">网点ID：<span class="help-inline"><font
							color="red">*</font> </span></label>
					<div class="controls" id="office_div">
						<sys:treeselect id="office" name="office.id"
							value="${staff.office.id}" labelName="office.name"
							labelValue="${staff.office.name}" title="网点"
							url="/sys/office/treeData?type=2" cssClass="required"
							allowClear="true" notAllowSelectParent="true" />
					</div>
				</div>

				<div class="form-group col-xs-6">
					<label class="control-label">区域ID：<span class="help-inline"><font
							color="red">*</font> </span></label>
					<div class="controls" id="area_div">
						<sys:treeselect id="area" name="area.id" value="${staff.area.id}"
							labelName="area.name" labelValue="${staff.area.name}" title="区域"
							url="/sys/area/treeData" cssClass="required" allowClear="true"
							notAllowSelectParent="true" />
					</div>
				</div>

				<div class="form-group col-xs-6">
					<label class="control-label">所属机构ID：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="controls">
						<form:input path="company.id" htmlEscape="false"
							class="form-control input-sm required"
							data-validate="validateCompanyId" />
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label class="control-label">部门：</label>
					<div class="controls">
						<form:input path="dept" htmlEscape="false" maxlength="64"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label class="control-label">工作岗位：</label>
					<div class="controls">
						<form:input path="work" htmlEscape="false" maxlength="10"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label class="control-label">职务：</label>
					<div class="controls">
						<form:input path="dupt" htmlEscape="false" maxlength="64"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label class="control-label">人员类型：<span class="help-inline"><font
							color="red">*</font> </span></label>
					<div class="controls">
						<form:select path="staffType"
							class="input-sm required form-control">
							<form:options items="${fns:getDictList('staff_type')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label class="control-label">人员状态：<span class="help-inline"><font
							color="red">*</font> </span></label>
					<div class="controls">
						<form:select path="status" class="input-sm required form-control">
							<form:options items="${fns:getDictList('person_status')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>

					</div>
				</div>
				<div class="form-group col-xs-4">
					<label class="control-label">人员工作状态：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="controls">
						<form:select path="workStatus"
							class="input-sm required form-control">
							<form:options items="${fns:getDictList('nWork_tatus')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
			</div>
			<!-- <div class="wizard-card" data-cardname="group">
				<h3 style="margin-bottom: 0px; display: none;">指纹录制</h3>
				<div class="row">
					<div class="col-xs-8">
						<object codebase="ZAZFingerActivexT.cab#version=1.0.0.4"
							classid="clsid:87772C8D-3C8C-4E55-A886-5BA5DA384424"
							id="ZAZFingerActivex" name="ZAZFingerActivex" width="256"
							height="288" accesskey="a" tabindex="0" title="finger">
							<embed width="256" height="288"></embed>
						</object>
					</div>
					<input id="fingerInfoList0_id" name="fingerInfoList[0].id"
						type="hidden" value="" />
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label">指纹模板数据</label>
							<div class="controls">
								<form:input path="fingerInfoList[0].fingerTemplate"
									htmlEscape="false" class="input-xlarge required"
									id="fingerCode3" readonly="true" name="fingerCode3" />
								<input type="button" name="btnGetFinger3" id="btnGetFinger3"
									value="取指纹" onclick="displaymessage3()"
									class="btn btn-success btn-sm" />
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">人员备份指纹</label>
							<div class="controls">
								<form:input path="fingerInfoList[0].backupFp" htmlEscape="false"
									class="input-xlarge required" id="fingerCode4"
									name="fingerCode4" readonly="true" />
								<input type="button" name="btnGetFinger4" id="btnGetFinger4"
									value="备份指纹" onclick="displaymessage4()"
									class="btn btn-success btn-sm" />
							</div>
						</div>
						<div class="form-group">
							<label class="control-label">注册指纹</label>
							<div class="controls">
								<form:input path="fingerInfoList[0].fingerNum"
									htmlEscape="false" class="input-xlarge required" id="FPno"
									readonly="true" name="FPno" />
								<input type="button" name="btnAddFp" id="btnAddFp" value="注册指纹"
									onclick="Addfp()" class="btn btn-success btn-sm" />
							</div>
						</div>
						<div class="form-group">
							<div class="controls">
								<input type="button" name="btnFPdel" id="btnFPdel" value="删除指纹"
									onclick="Delfp()" class="btn btn-success btn-sm" /> <input
									type="button" name="btnSearchFp" id="btnSearchFp" value="搜索指纹"
									onclick="SearchFp()" class="btn btn-success btn-sm" />
							</div>
						</div>

					</div>
				</div>
			</div> -->

			<div class="wizard-card">
				<h3>指纹特征信息</h3>
				<div class="form-group">
					<label class="control-label col-xs-4">密码：</label>
					<div class="col-xs-6">
						<form:input path="fingerInfoList[0].pwd" htmlEscape="false"
							maxlength="8" class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">起始时间：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<input name="fingerInfoList[0].startDate" type="text"
							readonly="readonly" maxlength="20" class="input-medium Wdate "
							value="<fmt:formatDate value="${staff.fingerInfoList[0].startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
							data-validate="validateStartDate" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">截止时间：<span
						class="help-inline"><font color="red">*</font> </span></label>
					<div class="col-xs-6">
						<input name="fingerInfoList[0].endDate" type="text"
							readonly="readonly" maxlength="20" class="input-medium Wdate "
							value="<fmt:formatDate value="${staff.fingerInfoList[0].endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
							data-validate="validateEndDate" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">主机信息：</label>
					<div class="col-xs-6">
						<form:input path="fingerInfoList[0].host" htmlEscape="false"
							class="form-control input-sm required" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">备注信息：</label>
					<div class="col-xs-6">
						<form:textarea path="remarks" htmlEscape="false" rows="3"
							maxlength="255" class="form-control" />
					</div>
				</div>
			</div>
			<div class="wizard-card" data-cardname="group">
				<h3>信息完善</h3>
				<div class="form-group">
					<label class="control-label col-xs-4">联系方式：</label>
					<div class="col-xs-6">
						<form:input path="phone" htmlEscape="false" maxlength="18"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">电子邮箱：</label>
					<div class="col-xs-6">
						<form:input path="email" htmlEscape="false" maxlength="32"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">家庭住址：</label>
					<div class="col-xs-6">
						<form:input path="address" htmlEscape="false" maxlength="64"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">民族：</label>
					<div class="col-xs-6">
						<form:input path="nation" htmlEscape="false" maxlength="12"
							class="input-sm form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-4">学历：</label>
					<div class="col-xs-6">
						<form:input path="education" htmlEscape="false" maxlength="10"
							class="input-sm form-control" />
					</div>
				</div>
			</div>
			<div class="wizard-card" data-cardname="group">
				<h3>照片完善</h3>
				<div class="container"
					style="width: 500px; height: 300px; overflow-y: auto;">
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed"
						style="width: 430; height: 250;">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>照片类型</th>
								<th>照片路径</th>
								<shiro:hasPermission name="guard:staff:edit">
									<th width="10">&nbsp;</th>
								</shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="staffImageList">
							<tr id="staffImageList0">
								<td class="hide"><input id="staffImageList0_id"
									name="staffImageList[0].id" type="hidden" value="${row.id}" />
									<input id="staffImageList0_delFlag"
									name="staffImageList[0].delFlag" type="hidden" value="0" /></td>
								<td><select id="staffImageList0_imageType"
									name="staffImageList[0].imageType"
									data-value="${row.imageType}" class="input-small required">
										<c:forEach items="0" var="dict">
											<option value="人员照片">人员照片</option>
										</c:forEach>
								</select></td>
								<td><form:hidden id="staffImageList0_imagePath"
										path="staffImageList[0].imagePath" htmlEscape="false"
										maxlength="255" class="input-sm" /> <sys:ckfinder
										input="staffImageList0_imagePath" type="images"
										uploadPath="/guard/staff" selectMultiple="false" maxWidth="50"
										maxHeight="50" /></td>
							</tr>

						</tbody>
						<shiro:hasPermission name="guard:staff:edit">
							<tfoot>
								<tr>
									<td colspan="4"><button type="button"
											onClick="addRow('#staffImageList', staffImageRowIdx, staffImageTpl);staffImageRowIdx = staffImageRowIdx + 1;"
											class="btn">新增</button></td>
								</tr>
							</tfoot>
						</shiro:hasPermission>
					</table>
				</div>
			</div>
			<div class="wizard-card" data-cardname="services">
				<h3>家庭成员</h3>
				<div class="container"
					style="width: 500px; height: 300px; overflow-y: auto; overflow-x: auto;">
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

				<div class="wizard-card">
					<h3>工作简历</h3>
					<div class="container"
						style="width: 500px; height: 300px; overflow-y: auto; overflow-x: auto;">
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
				<div class="wizard-error">
					<div class="alert alert-error">
						<strong>错误</strong> 提交到服务器过程中发生错误，请重新提交，或联系管理员。
					</div>
				</div>
				<div class="wizard-failure">
					<div class="alert alert-error">
						<strong>失败</strong> 提交到服务器失败，请重新提交。
					</div>
				</div>
				<div class="wizard-success">
					<div class="alert alert-success">
						<span class="create-server-name"></span> 人员创建 <strong>成功。</strong>
					</div>
					<input type="reset" value="重置" id="btnReset" /> <a
						class="btn create-another-server">继续添加</a> <span
						style="padding: 0 10px">or</span> <a class="btn im-done">完成</a>
				</div>
		</form:form>
		<!-- 照片 -->
		<script type="text/template" id="staffImageTpl">//<!--
						<tr id="staffImageList{{idx}}">
							<td class="hide">
								<input id="staffImageList{{idx}}_id" name="staffImageList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="staffImageList{{idx}}_delFlag" name="staffImageList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<select id="staffImageList{{idx}}_imageType" name="staffImageList[{{idx}}].imageType" data-value="{{row.imageType}}" class="required">
									<c:forEach items="${fns:getDictList('image_type')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="staffImageList{{idx}}_imagePath" name="staffImageList[{{idx}}].imagePath" type="hidden" value="{{row.imagePath}}" maxlength="255"/>
								<sys:ckfinder input="staffImageList{{idx}}_imagePath" type="images" uploadPath="/guard/staff" selectMultiple="false"  maxWidth="50"
					maxHeight="50" />
							</td>
							<shiro:hasPermission name="guard:staff:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffImageList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
		<script type="text/javascript">
						var staffImageRowIdx = 0, staffImageTpl = $("#staffImageTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(staff.staffImageList)};
							for (var i=1; i<data.length; i++){
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
								<input id="staffExFamilyList{{idx}}_name" name="staffExFamilyList[{{idx}}].name" type="text" value="{{row.name}}" maxlength="32" class="tableinput required"  data-validate="validateRowName"/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_age" name="staffExFamilyList[{{idx}}].age" type="text" value="{{row.age}}" class="tableinput "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_relation" name="staffExFamilyList[{{idx}}].relation" type="text" value="{{row.relation}}" maxlength="20" class="tableinput "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_work" name="staffExFamilyList[{{idx}}].work" type="text" value="{{row.work}}" maxlength="64" class="tableinput "/>
							</td>
							<td>
								<input id="staffExFamilyList{{idx}}_phone" name="staffExFamilyList[{{idx}}].phone" type="text" value="{{row.phone}}" maxlength="18" class="tableinput "/>
							</td>
							<shiro:hasPermission name="guard:staff:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffExFamilyList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
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
								<input id="staffExWorkList{{idx}}_workName" name="staffExWorkList[{{idx}}].workName" type="text" value="{{row.workName}}" maxlength="32" class="tableinput required" data-validate="validateWorkName"/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_workTime" name="staffExWorkList[{{idx}}].workTime" type="text" value="{{row.workTime}}" maxlength="64" class="tableinput "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_dept" name="staffExWorkList[{{idx}}].dept" type="text" value="{{row.dept}}" maxlength="64" class="tableinput "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_dupt" name="staffExWorkList[{{idx}}].dupt" type="text" value="{{row.dupt}}" maxlength="64" class="tableinput "/>
							</td>
							<td>
								<input id="staffExWorkList{{idx}}_certifier" name="staffExWorkList[{{idx}}].certifier" type="text" value="{{row.certifier}}" maxlength="64" class="tableinput "/>
							</td>
							<shiro:hasPermission name="guard:staff:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#staffExWorkList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
		<script type="text/javascript">
						var staffExWorkRowIdx = 0, staffExWorkTpl = $("#staffExWorkTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(staff.staffExWorkList)};
							for (var i=0; i<data.length; i++){
								addRow('#staffExWorkList', staffExWorkRowIdx, staffExWorkTpl, data[i]);
								staffExWorkRowIdx = staffExWorkRowIdx + 1;
							}
						});
					</script>
	</div>

</body>
</html>