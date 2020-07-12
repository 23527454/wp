<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>款箱调拨同步信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">

    var synOpen = true;
	$(document).ready(function() {
		if($("#contentTable tr[is-download='0']").length > 0){
			setInterval(function(){
				if(top.$.jBox.FadeBoxCount ==0&&synOpen){
					$("#searchForm").submit();
				}
			}, 5000);
		}
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function myModal() {
	        synOpen=false;
    		$('#myModal').modal({
    			keyboard : true
    		});
    		$('#serName').val('');
    		$('#password').val('');
    }
    function myModal1() {
	        synOpen=false;
    		$('#myScrsModal').modal({
    			keyboard : true
    		});
    		$('#jserName').val('');
    		$('#jpassword').val('');
    }

    function myModal2() {
        var userName = $('#userName').val();
        var password = $('#password').val();

        if(!userName||!password){
            alert('请填写完整信息');
            return;
        }
           loading('正在同步，请稍等...');
         $.ajax({
            type: "POST",
            url: "${ctx}/guard/synOtherSystem/loginOtherSystem",
            data: {
                'userName':userName,
                'password':password,
                'systemType':'VMS'
            },
            dataType: "json"
        }).done(function(response) {
            closeLoading();
            top.$.jBox.tip('同步成功！');
            synOpen=true;
            $('#myModal').modal("hide");
            $("#searchForm").submit();
        }).fail(function(response) {
            closeLoading();
            top.$.jBox.tip('验证失败！');
            //synOpen=true;
            //$('#myModal').modal("hide");
        });
    }

    function myModal3() {
        var userName = $('#juserName').val();
        var password = $('#jpassword').val();

        if(!userName||!password){
            alert('请填写完整信息');
            return;
        }
           loading('正在同步，请稍等...');
         $.ajax({
            type: "POST",
            url: "${ctx}/guard/synOtherSystem/loginOtherSystem",
            data: {
                'userName':userName,
                'password':password,
                'systemType':'SCRS'
            },
            dataType: "json"
        }).done(function(response) {
            closeLoading();
            top.$.jBox.tip('同步成功！');
            synOpen=true;
            $('#myModal').modal("hide");
            $("#searchForm").submit();
        }).fail(function(response) {
            closeLoading();
            top.$.jBox.tip('验证失败！');
            //synOpen=true;
            //$('#myModal').modal("hide");
        });
    }
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/guard/downloadMoneyBoxAllot/">款箱调拨同步信息列表</a></li>
		<%-- <shiro:hasPermission name="guard:downloadMoneyBoxAllot:edit">
			<li><a href="${ctx}/guard/downloadMoneyBoxAllot/form">款箱调拨同步信息添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="downloadMoneyBoxAllot"
		action="${ctx}/guard/downloadMoneyBoxAllot/" method="post"
		class="breadcrumb form-search form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>网点：</label>
			<sys:treeselect id="office" name="office.id"
				value="${downloadMoneyBoxAllot.office.id}" labelName="office.name"
				labelValue="${downloadMoneyBoxAllot.office.name}" title="网点"
				url="/sys/office/treeData" cssClass="" allowClear="true"
				notAllowSelectRoot="true" />
		</div>
		<div class="form-group">
			<label>款箱编码：</label>
			<form:input path="moneyBox.boxCode" htmlEscape="false"
				class="form-control input-sm" />
		</div>
		<div class="form-group">
			<label>开始时间：</label> <input name="downloadTime" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadMoneyBoxAllot.downloadTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label>截止时间：</label> <input name="downloadTimeTwo" type="text"
				readonly="readonly" maxlength="20" class="form-control input-sm Wdate"
				value="${downloadMoneyBoxAllot.downloadTimeTwo}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
		</div>
		
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 30px;">序号</th>
				<th>款箱编号</th>
				<th>款箱卡号</th>
				<th>款箱类型</th>
				<th>同步时间</th>
				<th>同步类型</th>
				<th>同步状态</th>
				<th>网点</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="downloadMoneyBoxAllot" varStatus="varStatus">
				<tr is-download="${downloadMoneyBoxAllot.isDownload }">
					<td style="text-align: center;">${varStatus.count }</td>
					<td>${downloadMoneyBoxAllot.moneyBox.boxCode}</td>
					<td>${downloadMoneyBoxAllot.moneyBox.cardNum}</td>
					<td>
						${fns:getDictLabel(downloadMoneyBoxAllot.moneyBox.boxType, 'box_type', '')}
					</td>
					<td>${downloadMoneyBoxAllot.downloadTime}</td>
					 <td>${fns:getDictLabel(downloadMoneyBoxAllot.isDownload, 'isDownload', '')} 
					<%-- <td>${fns:getDictLabel(downloadMoneyBoxAllot.downloadType, 'downloadType', '')}
					</td> --%>
					<%-- <td>${fns:getDictLabel(downloadMoneyBoxAllot.isDownload, 'isDownload', '')} 	</td>--%>
					<c:if test="${downloadMoneyBoxAllot.downloadType == '0'}">
						<td>添加</td>
							</c:if>
					<c:if test="${downloadMoneyBoxAllot.downloadType == '1'}">
						<td>更新</td>
							</c:if>
					<c:if test="${downloadMoneyBoxAllot.downloadType == '2'}">
						<td>删除</td>
							</c:if>
				
					<td>${downloadMoneyBoxAllot.office.name}</td>
					<td>
						<shiro:hasPermission name="guard:downloadMoneyBoxAllot:edit">
							<c:if test="${downloadMoneyBoxAllot.isDownload == '0'}">
								<a href="${ctx}/guard/downloadMoneyBoxAllot/delete?id=${downloadMoneyBoxAllot.id}"
								onclick="return confirmx('确认要取消该同步记录吗？', this.href)">取消</a>
							</c:if>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
    		aria-labelledby="myModalLabel" aria-hidden="true">
    		<div class="modal-dialog" style="width: 650px;">
    			<div class="modal-content">
    				<div class="modal-header">
    					<button type="button" class="close" data-dismiss="modal"
    						aria-hidden="true">&times;</button>
    					<h4 class="modal-title" id="myModalLabel">金库系统账号验证</h4>
    				</div>
    				<!-- 表单 -->
    				<div class="modal-body" style="width: 600px;">

    				<input id="userName" name="userName" placeholder="金库系统账号" type="text" style="width: 200px;margin-bottom: 5px;">
    				<input id="password" name="password" type="password" placeholder="金库系统密码" style="width: 200px;margin-bottom: 5px;">
    				<input type="button" value="确认同步" class="btn btn-primary" onclick="myModal2()"/>
    				</div>
    			</div>
    		</div>
    	</div>

    	<div class="modal fade" id="myScrsModal" tabindex="-1" role="dialog"
            aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog" style="width: 650px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">加配钞系统账号验证</h4>
                    </div>
                    <!-- 表单 -->
                    <div class="modal-body" style="width: 600px;">

                    <input id="juserName" name="juserName" placeholder="加配钞系统账号" type="text" style="width: 200px;margin-bottom: 5px;">
                    <input id="jpassword" name="jpassword" type="password" placeholder="加配钞系统密码" style="width: 200px;margin-bottom: 5px;">
                    <input type="button" value="确认同步" class="btn btn-primary" onclick="myModal3()"/>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>