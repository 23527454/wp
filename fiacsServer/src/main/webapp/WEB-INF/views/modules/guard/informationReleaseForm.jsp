<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>信息发布管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript">
	$(document).ready(
			function() {
			$("#inputForm").validate({
                            submitHandler : function(form) {
                                if(!document.getElementById('title').value){
                                    alert('标题不能为空');
                                    return;
                                }
                                if(!document.getElementById('content').value){
                                     alert('内容不能为空');
                                    return;
                                }

                                 if(!document.getElementById('beginTime').value||!document.getElementById('endTime').value){
                                     alert('时间范围不能为空');
                                    return;
                                }

                                var oDate1 = new Date(document.getElementById('beginTime').value);
                                 var oDate2 = new Date(document.getElementById('endTime').value);
                                    if(oDate1.getTime() > oDate2.getTime()){
                                           alert('结束时间不能小于起始时间');
                                          return;
                                        }
                                var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
                                if(nodes2.length==0){
                                      alert('请选择通知范围');
                                    return;
                                }
                                for(var i=0; i<nodes2.length; i++) {
                                    ids2.push(nodes2[i].id);
                                }
                                $("#scope").val(ids2);
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
            			tree2.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
            			// 默认选择节点
            			var ids2 = "${info.scope}".split(",");
            			for(var i=0; i<ids2.length; i++) {
            				var node = tree2.getNodeByParam("id", ids2[i]);
            				try{tree2.checkNode(node, true, false);}catch(e){}
            			}
            			// 默认展开全部节点
            			tree2.expandAll(true);
            			// 刷新（显示/隐藏）机构


	});

</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/guard/informationRelease/">信息发布列表</a></li>
		<li class="active"><a
			href="${ctx}/guard/informationRelease/form?id=${info.id}">信息发布<shiro:hasPermission
					name="guard:informationRelease:edit">${not empty info.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="guard:informationRelease:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="info" enctype="multipart/form-data"
		action="${ctx}/guard/informationRelease/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" id="id"/>
		<sys:message content="${message}" />
		<div class="row">
		<div class="col-xs-1">
		<label class="control-label" style="margin-left:60px;width:100px;">通知范围:</label>
		</div>
		<div class="col-xs-3">
		    <div id="officeTree" class="ztree"
        					style="margin-left:60px;margin-top:3px;float:left;overflow:auto;height:330px;"></div>
        				<form:hidden path="scope" id="scope"/>
		    </div>
		<div class="col-xs-8">
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>标题：</label>
			<div class="col-xs-8">
			<form:input  path="title"
					class="form-control input-sm required alnum" id="title" />
			</div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2"><font class="red">*</font>开始时间：</label>
           <div class="col-xs-3">
                 <input name="beginTime" type="text"
                maxlength="20" class="form-control input-sm Wdate required" style="height: auto;" readonly="readonly"
                id="beginTime"
                value="<fmt:formatDate value="${info.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
                data-validate="validateStartDate" />
            </div>
            <div class="col-xs-2">
            <label class="control-label">结束时间：</label>
            </div>
            <div class="col-xs-3">
                 <input name="endTime" type="text"
                maxlength="20" class="form-control input-sm Wdate required" style="height: auto;" readonly="readonly"
                id="endTime"
               value="<fmt:formatDate value="${info.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"
                data-validate="validateStartDate" />
            </div>
        </div>
        <div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font>信息内容：</label>
			<div class="col-xs-8">
				<form:textarea path="content" htmlEscape="false" rows="13" id="content"
					maxlength="1000" class="input-xxlarge required" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<shiro:hasPermission name="guard:informationRelease:view">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="${not empty info.id?'修改':'保存'}" />&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
		</div>
		</div>
	</form:form>
</div>
</body>
</html>