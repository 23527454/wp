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
            });


    </script>
</head>
<body>
<div class="container-fluid">
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/tbmj/accessParaInfo/">门禁信息列表</a></li>
        <li class="active"><a
                href="${ctx}/tbmj/accessParaInfo/form?id=${accessParaInfo.id}">门禁信息修改</a></li>
    </ul>
    <br/>
    <form:form id="inputForm" modelAttribute="accessParaInfo"
               action="${ctx}/tbmj/accessParaInfo/save" method="post"
               class="form-horizontal">
    <form:hidden path="id" id="accessParaInfo_id"/>
    <form:hidden path="equipmentId" id="equipment_id"/>
    <sys:message content="${message}"/>

    <h4 class="section-title" data-toggle="collapse" data-target="#accessParaBasicInfo"  aria-expanded="false" aria-controls="accessParaBasicInfo">基本参数</h4>
    <section class="collapse in" id="accessParaBasicInfo">
        <div class="form-group">
            <label class="control-label col-xs-2"><font class="red">*</font>门继电器动作时间:</label>
            <div class="col-xs-1">
                <form:input path="doorRelayTime" htmlEscape="false" maxlength="16"
                            class="form-control input-sm required digits"/>
            </div>
            <label class="control-label col-xs-2" style="text-align:left;padding-left:8px;padding-right:8px;">秒</label>

            <label class="control-label col-xs-2"><font class="red">*</font>门开延时报警时间:</label>
            <div class="col-xs-1">
                <form:input path="doorDelayTime" htmlEscape="false" maxlength="16"
                            class="form-control input-sm required digits"/>
            </div>
            <label class="control-label col-xs-2" style="text-align:left;">秒</label>
        </div>

        <div class="form-group">
            <label class="control-label col-xs-2"><font class="red">*</font>验证人员之间间隔时间:</label>
            <div class="col-xs-1">
                <form:input path="alarmIntervalTime" htmlEscape="false" maxlength="16"
                            class="form-control input-sm required digits"/>
            </div>
            <label class="control-label col-xs-2" style="text-align:left;padding-left:8px;padding-right:8px;">分</label>

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
            <label class="control-label col-xs-2" style="text-align:left;padding-left:8px;padding-right:8px;">分</label>

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
            <label class="control-label col-xs-2" style="text-align:left;padding-left:8px;padding-right:8px;">分</label>

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
            <label class="control-label col-xs-2" style="text-align:left;padding-left:8px;padding-right:8px;"></label>

            <label class="control-label col-xs-2"><font class="red">*</font> 组合开门数量：</label>
            <div class="col-xs-1">
                <form:input path="combNum" htmlEscape="false" maxlength="16"
                            class="form-control input-sm required digits" readonly="true"/>
            </div>
        </div>
    </section>



    <h4 class="section-title" data-toggle="collapse" data-target="#accessParaAuthorInfo"  aria-expanded="false" aria-controls="accessParaAuthorInfo">权限设置</h4>
    <section class="collapse in" id="accessParaAuthorInfo">
        <h5 class="section-title" data-toggle="collapse" data-target="#baseGroup"  aria-expanded="false" aria-controls="baseGroup" style="text-align:left;padding-left: 5%">基本组合：</h5>
        <section class="collapse in" id="baseGroup">
            <div class="form-group">
                <label class="control-label col-xs-2">1:</label>
                <div class="col-xs-4">
                    <form:select path="base1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-2" style="width: 6%;padding-left:8px;padding-right:8px;">2:</label>
                <div class="col-xs-4">
                    <form:select path="base2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2">3:</label>
                <div class="col-xs-4">
                    <form:select path="base3" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-1" style="width: 6%;padding-left:8px;padding-right:8px;">4:</label>
                <div class="col-xs-4">
                    <form:select path="base4" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2">5:</label>
                <div class="col-xs-4">
                    <form:select path="base5" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-1" style="width: 6%;padding-left:8px;padding-right:8px;">6:</label>
                <div class="col-xs-4">
                    <form:select path="base6" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block;" onchange="checkContent(this);">
                        <form:options items="${fns:getDictList('base_group')}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </section>


        <h5 class="section-title" data-toggle="collapse" data-target="#workTime"  aria-expanded="false" aria-controls="workTime" style="text-align:left;padding-left: 5%">非工作时间段组合：</h5>
        <section class="collapse in" id="workTime">
            <div class="form-group">
                <label class="control-label col-xs-2">1:</label>
                <div class="col-xs-4">
                    <form:select path="workTime1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block" onchange="change2()">
                        <form:options items="${hglist}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-1" style="width: 6%;padding-left:8px;padding-right:8px;">2:</label>
                <div class="col-xs-4">
                    <form:select path="workTime2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block" onchange="change2()">
                        <form:options items="${hglist}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </section>

        <h5 class="section-title" data-toggle="collapse" data-target="#netOutAge"  aria-expanded="false" aria-controls="netOutAge" style="text-align:left;padding-left: 5%">TCP断网组合：</h5>
        <section class="collapse in" id="netOutAge">
            <div class="form-group">
                <label class="control-label col-xs-2">1:</label>
                <div class="col-xs-4">
                    <form:select path="netOutAge1" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block" onchange="change3()">
                        <form:options items="${hglist}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <label class="control-label col-xs-1" style="width: 6%;padding-left:8px;padding-right:8px;">2:</label>
                <div class="col-xs-4">
                    <form:select path="netOutAge2" cssClass="form-control input-sm" cssStyle="width: 30%;display: inline-block" onchange="change3()">
                        <form:options items="${hglist}"
                                      itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </section>

    </section>



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
        <shiro:hasPermission name="tbmj:accessParaInfo:edit">
            <input id="btnSubmit" class="btn btn-primary" type="submit"
                   value="保 存"/>&nbsp;</shiro:hasPermission>
        <a id="btnCancel" href="${ctx}/tbmj/accessParaInfo/" class="btn btn-default">返回</a>
    </div>
