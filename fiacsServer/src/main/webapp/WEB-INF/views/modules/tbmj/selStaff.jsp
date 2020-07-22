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
<div class="demoTable" style="margin: 0px;padding: 0px;padding-top: 10px">
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
            <input id="btnSubmit" class="layui-btn" type="submit" value="查询" data-type="reload"/>
        </div>
    </div>
</div>

<table class="layui-hide" id="LAY_table_user" lay-filter="user"></table>

<form class="layui-form" id="addAuthor" action="${ctx}/tbmj/authorization/save" method="post">
    <input type="hidden" id="accessParaInfoId" name="accessParaInfoId" value="${accessParaInfoId}"/>
    <input type="hidden" id="selStaffIds" name="selStaffIds" />
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">时区号:</label>
            <div class="layui-input-inline">
                <select name="timezoneInfoNum" id="timezoneInfoNum" lay-verify="required">
                    <c:forEach items="${fns:getDictList('time_zone_num')}" var="dict" varStatus="status">
                        <option value="${dict.value}">${dict.label}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">工作日期号:</label>
            <div class="layui-input-inline">
                <select name="workDayNum" id="workDayNum" lay-verify="required">
                    <c:forEach items="${fns:getDictList('workday_num')}" var="dict" varStatus="status">
                        <option value="${dict.value}">${dict.label}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">人员分组:</label>
            <div class="layui-input-inline">
                <select name="permissionGroup" id="permissionGroup" lay-verify="required">
                    <c:forEach items="${fns:getDictList('permission_group')}" var="dict" varStatus="status">
                        <option value="${dict.value}">${dict.label}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">是否允许查库:</label>
            <div class="layui-input-inline">
                <select name="checkPom" id="checkPom" lay-verify="required">
                    <c:forEach items="${fns:getDictList('yes_no')}" var="dict" varStatus="status">
                        <option value="${dict.value}">${dict.label}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-form-item">
            <label class="layui-form-label">备注信息:</label>
            <div class="layui-input-block">
                <textarea name="remarks" id="remarks" lay-verify="required" rows="4" style="width: 98%" maxlength="255" class="layui-textarea"></textarea>
            </div>
        </div>
    </div>
</form>

<div class="layui-btn-group demoTable" style="margin-left: 10px">
    <button class="layui-btn" id="toCommit" data-type="getCheckData">确定</button>
</div>


<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>
    layui.use('table', function(){
        var table = layui.table;

        //方法级渲染
        table.render({
            elem: '#LAY_table_user'
            ,url: '${ctx}/guard/staff/selStaff?accessParaInfoId=${accessParaInfoId}'
            ,cols: [[
                {checkbox: true, fixed: true}
                ,{field:'id', minWidth:100, title:'编号', sort: true, fixed: true, align:'center'}
                ,{field:'name', minWidth:100, title:'姓名', align:'center'}
                ,{field:'workNum', minWidth:200, title:'工号', sort: true, align:'center'}
                ,{field:'dept', title:'部门', align:'center'}
                ,{field:'phone', title:'联系电话', align:'center'}
            ]]
            ,id: 'testReload'
            ,page: true
            ,request:{pageName: 'pageIndex',limitName: 'size'}
            ,limit: 5
            ,limits: [5,10,15,20]
        });

        var $ = layui.$, active = {
            reload: function(){
                var name = $('#name');
                var workNum = $('#workNum');

                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        name: name.val(),
                        workNum:workNum.val()
                    }
                }, 'data');
            },
            getCheckData: function(){ //获取选中数据
                var checkStatus = table.checkStatus('testReload'),data = checkStatus.data;
                if(data.length<1){
                    layer.alert("请勾选人员！");
                    return false;
                }
                var ids=[];
                for(var i=0;i<data.length;i++){
                    ids+=data[i].id+',';
                }
                ids=ids.substr(0,ids.length-1);
                $("#selStaffIds").val(ids.toString());
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

                var accessParaInfoId=$("#accessParaInfoId").val();
                var selStaffIds=$("#selStaffIds").val();
                var timezoneInfoNum=$("#timezoneInfoNum").val();
                var workDayNum=$("#workDayNum").val();
                var permissionGroup=$("#permissionGroup").val();
                var checkPom=$("#checkPom").val();
                var remarks=$("#remarks").val();

                $.post("${ctx}/tbmj/authorization/save",{
                    "accessParaInfoId":accessParaInfoId,
                    "selStaffIds":selStaffIds,
                    "timezoneInfoNum":timezoneInfoNum,
                    "workDayNum":workDayNum,
                    "permissionGroup":permissionGroup,
                    "checkPom":checkPom,
                    "remarks":remarks,
                },function (data) {
                    if (data=="1"){
                        alert("添加成功！");
                        parent.location.reload()
                    }else if(data=="2"){
                        alert("添加成功,请修改默认时区！");
                        parent.location.reload()
                    }else if(data=="3"){
                        alert("该用户已经存在！");
                    }else{
                        alert("添加失败！");
                    }
                    parent.layer.close(index);
                });

                //$("#addAuthor").submit();
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });
</script>
</body>
</html>
