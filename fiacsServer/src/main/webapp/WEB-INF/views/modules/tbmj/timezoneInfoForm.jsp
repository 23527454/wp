<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>时区信息管理</title>
	<meta name="decorator" content="default" />
	<script type="text/javascript">

		$(document).ready(
				function() {
					$("#weekNum").change(function () {
						var wd=$(this).val();
						var id=$("#id").val();
						window.location.href = "${ctx}/tbmj/timezoneInfo/form?id="+id+"&weekNum="+wd;
						//$.post("${ctx}/tbmj/timezoneInfo/form2?weekNumber="+wd+"&id="+id);
					})


					$("#timeStart1").bind('click',function(event){timePacker($(this),event)});
					$("#timeEnd1").bind('click',function(event){timePacker($(this),event)});
					$("#timeStart2").bind('click',function(event){timePacker($(this),event)});
					$("#timeEnd2").bind('click',function(event){timePacker($(this),event)});
					$("#timeStart3").bind('click',function(event){timePacker($(this),event)});
					$("#timeEnd3").bind('click',function(event){timePacker($(this),event)});
					$("#timeStart4").bind('click',function(event){timePacker($(this),event)});
					$("#timeEnd4").bind('click',function(event){timePacker($(this),event)});

					$("#inputForm")
							.validate(
									{
										submitHandler : function(form) {
											loading('正在提交，请稍等...');
											form.submit();
										}
									});


					$("#btnCopy").on('click',function () {
						/*$.post("${ctx}/tbmj/timezoneInfo/copy",$("#inputForm").serialize(),function (data) {
							if(data){
								alert("复制成功!");
							}
						});*/
						$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/copy");
						$("#inputForm").submit();
						$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/save");
					});

					$("#btnPaste").on('click',function () {
						$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/paste");
						$("#inputForm").submit();
						$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/save");
					});
				});
		//数字:数字
		jQuery.validator.addMethod("numfh", function(value, element) {
			return this.optional(element) || /^[0-9]{2}:[0-9]{2}$/.test(value);
		}, "注意输入格式：XX:XX");

	</script>
</head>
<body>
<div class="container-fluid">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tbmj/accessParaInfo/">时区信息列表</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="timezoneInfo"
			   action="${ctx}/tbmj/timezoneInfo/save" method="post"
			   class="form-horizontal">
		<div class="row">
			<div style="margin-left:10px;margin-top:10px;">
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						   value="保 存"/>
				</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					   onclick="history.go(-1)" />
				<shiro:hasPermission name="tbmj:timezoneInfo:edit">
					<input id="btnCopy" class="btn btn-primary" type="button" style="margin-left: 5%"
						   value="复 制"/>&nbsp;
					<input id="btnPaste" class="btn btn-primary" type="button"
						   value="粘 贴"/>&nbsp;
				</shiro:hasPermission>
			</div>
		</div>
		<br/>
		<br>
		<%--type="hidden" name="id" value="${id}" id="id"--%>
		<input:hidden path="id" />
		<input:hidden path="equipment.id" />
		<sys:message content="${message}" />
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 星期：</label>
			<div class="col-xs-2">
				<select name="weekNum" id="weekNum" cssClass="form-control input-sm" style="width: 50%">
					<option value="1" <c:if test="${weekNum=='1'}">selected</c:if>>星期一</option>
					<option value="2" <c:if test="${weekNum=='2'}">selected</c:if>>星期二</option>
					<option value="3" <c:if test="${weekNum=='3'}">selected</c:if>>星期三</option>
					<option value="4" <c:if test="${weekNum=='4'}">selected</c:if>>星期四</option>
					<option value="5" <c:if test="${weekNum=='5'}">selected</c:if>>星期五</option>
					<option value="6" <c:if test="${weekNum=='6'}">selected</c:if>>星期六</option>
					<option value="7" <c:if test="${weekNum=='7'}">selected</c:if>>星期日</option>
				</select>
				<%--<form:select path="weekNumber" cssClass="form-control input-sm">
					<form:options items="${fns:getDictList('week_number')}"
								  itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>--%>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 时段一开始：</label>
			<div class="col-xs-2">
				<input type="text" name="timeStart1" id="timeStart1" value="${timeStart1}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeStart1" class="form-control input-sm required numfh"></form:input>--%>
			</div>
			<label class="control-label col-xs-2"><font class="red">*</font> 时段一结束：</label>
			<div class="col-xs-2">
				<input type="text" name="timeEnd1" id="timeStart1" value="${timeEnd1}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeEnd1" class="form-control input-sm required numfh"></form:input>--%>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 时段二开始：</label>
			<div class="col-xs-2">
				<input type="text" name="timeStart2" id="timeStart2" value="${timeStart2}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeStart2" class="form-control input-sm required numfh"></form:input>--%>
			</div>
			<label class="control-label col-xs-2"><font class="red">*</font> 时段二结束：</label>
			<div class="col-xs-2">
				<input type="text" name="timeEnd2" id="timeEnd2" value="${timeEnd2}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeEnd2" class="form-control input-sm required numfh"></form:input>--%>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 时段三开始：</label>
			<div class="col-xs-2">
				<input type="text" name="timeStart3" id="timeStart3" value="${timeStart3}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeStart3" class="form-control input-sm required numfh"></form:input>--%>
			</div>
			<label class="control-label col-xs-2"><font class="red">*</font> 时段三结束：</label>
			<div class="col-xs-2">
				<input type="text" name="timeEnd3" id="timeEnd3" value="${timeEnd3}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeEnd3" class="form-control input-sm required numfh"></form:input>--%>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2"><font class="red">*</font> 时段四开始：</label>
			<div class="col-xs-2">
				<input type="text" name="timeStart4" id="timeStart4" value="${timeStart4}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeStart4" class="form-control input-sm required numfh"></form:input>--%>
			</div>
			<label class="control-label col-xs-2"><font class="red">*</font> 时段四结束：</label>
			<div class="col-xs-2">
				<input type="text" name="timeEnd4" id="timeEnd4" value="${timeEnd4}" class="form-control input-sm required numfh" />
				<%--<form:input path="timeEnd4" class="form-control input-sm required numfh"></form:input>--%>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-xs-2">备注：</label>
			<div class="col-xs-2">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
							   maxlength="255" class="input-xxlarge " />
			</div>
		</div>
	</form:form>
