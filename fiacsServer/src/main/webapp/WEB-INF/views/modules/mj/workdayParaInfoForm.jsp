<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>假期添加</title>
    <meta name="keywords" content="假期添加"/>
    <meta name="description" content="假期添加" />
    <link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
</head>

<body>
<form class="layui-form" method="post" id="addForm" action="${ctx}/mj/workdayParaInfo/save" style="margin-right: 10%;margin-top: 10%">
    <input type="hidden" id="eId" name="eId" value="${eId}">
    <div class="layui-form-item">
        <label class="layui-form-label">起始日期:</label>
        <div class="layui-input-block">
            <input type="text" id="start" name="start" htmlEscape="false" class="layui-input" placeholder="yyyy-MM-dd" />
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">结束日期:</label>
        <div class="layui-input-block">
            <input type="text" id="end" name="end" htmlEscape="false" class="layui-input" placeholder="yyyy-MM-dd" />
        </div>
    </div>
</form>

<div class="layui-form-item" style="text-align: center;margin-top: 10%">
    <div class="layui-btn-group demoTable">
        <button class="layui-btn" id="getCommit" data-type="getCommit">确定</button>
    </div>

    <div class="layui-btn-group demoTable">
        <button class="layui-btn" id="addRest" data-type="addRest">默认假日</button>
    </div>
</div>


<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.js"></script>
<script type="text/javascript" src="${ctxStatic}/layui/layui.all.js"></script>
<script>
    $(function() {
        $("#getCommit").on('click',function () {
            var start=$("#start").val();
            var end=$("#end").val();
            if(start==null || start=="" || end==null || end==""){
                layer.open({
                    title:'提示',
                    content:'开始时间和结束时间不能为空！'
                });
            }else{
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);
                $("#addForm").submit();
            }
        });
        $("#addRest").on('click',function () {
            layer.open({
                title: '员工选择', //将姓名设置为红色
                shadeClose: true,           //弹出框之外的地方是否可以点击
                offset: 'auto',
                content: '系统将会自动生成今年的公休日(周六和周日)，并清除以前设定的节假日，是否确定重置？',
                btn: ['确定','取消'],
                btnAlign: 'c',
                yes: function(){
                    window.location.href="${ctx}/mj/workdayParaInfo/insertDefault?eId=${eId}";
                    /*$.post("${ctx}/mj/workdayParaInfo/insertDefault?eId=${eId}",function (data) {
                        if(data){
                            layui.open({
                                title: '提示', //将姓名设置为红色
                                content: '创建成功！'
                            })
                        }else{
                            layui.open({
                                title: '提示', //将姓名设置为红色
                                content: '创建失败！'
                            })
                        }
                    });
                    //最后关闭弹出层
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.layer.close(index);*/
                },btn2: function(){
                }
        });
    });
})

    layui.use('laydate', function(){
        var laydate = layui.laydate;

        var date = new Date();
        var year = date.getFullYear();
        var nowTime = new Date().valueOf();

        var start = laydate.render({ //投票开始
            elem: '#start',
            min: year+'-1-1',
            max: year+'-12-31',
            btns: ['clear', 'confirm'],
            done:function(value,date){
                endMax = end.config.max;
                end.config.min = date;
                end.config.min.month = date.month -1;
            }
        });
        var end = laydate.render({ //投票结束
            elem: '#end',
            min: year+'-1-1',
            max: year+'-12-31',
            btns: ['clear', 'confirm'],
            done:function(value,date){
                if($.trim(value) == ''){
                    var curDate = new Date();
                    date = {'date': curDate.getDate(), 'month': curDate.getMonth()+1, 'year': curDate.getFullYear()};
                }
                start.config.max = date;
                start.config.max.month = date.month -1;
            }
        });


    });

</script>
</body>
</html>
