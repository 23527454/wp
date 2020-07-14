<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>员工选择</title>
    <meta name="keywords" content="员工选择"/>
    <meta name="description" content="员工选择" />
    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
</head>

<body>
<form:form id="searchForm"
           action="${ctx}/guard/staff/selStaff" method="post"
           class="layui-form" cssStyle="margin: 0px;padding: 0px;padding-top: 10px">
<div class="layui-inline">
    <label class="layui-form-label">姓名：</label>
    <div class="layui-input-inline">
        <input type="text" id="name" name="name" htmlEscape="false" value="${name}"
               class="layui-input" />
    </div>
</div>
<div class="layui-inline">
    <label class="layui-form-label">工号：</label>
    <div class="layui-input-inline">
        <input type="text" id="workNum" name="workNum" htmlEscape="false" value="${workNum}"
               class="layui-input" />
    </div>
</div>

<div class="layui-inline">
    <div class="layui-input-inline">
        <input id="btnSubmit" class="layui-btn" type="submit" value="查询" />
    </div>
</div>
</form:form>

<table class="layui-table" lay-data="{ url:'${ctx}/guard/staff/selStaff', page:true, id:'idTest',request: {pageName: 'pageIndex',limitName: 'size'}}" lay-filter="demo">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left'}"></th>
        <th lay-data="{field:'id', minWidth:100, sort: true, fixed: true, align:'center'}">ID</th>
        <th lay-data="{field:'name', minWidth:100, align:'center'}">姓名</th>
        <th lay-data="{field:'workNum', minWidth:200, sort: true, align:'center'}">工号</th>
        <th lay-data="{field:'dept', align:'center'}">部门</th>
        <th lay-data="{field:'phone', align:'center'}">联系电话</th>
    </tr>
    </thead>
</table>

<div class="layui-btn-group demoTable">
    <button class="layui-btn" data-type="getCheckData">确定</button>
</div>

<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>


    layui.use('table', function(){
        var table = layui.table;
        //监听表格复选框选择
        table.on('checkbox(demo)', function(obj){
            console.log(obj)
        });


        var $ = layui.$, active = {
            getCheckData: function(){ //获取选中数据
                var checkStatus = table.checkStatus('idTest')
                    ,data = checkStatus.data;
                layer.alert(JSON.stringify(data));
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

    });
</script>
</html>
