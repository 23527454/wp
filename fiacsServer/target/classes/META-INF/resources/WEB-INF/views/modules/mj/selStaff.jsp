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

<div class="layui-btn-group demoTable" style="margin-left: 10px">
    <button class="layui-btn layui-layer-close layui-layer-close1" data-type="getCheckData">确定</button>
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
            ,url: '${ctx}/guard/staff/selStaff'
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
                    layer.alert("请勾选信息！");
                    return false;
                }
                var ids=[];
                for(var i=0;i<data.length;i++){
                    ids+=data[i].id+',';
                }
                ids=ids.substr(0,ids.length-1);
                layer.alert(ids);
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);
                //return ids;
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });

    /*function close(status){
        if(status=="yes"){
            window.parent.location.reload();//刷新父页面
            var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
            parent.layer.close(index);
        }
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    }*/
</script>
</body>
</html>
