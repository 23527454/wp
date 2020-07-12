<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>线路信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
		$(document).ready(function() {
			
			var line_name = $("#line_name").val();
			var area_id = parent.$("#area_id").val();
			var area_name = parent.$("#area_name").val();
			
			$("#selectedAreaId").val(area_id);
			if (line_name == "" || typeof (line_name) == "undefined") {
				$("#areaId").val(area_id);
				$("#areaName").val(area_name);
			}
			
			if (document.all['radio_hidden'].value == '0') {
				document.all['radio'][0].checked = true;
			} else if (document.all['radio_hidden'].value == '1') {
				document.all['radio'][1].checked = true;
			} else {
				document.all['radio'][0].checked = true;
				$("#radio_hidden").val("0");
			}
			
			
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
			
			if($("#areaId").val()){
				$("#lineNodesTfoot").show();
			}
			
		});
		function addRow(list, idx, tpl, row){
			var idxo=idx+1;
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row, areaId:$("#areaId").val()
			}));
			var nodeIunterval=$("#nodeIunterval").val();
			$("#lineNodesList"+idx+"_nodeNextGap").val(nodeIunterval)
			$("#lineNodesList"+idx+"_nodeSn").val(idxo);
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
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
		
		function getRadio() {
			e = event.srcElement;
			if (e.type == "radio" && e.name == "radio") {
				if (e.value == 0) {
					$("#radio_hidden").val(0);
				} else if (e.value == 1) {
					$("#radio_hidden").val(1);
				}
			}
		}
		
		function areaTreeselectCallBack(nodes, v, h, f){
			//$("#lineNodesList").empty();
			$("#lineNodesList tr").each(function(idx, elemt){
				delRow("#"+$(elemt).attr("id"));
			})
			$("#lineNodesTfoot").show();
		}
	</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/line/list?area.id=${selectedAreaId}">线路信息列表</a></li>
		<li class="active"><a href="${ctx}/guard/line/form?id=${line.id}&selectedAreaId=${selectedAreaId}">线路信息<shiro:hasPermission
					name="guard:line:edit">${not empty line.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:line:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="line"
		action="${ctx}/guard/line/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<input type="hidden" name="selectedAreaId" id="selectedAreaId"/>
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>线路名称：</label>
			<div class="col-xs-3">
				<form:input path="lineName" htmlEscape="false" maxlength="48"
					class="form-control input-sm required" id="line_name" />
			</div>
			<form:errors path="lineName" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>所属区域：</label>
			<div class="col-xs-3">
				<sys:treeselect id="area" name="area.id" value="${line.area.id}"
					labelName="area.name" labelValue="${line.area.name}" title="区域"
					url="/sys/area/treeData" cssClass="required" allowClear="true"
					notAllowSelectRoot="true" />
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2"><font class="red">*</font>前误差时间：</label>
			<div class="col-xs-3">
				<form:input path="allowWrongTimeBeFore" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label class="control-label col-xs-2"><font class="red">*</font>后误差时间：</label>
			<div class="col-xs-3">
				<form:input path="allowWrongTimeAfter" htmlEscape="false"
					class="form-control input-sm required" />
			</div>
		</div>
		<form:input path="lineOrder" id="radio_hidden" type="hidden" />

		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>线路顺序：</label>
			<div class="col-xs-3">
				<div class="radio-inline">
					<span onclick="getRadio();"> 
						<input type="radio" name="radio" value="0"><label>无序</label>
						<input type="radio" name="radio" value="1"><label>有序</label>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>节点时间间隔：</label>
			<div class="col-xs-3">	
				<div class="input-group">
					<form:input path="nodeIunterval" htmlEscape="false"
						class="form-control input-sm required digits"  id="nodeIunterval"/>
					<span class="input-group-addon input-sm">分钟</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注：</label>
			<div class="col-xs-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="255" class="form-control input-sm " />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">线路节点信息表：</label>
			<div class="col-xs-5">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th class="hide"></th>
							<th style="width: 500px">营业网点</th>
							<th style="width: 150px">节点序号</th>
							<th style="width: 150px">与下节点间隔时间</th>
							<shiro:hasPermission name="guard:line:edit">
								<th style="width: 37px;">&nbsp;</th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody id="lineNodesList">
					</tbody>
					<shiro:hasPermission name="guard:line:edit">
						<tfoot id="lineNodesTfoot" style="display: none;">
							<tr>
								<td colspan="5"><a href="javascript:"
									onclick="addRow('#lineNodesList', lineNodesRowIdx, lineNodesTpl);lineNodesRowIdx = lineNodesRowIdx + 1;"
									class="btn">新增</a></td>
							</tr>
						</tfoot>
					</shiro:hasPermission>
				</table>
				<script type="text/template" id="lineNodesTpl">//<!--
						<tr id="lineNodesList{{idx}}">
							<td class="hide">
								<input id="lineNodesList{{idx}}_id" name="lineNodesList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="lineNodesList{{idx}}_delFlag" name="lineNodesList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>

								<sys:treeselect id="lineNodesList{{idx}}_office" name="lineNodesList[{{idx}}].office.id" value="{{row.office.id}}" labelName="lineNodesList{{idx}}.office.name" labelValue="{{row.office.name}}"
									title="网点" url="/guard/line/officeTreeData?areaId={{areaId}}" cssClass="required" allowClear="true"></sys:treeselect>
						
							</td>
							<td>
								<input id="lineNodesList{{idx}}_nodeSn" name="lineNodesList[{{idx}}].nodeSn" type="text"  class="form-control input-sm required" />
							</td>
							<td>
								<div class="input-group">
								<input id="lineNodesList{{idx}}_nodeNextGap" name="lineNodesList[{{idx}}].nodeNextGap" type="text" value="{{row.nodeNextGap}}" class="form-control input-sm required digits"/>
								<span class="input-group-addon input-sm">分钟</span>
								</div>
							</td>
							<td class="text-center" width="10">
								<shiro:hasPermission name="guard:line:edit">
									{{#delBtn}}<span class="btn close" onclick="delRow('#lineNodesList{{idx}}')" title="删除"><i class="glyphicon glyphicon-trash"></i></span>{{/delBtn}}
								</shiro:hasPermission>
							</td>

						</tr>//-->
					</script>
				<script type="text/javascript">
						var lineNodesRowIdx = 0, lineNodesTpl = $("#lineNodesTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(line.lineNodesList)};
							for (var i=0; i<data.length; i++){
								addRow('#lineNodesList', lineNodesRowIdx, lineNodesTpl, data[i]);
								lineNodesRowIdx = lineNodesRowIdx + 1;
							}
						});
						function treeselectCallBack(nodes,v, h, f){
							if(nodes && nodes.length > 0){
								var name = nodes[0].name;								
								for(var i=0;i<lineNodesRowIdx;i++){
									if(name ==  $("#lineNodesList"+i+"_officeName").val()){
										top.$.jBox.tip("已选择该节点，请重新选择。");
										return false;
									}
								}
							}
							return true;
						}
					</script>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:line:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</form:form>
</div>
</body>
</html>