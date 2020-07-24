<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>时区添加</title>
    <meta name="keywords" content="时区添加"/>
    <meta name="description" content="时区添加" />
    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
</head>

<body>

<form class="layui-form" id="addForm" action="${ctx}/tbmj/timezoneInfo/save2" method="post" style="margin-top: 15px;margin-right: 15px">
    <input type="hidden" id="accessParaInfoId" name="accessParaInfoId" value="${accessParaInfoId}"/>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">时区类型：</label>
            <div class="layui-input-block">
                <form:select path="timeZoneType" name="timeZoneType" id="timeZoneType" cssClass="form-control input-sm" lay-verify="required">
                    <form:options items="${fns:getDictList('time_zone_type')}"
                                  itemLabel="label" itemValue="value" htmlEscape="false" />
                </form:select>
            </div>
        </div>

        <div class="layui-inline">
            <label class="layui-form-label">时区名：</label>
            <div class="layui-input-block">
                <input type="text" name="timezoneName" id="timezoneName" lay-verify="required" lay-reqtext="时区名不能为空！" placeholder="请输入时区名" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>

    <div class="layui-btn-group demoTable" style="margin-left: 30px">
        <button type="submit" class="layui-btn" id="toCommit" lay-submit="" lay-filter="demo1">提交</button>
        <%--<button class="layui-btn" id="toCommit" data-type="getCheckData">确定</button>--%>
    </div>
</form>


<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>


    $("#addForm").submit(function () {
        var isOK=false;
        var type=$("#timeZoneType").val();
        var name=$("#timezoneName").val();

        $.post("${ctx}/tbmj/timezoneInfo/save2?"+$("#addForm").serialize(),function (data) {
            if(data.status=="ok"){
                alert(data.msg);
                parent.location.reload();
                parent.layer.close(index);
            }else{
                alert(data.msg);
            }
        });

        /*$.post("${ctx}/tbmj/timezoneInfo/checkName?timeZoneType="+type+"&timezoneName="+name,function (data) {
            if(data){
                alert(22222);
                isOK=true;
            }
        });
        alert("isOK"+isOK);
        if(isOK){

        }else{
            alert("该时区名在此类型中已存在！");
        }*/
        return false;
    });
</script>
</body>
</html>