</div>

<script>
	$(function () {
		$("#btnCopy").on('click',function () {
			/*
			var formData = {};
			var t = $('#inputForm').serializeArray();
			$.each(t, function() {
				if(this.name!="weekNumber"){
					formData[this.name] = this.value;
				}
			});
			var str=JSON.stringify(formData);
			var json=JSON.parse(str);
			console.log(str)
			var id=json.id;
			var timeStart1=json.timeStart1;
			var timeStart2=json.timeStart2;
			var timeStart3=json.timeStart3;
			var timeStart4=json.timeStart4;
			var timeStart1=json.timeStart1;
			var timeEnd1=json.timeEnd1;
			var timeEnd2=json.timeEnd2;
			var timeEnd3=json.timeEnd3;
			var timeEnd4=json.timeEnd4;
			var remarks=json.remarks;
			*/
			$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/copy");
			$("#inputForm").submit();
			$("#inputForm").attr("action","${ctx}/tbmj/timezoneInfo/save");
		})
	});
</script>


<style>
	/*日历插件样式表*/
	*{
		padding:0;
		margin:0;
	}
	.timePacker{
		/*width: 350px;*/
		/*height: 190px;*/
		border: 2px solid #C5C8CC;
		background-color: white;
		padding: 10px;
		border-radius: 5px;
	}
	.timePacker-title{
		width: auto;
		padding: 4px;
		font-size: 14px;
		font-family: 微软雅黑;
		font-weight: 600;
	}
	.timePacker-content{
		width: 380px;
		height: 160px;
	}
	.timePacker-content ul {
		width: 380px;
		height: 160px;
		overflow: hidden;
	}
	.timePacker-content ul .hoursList {
		float: left;
		display: block;
		width: 50px;
		height: 30px;
		background-color: #F6F7F7;
		color: #000000;
		font-size: 14px;
		font-family: 微软雅黑;
		text-align: center;
		line-height: 30px;
		margin: 4px 4px;
		cursor: pointer;
	}
	.timePackerSelect{
		color: white!important;
		background-color: #007BDB!important;
	}
	/*.nowTime{*/
	/*color: #007BDB;*/
	/*}*/
	.timePacker-content ul .mList {
		float: left;
		display: block;
		width: 30px;
		height: 20px;
		background-color: #F6F7F7;
		/*color: #000000;*/
		font-size: 14px;
		font-family: 微软雅黑;
		text-align: center;
		line-height: 20px;
		margin: 4px 4px;
		cursor: pointer;
	}
	.timePacker-content ul .hoursList:hover,.timePacker-content ul .mList:hover{
		color: white!important;
		background-color: #007BDB!important;
	}
	.timePacker-back-hours{
		display: block;
		width: 22px;
		height: 18px;
		float: right;
		cursor: pointer;
		margin-right: 16px;
	}
	.timePacker-back-hours img{
		width: 100%;
		height: 100%;
	}
