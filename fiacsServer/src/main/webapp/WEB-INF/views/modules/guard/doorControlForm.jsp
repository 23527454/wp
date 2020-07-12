<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>门禁操控管理</title>
<script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
<meta name="decorator" content="default" />
<style type="text/css">
    .btn-circle {
      width: 170px;
      height: 170px;
      text-align: center;
      padding: 6px 0;
      font-size: 18px;
      line-height: 1.428571429;
      border-radius: 85px;
    }
</style>
<script type="text/javascript">
	function control(type){
	 var getTimestamp=new Date().getTime();
	     $.ajax({
             type : "GET",
             url : "${ctx}/guard/doorInOutEvent/controlDoor?nodes=${nodes}&resetType="+type+"&time="+getTimestamp
         }).done(function(response) {
             var resultResponse = jQuery.parseJSON(response);
            if(resultResponse.errorMsg){
                alert(resultResponse.errorMsg);
            }else{
                if(resultResponse.code=='1'){
                    alert("操控完成");
                }else{
                    alert('选择的网点没有添加设备');
                }
            }
         }).fail(function() {
         });
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void();">门禁操控</a></li>
	</ul>
	<div style="margin-top:2%;">
        <button class="btn btn-circle" style="margin-left:20px;background-color:#FF33FF;" onclick="control(1)"><span style="font-weight:bold;font-size:26px;color:white;">开外门</span></button>
        <button class="btn btn-circle" style="margin-left:10px;background-color:#FFFF66;" onclick="control(2)"><span style="font-weight:bold;font-size:26px;color:white;">开内门</span></button>
        <button class="btn btn-circle" style="margin-left:10px;background-color:#6666FF;" onclick="control(3)"><span style="font-weight:bold;font-size:26px;color:white;">开3门</span></button>
        <button class="btn btn-circle" style="margin-left:10px;background-color:#00CC99;" onclick="control(4)"><span style="font-weight:bold;font-size:26px;color:white;">开4门</span></button>
	<div>
	<div style="margin-top:20px;">
            <button class="btn btn-circle" style="margin-left:100px;background-color:#00FFFF;" onclick="control(5)"><span style="font-weight:bold;font-size:26px;color:white;">全开</span></button>
            <button class="btn btn-circle" style="margin-left:20px;background-color:#999999;" onclick="control(6)"><span style="font-weight:bold;font-size:26px;color:white;">全闭</span></button>
            <button class="btn btn-circle" style="margin-left:20px;background-color:#FF9966;" onclick="control(0)"><span style="font-weight:bold;font-size:26px;color:white;">复位</span></button>
    	<div>
</body>
</html>