</div>
</form:form>
</div>

<script>
    var commit1=true;
    var commit2=true;
    var commit3=true;

    function checkContent() {
        var base1=$("#base1").val();
        var base2=$("#base2").val();
        var base3=$("#base3").val();
        var base4=$("#base4").val();
        var base5=$("#base5").val();
        var base6=$("#base6").val();
        var num1=0;
        var num2=0;
        if(base1=='00'){
            num1=num1-1;
        }else if(base1!='00'){
            num2=num2+1;
        }

        if(base2=='00'){
            num1=num1-1;
        }else if(base2!='00'){
            num2=num2+1;
        }

        if(base3=='00'){
            num1=num1-1;
        }else if(base3!='00'){
            num2=num2+1;
        }

        if(base4=='00'){
            num1=num1-1;
        }else if(base4!='00'){
            num2=num2+1;
        }

        if(base5=='00'){
            num1=num1-1;
        }else if(base5!='00'){
            num2=num2+1;
        }

        if(base6=='00'){
            num1=num1-1;
        }else if(base6!='00'){
            num2=num2+1;
        }


        if(num2<=0){
            $("#combNum").val(2);
        }else{
            $("#combNum").val(num2);
        }

        var base=[base1,base2,base3,base4,base5,base6];
        var base2=[];
        for(var i=0;i<base.length;i++){
            if(base[i]!='00'){
                base2.push(base[i]);
            }
        }

        if(mm(base2)){
            commit1=false;
            alert("基础组合中不能存在相同权限！");
        }else{
            commit1=true;
        }
    }


    function change2(){
        var workTime1=$("#workTime1").val();
        var workTime2=$("#workTime2").val();


        var workTime=[workTime1,workTime2];
        var workTime_bk=[];
        for(var i=0;i<workTime.length;i++){
            if(workTime[i]!='00'){
                workTime_bk.push(workTime[i]);
            }
        }

        if(mm(workTime_bk)){
            commit2=false;
            alert("非工作时间段组合中不能存在相同权限！");
        }else{
            commit2=true;
        }
    }

    function change3(){
        var netOutAge1=$("#netOutAge1").val();
        var netOutAge2=$("#netOutAge2").val();


        var netOutAge=[netOutAge1,netOutAge2];
        var netOutAge_bk=[];
        for(var i=0;i<netOutAge.length;i++){
            if(netOutAge[i]!='00'){
                netOutAge_bk.push(netOutAge[i]);
            }
        }

        if(mm(netOutAge_bk)){
            commit3=false;
            alert("TCP断网组合中不能存在相同权限！");
        }else{
            commit3=true;
        }
    }

    function mm(a){return /(\x0f[^\x0f]+)\x0f[\s\S]*\1/.test("\x0f"+a.join("\x0f\x0f") +"\x0f");}


    $("#inputForm")
        .validate(
            {
                submitHandler: function (form) {
                    if(commit1&&commit2&&commit3){
                        loading('正在提交，请稍等...');
                        form.submit();
                    }else if(!commit1){
                        alert("基础组合中不能存在相同权限！");
                    }else if(!commit2){
                        alert("非工作时间段组合中不能存在相同权限！");
                    }else if(!commit3){
                        alert("TCP断网组合中不能存在相同权限！");
                    }
                }
            });
</script>
</body>
</html>