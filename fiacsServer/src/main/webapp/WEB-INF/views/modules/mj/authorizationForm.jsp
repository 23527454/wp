<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>权限信息管理</title>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
    <script type="text/javascript">

        $(document).ready(

            function () {
                var id = $("#accessParaInfo_id").val();
                $("#equipment_ids").val(id);
                if (id != "" && typeof (id) != "undefined") {
                    $("#officeButton").addClass("disabled");
                } else {
// 					$("#ip").val("192.168.1.118");

                    var office_id = parent.$("#office_id").val();
                    var office_name = parent.$("#office_name").val();
                    $("#controlPos").val(office_name);
                    var office_type = parent.$("#office_type").val();

                    if (office_id != null && office_id != "" && office_id != "0" && typeof (office_id) != "undefined") {
                        $.ajax({
                            type: "GET",
                            url: "${ctx}/guard/equipment/getNumber?id=" + office_id
                        }).done(function (response) {
                            var data = JSON.parse(response);
                            var data_number = data.number;
                            if (data_number == "0") {
                                $("#officeName").val(office_name);
                                $("#controlPos").val(office_name);
                                //网点ID
                                $("#equipment_ids").val(office_id);
                                $("#officeId").val(office_id);
                            }
                        }).fail(function () {
                        });
                    }
                }


                $("#inputForm")
                    .validate(
                        {
                            submitHandler: function (form) {
                                loading('正在提交，请稍等...');
                                $.post("${ctx}/mj/authorization/save",);
                                form.submit();
                            }
                        });

                var message="${message}";
                if(message!='')
                {
                    top.$.jBox.tip("${message}","",{persistent:true,opacity:0});$("#messageBox").show();
                }
            });
    </script>
</head>
<body>
<sys:message content="${message}" />
<div class="container-fluid">
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/mj/authorization/">权限信息列表</a></li>
        <li class="active"><a
                href="#">权限信息${isNew?"添加":"修改"}</a></li>
    </ul>
    <br/>
    <form:form id="inputForm" modelAttribute="authorization"
               action="${ctx}/mj/authorization/save" method="post"
               class="form-horizontal">
    <sys:message content="${message}"/>
    <input type="hidden" name="id" id="id" value="${authorization.id}" />
    <input type="hidden" name="selStaffIds" id="selStaffIds" value="${selStaffIds}" />
    <input type="hidden" name="accessParaInfoId" id="accessParaInfoId" value="${authorization.accessParaInfoId}" />
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>时区号:</label>
        <div class="col-xs-2">
            <form:select path="timezoneInfoNum" cssClass="form-control input-sm">
                <form:options items="${fns:getDictList('time_zone_num')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-2"><font class="red">*未完成</font>工作日期号:</label>
        <div class="col-xs-2">
            <select id="workDayNum" name="workDayNum" style="width: 100%">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>人员分组:</label>
        <div class="col-xs-2">
            <form:select path="staffGroup" cssClass="form-control input-sm">
                <form:options items="${fns:getDictList('staff_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-2"><font class="red">*</font>是否允许查库:</label>
        <div class="col-xs-2">
            <form:select path="checkPom" cssClass="form-control input-sm">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2">备注：</label>
        <div class="col-xs-3">
            <form:textarea path="remarks" htmlEscape="false" rows="4"
                           maxlength="255" class="input-xxlarge " />
        </div>
    </div>

</div>
<div class="form-group">
    <div class="col-xs-offset-2 col-xs-10">
        <shiro:hasPermission name="mj:authorization:edit">
            <input id="btnSubmit" class="btn btn-primary" type="submit"
                   value="保 存"/>&nbsp;</shiro:hasPermission>
        <a id="btnCancel" href="${ctx}/mj/authorization/" class="btn btn-default">返回</a>
    </div>
</div>
</form:form>
</div>

<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>
    $("#selStaffNames").click(function(){
        layer.open({
            type: 2,
            title: '员工选择', //将姓名设置为红色
            shadeClose: true,           //弹出框之外的地方是否可以点击
            offset: '10%',
            area: ['60%', '80%'],
            content: '${ctx}/guard/staff/plan1?accessParaInfoId=${authorization.accessParaInfoId}',
            btn: ['确定'],
            btnAlign: 'l',
            yes: function(index){
                //当点击‘确定’按钮的时候，获取弹出层返回的值
                var res = window["layui-layer-iframe" + index].callbackdata();
                //打印返回的值，看是否有我们想返回的值。
                console.log(res);
                $("#selStaffIds").val(res.selIds);
                $("#selStaffNames").val(res.selNames);
                //最后关闭弹出层
                layer.close(index);
            }
        });
    });
</script>
</body>
</html>