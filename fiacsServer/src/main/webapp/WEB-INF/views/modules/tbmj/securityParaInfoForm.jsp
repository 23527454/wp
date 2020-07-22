<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>防盗信息参数管理</title>
    <meta name="decorator" content="default"/>
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
                                form.submit();
                            }
                        });

            });


    </script>
</head>
<body>
<div class="container-fluid">
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/tbmj/securityParaInfo/">防盗信息列表</a></li>
        <li class="active"><a
                href="${ctx}/tbmj/securityParaInfo/form?id=${securityParaInfo.id}">防盗信息</a></li>
    </ul>
    <br/>
    <form:form id="inputForm" modelAttribute="securityParaInfo"
               action="${ctx}/tbmj/securityParaInfo/save" method="post"
               class="form-horizontal">
    <form:hidden path="id" id="accessParaInfo_id"/>
    <form:hidden path="equipment.id" id="equipment_id"/>
    <sys:message content="${message}"/>
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>布防离开等待时间:</label>
        <div class="col-xs-1">
            <form:input path="leaveRelayTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">秒</label>

        <label class="control-label col-xs-2"><font class="red">*</font>门磁自动布防时间:</label>
        <div class="col-xs-1">
            <form:input path="doorSensorTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">秒</label>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>现场未布防提醒时间:</label>
        <div class="col-xs-1">
            <form:input path="localTipsTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">秒</label>

        <label class="control-label col-xs-2"><font class="red">*</font>现场未布防提醒告警时间:</label>
        <div class="col-xs-1">
            <form:input path="tipsAlarmTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>同类报警间隔时间:</label>
        <div class="col-xs-1">
            <form:input path="alarmIntervalTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>

        <label class="control-label col-xs-2"><font class="red">*</font>允许远程撤防:</label>
        <div class="col-xs-1">
            <form:select path="allowRemoteClose" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>允许门磁自动布防:</label>
        <div class="col-xs-1">
            <form:select path="allowDoorSensorOpen" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>
        <label class="control-label col-xs-2"><font class="red">*</font>允许出门按钮自动布防:</label>
        <div class="col-xs-1">
            <form:select path="allowDoorButtonOpen" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>允许按钮布防:</label>
        <div class="col-xs-1">
            <form:select path="allowButtonOpen" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>
        <label class="control-label col-xs-2"><font class="red">*</font>允许远程布防:</label>
        <div class="col-xs-1">
            <form:select path="allowRemoteOpen" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>允许进人员验证撤防:</label>
        <div class="col-xs-1">
            <form:select path="allowAuthClose" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>
        <label class="control-label col-xs-2"><font class="red">*</font>允许按钮撤防:</label>
        <div class="col-xs-1">
            <form:select path="allowButtonClose" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>允许人员验证解除告警:</label>
        <div class="col-xs-1">
            <form:select path="allowAuthRelieve" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>
        <label class="control-label col-xs-2"><font class="red">*</font>允许按钮解除告警:</label>
        <div class="col-xs-1">
            <form:select path="allowButtonRelieve" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>允许市电断电:</label>
        <div class="col-xs-1">
            <form:select path="allowPowerAlarm" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>
        <label class="control-label col-xs-2"><font class="red">*</font>允许电池低电压:</label>
        <div class="col-xs-1">
            <form:select path="allowBatteryAlarm" cssClass="form-control input-sm ">
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
        <shiro:hasPermission name="tbmj:securityParaInfo:edit">
            <input id="btnSubmit" class="btn btn-primary" type="submit"
                   value="保 存"/>&nbsp;</shiro:hasPermission>
        <a id="btnCancel" href="${ctx}/tbmj/securityParaInfo/" class="btn btn-default">返回</a>
    </div>
</div>
</form:form>
</div>
</body>
</html>