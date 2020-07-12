<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>权限信息管理</title>
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
                href="${ctx}/mj/authorization/form?id=${authorization.id}">权限信息${isNew?"添加":"修改"}</a></li>
    </ul>
    <br/>
    <form:form id="inputForm" modelAttribute="authorization"
               action="${ctx}/mj/authorization/save" method="post"
               class="form-horizontal">
    <c:if test="${authorization.id!=null}">
        <form:hidden path="id" id="accessParaInfo_id"/>
    </c:if>
    <sys:message content="${message}"/>
    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>门号:</label>
        <div class="col-xs-2">
            <form:select path="accessParaInfoId" class="form-control input-sm required" >
                <form:options items="${accessParaInfos}" itemLabel="doorPos" itemValue="id" htmlEscape="false"/>
            </form:select>
        </div>

        <label class="control-label col-xs-2"><font class="red">*</font>员工名称:</label>
        <div class="col-xs-2">
            <form:select path="staffId" class="form-control input-sm required">
                <form:options items="${staffs}" itemLabel="name" itemValue="id" htmlEscape="false"/>
            </form:select>
            <form:errors path="staffName" cssClass="error"></form:errors>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*未完成</font>时区号:</label>
        <div class="col-xs-2">
            <select id="timezoneInfoNum" name="timezoneInfoNum" style="width: 100%">
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
    <%--<div class="form-group">
        <label class="control-label col-xs-2"><font class="red">*</font>门禁有效期:</label>
        <div class="col-xs-2">
            <form:input path="validityDate" onclick="WdatePicker({readOnly:true,isShowToday:false})" htmlEscape="false" maxlength="32"
                        class="form-control input-sm required netMask" />
        </div>
    </div>--%>

    <div class="form-group">
        <label class="control-label col-xs-2">备注：</label>
        <div class="col-xs-3">
            <form:textarea path="remarks" htmlEscape="false" rows="4"
                           maxlength="255" class="input-xxlarge " />
        </div>
    </div>

    <%--<c:choose>
        <c:when test="${isNew}">
            <div class="form-group">
                <label class="control-label col-xs-2"><font class="red">*</font>门号:</label>
                <div class="col-xs-2">
                    <form:select path="accessParaInfoId" class="form-control input-sm required" >
                        <form:options items="${accessParaInfos}" itemLabel="doorPos" itemValue="id" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-2"><font class="red">*</font>员工名称:</label>
                <div class="col-xs-2">
                    <form:select path="staffId" class="form-control input-sm required">
                        <form:options items="${staffs}" itemLabel="name" itemValue="id" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-2"><font class="red">*</font>时区号:</label>
                <div class="col-xs-2">
                    <select id="timezoneInfoNum" name="timezoneInfoNum" style="width: 100%">
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

                <label class="control-label col-xs-2"><font class="red">*</font>工作日期号:</label>
                <div class="col-xs-2">
                    <select id="workDayNum" name="timezoneInfoNum" style="width: 100%">
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
                <label class="control-label col-xs-2"><font class="red">*</font>人员门禁分组:</label>
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
                <label class="control-label col-xs-2"><font class="red">*</font>门禁有效期:</label>
                <div class="col-xs-2">
                    <form:input path="validityDate" onclick="WdatePicker({readOnly:true,isShowToday:false})" htmlEscape="false" maxlength="32"
                                class="form-control input-sm required netMask" />
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-2">备注：</label>
                <div class="col-xs-3">
                    <form:textarea path="remarks" htmlEscape="false" rows="4"
                                   maxlength="255" class="input-xxlarge " />
                </div>
            </div>
            &lt;%&ndash;<form:hidden path="id" />&ndash;%&gt;
        </c:when>

        <c:otherwise>
            &lt;%&ndash;<form:hidden path="id" />&ndash;%&gt;
            <div class="form-group">
                <label class="control-label col-xs-2"><font class="red">*</font>门号:</label>
                <div class="col-xs-2">
                    <form:select path="accessParaInfoId" class="form-control input-sm required" >
                        &lt;%&ndash;<c:forEach items="${accessParaInfos}" var="a" varStatus="varStatus">
                            <option value="${a.id}" <c:if test="${authorization.accessParaInfoId == a.id}">selected</c:if>>${a.doorPos}</option>
                        </c:forEach>&ndash;%&gt;
                        <form:options items="${accessParaInfos}" itemLabel="doorPos" itemValue="id" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-2"><font class="red">*</font>员工名称:</label>
                <div class="col-xs-2">
                    <form:select path="staffId" class="form-control input-sm required">
                        &lt;%&ndash;<c:forEach items="${staffs}" var="s" varStatus="varStatus">
                            <option value="${s.id}" <c:if test="${authorization.staffId == s.id}">selected</c:if>>${s.name}</option>
                        </c:forEach>&ndash;%&gt;
                        <form:options items="${staffs}" itemLabel="name" itemValue="id" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-2"><font class="red">*</font>时区号:</label>
                <div class="col-xs-2">
                    <select id="timezoneInfoNum" name="timezoneInfoNum" style="width: 100%">
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

                <label class="control-label col-xs-2"><font class="red">*</font>工作日期号:</label>
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
                <label class="control-label col-xs-2"><font class="red">*</font>人员门禁分组:</label>
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
            <label class="control-label col-xs-2"><font class="red">*</font>门禁有效期:</label>
            <div class="col-xs-2">
                <form:input path="validityDate" onclick="WdatePicker({readOnly:true,isShowToday:false})" htmlEscape="false" maxlength="32"
                            class="form-control input-sm required netMask" />
            </div>
        </div>

            <div class="form-group">
                <label class="control-label col-xs-2">备注：</label>
                <div class="col-xs-3">
                    <form:textarea path="remarks" htmlEscape="false" rows="4"
                                   maxlength="255" class="input-xxlarge " />
                </div>
            </div>
        </c:otherwise>
    </c:choose>--%>
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
</body>
</html>