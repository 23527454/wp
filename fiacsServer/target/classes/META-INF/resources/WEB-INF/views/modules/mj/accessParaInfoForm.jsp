<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>门禁信息管理</title>
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
        <li><a href="${ctx}/mj/accessParaInfo/">门禁信息列表</a></li>
        <li class="active"><a
                href="${ctx}/mj/accessParaInfo/form?id=${accessParaInfo.id}">门禁信息</a></li>
    </ul>
    <br/>
    <form:form id="inputForm" modelAttribute="accessParaInfo"
               action="${ctx}/mj/accessParaInfo/save" method="post"
               class="form-horizontal">
    <form:hidden path="id" id="accessParaInfo_id"/>
    <form:hidden path="equipmentId" id="equipment_id"/>
    <sys:message content="${message}"/>
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>门继电器动作时间:</label>
        <div class="col-xs-1">
            <form:input path="doorRelayTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">秒</label>

        <label class="control-label col-xs-2"><font class="red">*</font>门开延时报警时间:</label>
        <div class="col-xs-1">
            <form:input path="doorDelayTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">秒</label>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>验证人员之间间隔时间:</label>
        <div class="col-xs-1">
            <form:input path="alarmIntervalTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>

        <label class="control-label col-xs-2"><font class="red">*</font>远程授权等待超时时间:</label>
        <div class="col-xs-1">
            <form:input path="remoteOverTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>入库操作时间:</label>
        <div class="col-xs-1">
            <form:input path="enterOperaTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>

        <label class="control-label col-xs-2"><font class="red">*</font>查库操作时间:</label>
        <div class="col-xs-1">
            <form:input path="checkOperaTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>提醒出库时间:</label>
        <div class="col-xs-1">
            <form:input path="outTipsTime" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits"/>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">分</label>

        <label class="control-label col-xs-2"><font class="red">*</font>中心授权:</label>
        <div class="col-xs-1">
            <form:select path="centerPermit" cssClass="form-control input-sm ">
                <form:options items="${fns:getDictList('yes_no')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font> 验证方式：</label>
        <div class="col-xs-1">
            <form:select path="authType" cssClass="form-control input-sm">
                <form:options items="${fns:getDictList('door_open_type')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;"></label>

        <label class="control-label col-xs-2"><font class="red">*</font> 组合开门数量：</label>
        <div class="col-xs-1">
            <form:input path="combNum" htmlEscape="false" maxlength="16"
                        class="form-control input-sm required digits" readonly="true"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>基本组合:</label><br>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2">1:</label>
        <div class="col-xs-4">
            <form:select path="base1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">2:</label>
        <div class="col-xs-4">
            <form:select path="base2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2">3:</label>
        <div class="col-xs-4">
            <form:select path="base3" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">4:</label>
        <div class="col-xs-4">
            <form:select path="base4" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2">5:</label>
        <div class="col-xs-4">
            <form:select path="base5" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">6:</label>
        <div class="col-xs-4">
            <form:select path="base6" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;">
                <form:options items="${fns:getDictList('base_group')}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>非工作时间段组合:</label>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2">1:</label>
        <div class="col-xs-4">
            <form:select path="workTime1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block">
                <form:options items="${hglist}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">2:</label>
        <div class="col-xs-4">
            <form:select path="workTime2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block">
                <form:options items="${hglist}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>TCP 断网组合：</label>
    </div>
    <div class="form-group">
        <label class="control-label col-xs-2">1:</label>
        <div class="col-xs-4">
            <form:select path="netOutAge1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block">
                <form:options items="${hglist}"
                              itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-1" style="width: 2px;padding-left:8px;padding-right:8px;">2:</label>
        <div class="col-xs-4">
            <form:select path="netOutAge2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block">
                <form:options items="${hglist}"
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
        <shiro:hasPermission name="mj:accessParaInfo:edit">
            <input id="btnSubmit" class="btn btn-primary" type="submit"
                   value="保 存"/>&nbsp;</shiro:hasPermission>
        <a id="btnCancel" href="${ctx}/mj/accessParaInfo/" class="btn btn-default">返回</a>
    </div>
</div>
</form:form>
</div>
</body>
</html>