</style>
<script>
	function timePacker(dom,e) {
		var hours = null;//存储 "时"
		var minutes = null;//存储 "分"
		var clientY = dom.offset().top + dom.height();//获取位置
		var clientX = dom.offset().left;
		var date = new Date();
		var nowHours = date.getHours();
		var nowMinutes = date.getMinutes();
		var time_hm=/^(0\d{1}|\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/; //时间正则，防止手动输入的时间不符合规范
		var inputText = dom.is("input") ? dom.val():dom.text();
		//插件容器布局
		var html = '';
		html += '<div class="timePacker">';
		html += '<div class="timePacker-hours" style="display: block;">';
		html += '<div class="timePacker-title"><span>小时</span></div>';
		html += '<div class="timePacker-content">';
		html += '<ul>';
		var i = 0;
		while (i < 24)
		{
			var text = i < 10 ? "0" + i : i;
			if(inputText !== "" && Number(inputText.split(":")[1]) === text){
				html += '<li class="hoursList timePackerSelect">'+text+'</li>';
				hours = Number(inputText.split(":")[1]);
			}else{
				if(i === nowHours){
					html += '<li class="hoursList" style="color: #007BDB;">'+text+'</li>';
				}else{
					html += '<li class="hoursList">'+text+'</li>';
				}
			}
			i++;
		}
		html += '</ul>';
		html +=  '</div>';
		html += '</div>';
		html += '<div class="timePacker-minutes" style="display: none;">';
		html += '<div class="timePacker-title"><span>分钟</span><span class="timePacker-back-hours" title="返回小时选择"><img src="${ctxStatic}/timePacker/back.png"/> </span></div>';
		html += '<div class="timePacker-content">';
		html += '<ul>';
		var m = 0;
		while (m < 60)
		{
			var textM = m < 10 ? "0" + m : m;
			if(inputText !== "" && Number(inputText.split(":")[1]) === textM){
				html += '<li class="mList timePackerSelect">'+textM+'</li>';
				minutes = Number(inputText.split(":")[1]);
			}else{
				if(m === nowMinutes){
					html += '<li class="mList" style="color: #007BDB;">'+textM+'</li>';
				}else{
					html += '<li class="mList">'+textM+'</li>';
				}
			}
			m++;
		}
		html += '</ul>';
		html +=  '</div>';
		html += '</div>';
		html += '</div>';
		if($(".timePacker").length > 0){
			$(".timePacker").remove();
		}
		$("body").append(html);
		$(".timePacker").css({
			position:"absolute",
			top:clientY,
			left:clientX
		});
		var _con = $(".timePacker"); // 设置目标区域,如果当前鼠标点击非此插件区域则移除插件
		$(document).mouseup(function(e){
			if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
				_con.remove();
			}
		});
		//小时选择
		$(".hoursList").bind('click',function () {
			$(this).addClass("timePackerSelect").siblings().removeClass("timePackerSelect");
			hours = $(this).text();
			var timer = setTimeout(function () {
				$(".timePacker-hours").css("display","none");
				$(".timePacker-minutes").fadeIn();
				if(minutes !== null){
					var getTime = hours + ":" + minutes;
					if(time_hm.test(getTime)){
						dom.removeClass("errorStyle");
					}
					dom.is("input") ? dom.val(getTime):dom.text(getTime);
				}
				clearTimeout(timer);
			},100);
		});
		//返回小时选择
		$(".timePacker-back-hours").bind('click',function () {
			var timer = setTimeout(function () {
				$(".timePacker-minutes").css("display","none");
				$(".timePacker-hours").fadeIn();
				clearTimeout(timer);
			},500);
		});
		//分钟选择
		$(".mList").bind('click',function () {
			$(this).addClass("timePackerSelect").siblings().removeClass("timePackerSelect");
			minutes = $(this).text();
			var timer = setTimeout(function () {
				var getTime = hours + ":" + minutes;
				if(time_hm.test(getTime)){
					dom.removeClass("errorStyle");
				}
				dom.is("input") ? dom.val(getTime):dom.text(getTime);
				clearTimeout(timer);
				_con.remove();
			},500);
		})
	}

</script>
</body>
</html>