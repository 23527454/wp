<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
<head>
    <link href="${ctxStatic}/realTimeMonitoring/css/BigData.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/realTimeMonitoring/css/index.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/realTimeMonitoring/css/index01.css" rel="stylesheet" type="text/css" />
    <script src="${ctxStatic}/realTimeMonitoring/js/jquery.js"></script>
    <link href="${ctxStatic}/realTimeMonitoring/js/bstable/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/realTimeMonitoring/js/bstable/css/bootstrap-table.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/realTimeMonitoring/css/Security_operation.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${ctxStatic}/realTimeMonitoring/js/artDialog/skins/default.css" type="text/css"/>
    <script src="${ctxStatic}/realTimeMonitoring/js/laydate.js"></script>
    <script src="${ctxStatic}/realTimeMonitoring/js/Home_page.js"></script>
    <script src="${ctxStatic}/realTimeMonitoring/js/echarts-all.js"></script>


    <script type="text/javascript" src="${ctxStatic}/realTimeMonitoring/js/jquery.pagination.js"></script>

    <script src="${ctxStatic}/jquery-plugin/jquery.tmpl.min.js"></script>
    <script src="${ctxStatic}/realTimeMonitoring/js/artDialog/artDialog.js"></script>
    <script src="${ctxStatic}/realTimeMonitoring/js/artDialog/plugins/iframeTools.source.js"></script>

    <meta charset="UTF-8">
	<style type="text/css">
	.layer{position:relative;width:100%;}
	#layer01{}
	#layer01 img{text-align: center;display: block;height: 35px;padding-top: 35px;margin: auto;}
	#layer02 > div{height:100%;float:left;position:relative;}
	.layer02-data{position: absolute;width: auto;height: 100px;color: white;top: 45px;left: 65px;}
	#layer022 > div{height:100%;float:left;position:relative;}
	.layer022-data{position: absolute;width: auto;height: 100px;color: white;top: 45px;left: 65px;}
	.layer03-panel{height:100%;position:relative;float:left;}
	.layer03-left-label{position:absolute;}
	#layer03_left_label01{top:10px;left:10px;color:white;height:20px;width:200px;font-weight: bold;}
	#layer03_left_label02{right:10px;top:10px;color:#036769;height:20px;width:200px;}
	.layer03-left-chart{position:relative;float:left;height:100%;}
	#layer03_right_label{position:absolute;top:10px;left:10px;color:white;height:20px;width:100px;}
	.layer03-right-chart{position:relative;float:left;height:100%;width:32%;}
	.layer03-right-chart-label{color: white;text-align: center;position: absolute;bottom: 4%;width: 100%;}
	.layer04-panel{position:relative;float:left;height:100%;width:48%;}
	.layer04-panel-label{width:100%;height:15%;color:white;padding-top:5px;}
	.layer04-panel-chart{width:100%;height:85%;}
	.p_chart{
    height: 200px;

   margin-top: -30px;

   margin-left: -20px;

   width: 350px;

}
.div_any01{
    width: 100%;
	height: 90%;
    margin-right: 2%;
	margin-top: -30px;
}
.div_any_child{
    width: 100%;
    height: 90%;
    box-shadow: -10px 0px 15px #034c6a inset,
    0px -10px 15px #034c6a inset,
    10px 0px 15px #034c6a inset,
    0px 10px 15px #034c6a inset;
    border: 1px solid #034c6a;
    box-sizing: border-box;
    position: relative;
    margin-top: 25px;
}
.zb1,.zb2,.zb3{float: left; width: 33.3333%; height: 100%;}
#zb1,#zb2,#zb3{height: calc(100% - 30px); }
.zhibiao span{ padding-top: 20px; display: block; text-align: center; color: #fff; font-size: 16px;}
  </style>
    <title>银行智能出入管理系统</title>

</head>
<body >
<div class="data_bodey">
    <div class="index_nav">
        <ul style="height: 30px; margin-bottom: 0px;">
        <li class="l_left total_chose_pl" >
        <img src="${ctxStatic}/images/logo.png" style="height:25px;width:25px;"></img>
                <li class="r_right total_chose_pl">
                   <span id="showTime" style="float:left;"></span>
                  </li>
               </ul>
    </div>
    <div class="index_tabs">
        <!--安防运作-->
        <div class="inner" style="height: 109%;">

            <div class="left_cage">
			<!-- 左上 -->
                <div class="dataAllBorder01 cage_cl" style="margin-top: 9% !important; height: 28%;">
                    <div class="dataAllBorder02 video_cage">
                     <div id="layer03_right" style="width:100%;height:100%;" class="layer03-panel zhibiao">

                   			<div class="zb1"><span>派送完成率</span><div id="zb1"></div></div>
                   			<div class="zb2"><span>回收完成率</span><div id="zb2"></div></div>
                   			<div class="zb3"><span>车辆准时率</span><div id="zb3"></div></div>
                        </div>
					</div>
                </div>
				<!-- 左中 -->
                 <div class="dataAllBorder01 cage_cl" style="margin-top: 1.5% !important; height: 34%;">
                				<div class="dataAllBorder02 video_cage">
                				<div class="analysis">款箱统计：</div>
                				<div id="layer02" class="layer" style="height:15%;margin-top:-30px;">
                                    <div id="layer02_04" style="width:30%;margin-left:-15px;" >
                				<div class="layer02-data">
                					<span style="font-size:26px;" id="ps_total">0</span>

                				</div>
                				<canvas width="120" height="100"></canvas>
                			</div>
                			<div id="layer02_05" style="width:30%;">
                				<div class="layer02-data">
                					<span style="font-size:26px;" id="ps_zw">0</span>

                				</div>
                				<canvas width="120" height="100"></canvas>
                			</div>
                			<div id="layer02_06" style="width:30%;">
                				<div class="layer02-data">
                					<span style="font-size:26px;" id="ps_db">0</span>

                				</div>
                				<canvas width="120" height="100"></canvas>
                				</div>
                			</div>
                			<div id="layer022" class="layer" style="height:15%;margin-top:30px;">
                                    <div id="layer022_04" style="width:30%;margin-left:-15px;" >
                				<div class="layer022-data">
                					<span style="font-size:26px;" id="hs_total">0</span>

                				</div>
                				<canvas width="120" height="100"></canvas>
                			</div>
                			<div id="layer022_05" style="width:30%;">
                				<div class="layer022-data">
                					<span style="font-size:26px;" id="hs_zw">0</span>

                				</div>
                				<canvas width="120" height="100"></canvas>
                			</div>
                			<div id="layer022_06" style="width:30%;">
                				<div class="layer022-data">
                					<span style="font-size:26px;" id="hs_db">0</span>
                				</div>
                				<canvas width="120" height="100"></canvas>
                				</div>
                			</div>
                			</div>
                                </div>
                <div class="dataAllBorder01 cage_cl" style="margin-top: 1.5% !important; height: 32%; position: relative;">
                <div class="dataAllBorder02 video_cage">
					<div class="analysis">次数统计：</div>
                        <div class="manage_top_middle" style="height:98%;">
                        <div id="container6" style="height: 90%;width:95%;margin-top:-15px;"></div>

                        </div>
					</div>
					</div>
            </div>

            <div class="center_cage">
                <div class="dataAllBorder01 cage_cl" style="margin-top: 3.5% !important; height: 62.7%; position: relative;">
                    <div class="dataAllBorder02" style="position: relative; overflow: hidden;">
                        <!--标题栏-->
                 <div class="manage_top_middle" style="height:100%;">
                    <div class="map_title">交接事件详情</div>
                            <div style="height:49%;">
                                <div style="width:49.6%;float:left;margin-left:5px;height:100%;">
                                    <table id="infomationDetal" class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                        <tbody>
                                        <tr height="25%">
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">网点名称</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="officeName"></span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">所属区域</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="areaName"></span></td>
                                        </tr>
                                        <tr height="25%">
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">班组</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="taskName"></span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">班次</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="taskId"></span></td>
                                        </tr>
                                         <tr height="25%">
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">任务类型</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="taskType"></span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">交接时间</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="time"></span></td>
                                        </tr>
                                        <tr height="25%">
                                            <td style="text-align:center;vertical-align:middle;" rowspan="2"><span style="font-weight:bold;font-size:15px">款箱编号</span></td>
                                            <td style="text-align:center;vertical-align:middle;" colspan="3" rowspan="2"><span style="font-weight:bold;font-size:15px" id="moneyBoxCodes"></span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                    <img style="height:74%;width:24.2%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="safeGuardOneImageId"></img>
                                    <img style="height:74%;width:24.2%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="safeGuardOneZImageId"></img>
                                    <div style="width:48.4%;float:left;height:26.5%;">
                                <table id="infomationDetal1" class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                        <tbody>
                                        <tr>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">押款员</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="safeGuardOneName"></span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    </div>
                            </div>
                            <div style="height:40%;display:block;margin-left:5px;margin-top:5px;">
                                <div style="width:25%;height:100%;float:left;">
                                        <img style="height:80%;width:100%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="commissionerOneImageId"></img>
                                        <div style="width:100%;float:left;height:26%;">
                                         <table class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                            <tbody>
                                            <tr>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">专员</span></td>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="commissionerOneName"></span></td>
                                            </tr>
                                            </tbody>
                                      </table>
                                      </div>
                                </div>
                                <div style="width:25%;float:left;height:100%" id="carone">
                                        <img style="height:80%;width:100%;float:left;" src="${ctxStatic}/images/car_img.jpg" id="carImageId"></img>
                                        <div style="width:100%;float:left;height:26%;">
                                         <table id="infomationDetal11" class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                            <tbody>
                                            <tr>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">车牌号</span></td>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="carPlate"></span></td>
                                            </tr>

                                            </tbody>
                                      </table>
                                        </div>
                                </div>
                                <div style="width:24%;float:left;display:none;height:100%;" id="zytwo">
                                        <img style="height:80%;width:100%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="commissionerTwoImageId"></img>
                                        <div style="width:100%;float:left;height:26%;">
                                          <table id="infomationDetal111" class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                            <tbody>
                                            <tr>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">专员</span></td>
                                                <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="commissionerTwoName"></span></td>

                                            </tr>

                                            </tbody>
                                      </table>
                                            </div>
                                </div>
                                <div style="width:49%;display: inline-block;height:100%">
                                        <img style="height:80%;width:50%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="safeGuardTwoImageId"></img>
                                    <img style="height:80%;width:50%;float:left;" onerror="errorImg(this)" src="${ctxStatic}/images/staff_img.jpg" id="safeGuardTwoZImageId"></img>
                                    <div style="width:100%;float:left;height:26%;">
                                <table id="infomationDetal23" class="table table-bordered" border="0" cellspacing="0" cellpadding="0" height="100%">
                                        <tbody>
                                        <tr>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px">押款员</span></td>
                                            <td style="text-align:center;vertical-align:middle;"><span style="font-weight:bold;font-size:15px" id="safeGuardTwoName"></span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dataAllBorder01 cage_cl" style="margin-top: 0.6% !important; height: 32.1%;">
                                   <div class="dataAllBorder02" id="map_title_innerbox">
                                       <div class="map_title_box">
                                           <div class="map_title_innerbox">
                                               <div class="map_title" style="background-image: url(${ctxStatic}/realTimeMonitoring/img/second_title.png);">交接事件列表</div>
                                   <table id="table"
                                   		class="table table-striped table-bordered table-hover">
                                   		<thead>
                                   			<tr>
                                   				<th style="text-align:center;vertical-align:middle;">网点名称</th>
                                   				<th style="text-align:center;vertical-align:middle;">所属公司</th>
                                   				<th style="text-align:center;vertical-align:middle;">所属区域</th>

                                   				<th style="text-align:center;vertical-align:middle;">车牌号</th>
                                   				<th style="text-align:center;vertical-align:middle;">负责人</th>
                                   				<th style="text-align:center;vertical-align:middle;">联系方式</th>
                                   				<th style="text-align:center;vertical-align:middle;">操作</th>

                                   			</tr>
                                   		</thead>
                                   		<tbody id="eventMessages">

                                   		</tbody>
                                   	</table>
                                           </div>

                                       </div>
                                       <table id="table" style="width: 100%"></table>
                                   </div>
                               </div>
                           </div>

            <div class="right_cage">
                <!--顶部切换位置-->
                <div class="dataAllBorder01 cage_cl" style="margin-top: 9% !important; height: 28%">
                   <div class="dataAllBorder02 over_hide dataAllBorder20" id="over_hide">
                        <div class="analysis">识别方式：</div>
                            <p class="data_chart" id="rode01" style="width:97%;height:96%;"></p>
                    </div>
                </div>

                <div class="dataAllBorder01 cage_cl check_increase" style=" margin-top: 1.5% !important;height:34%">
                    <!--切换01-->
                    <div class="dataAllBorder02 over_hide dataAllBorder20" id="over_hide">
					<div class="analysis">告警统计：</div>
                         <div id="userContainerPrize" style="height: 95%;width:97%;"></div>
                    </div>
                </div>

                <div class="dataAllBorder01 cage_cl check_decrease" style="margin-top: 1.5% !important; height: 32%; position: relative;">
				<div class="dataAllBorder02" style="padding: 1.2%; overflow: hidden">

                             <div class="analysis" id="gj_list">告警列表：</div>
                                   <div id="userContainerPrize1" style="height: 95%;width:97%;"></div>
                                    <div class="message_scroll_box" id="message_scroll_box">
                                                                    </div>
                              </div>

						</div>
                </div>
            </div>

        </div>

    </div>
</div>
<script type="text/javascript">

		var curTryNum = 0;
		var maxTryNum = 10;

		var websocket = null;

		var whoClose = 0;//0代表异常关闭 1代表主动关闭
		var connect = function(url) {
			//连接次数加一
			curTryNum = curTryNum + 1;

			if (!window.WebSocket) {
				window.WebSocket = window.MozWebSocket;
			}
			if (window.WebSocket) {
				websocket = new WebSocket(url);

				websocket.onopen = function(event) {
					console.log('链接成功');
					//连接成功时将当前已重连次数归零
					curTryNum = 0;

					console.log("心跳检测启动");
					heartCheck.start();
					websocket.send("serverPort");
				};
				websocket.onclose = function(event) {
					console.log('onclose');
					if (whoClose == 0) {
						if (curTryNum <= maxTryNum) {
							/* alert("连接关闭，5秒后重新连接……"); */
							// 5秒后重新连接，实际效果：每5秒重连一次，直到连接成功
							setTimeout(function() {
								connect(url);
							}, 5000);
						} else {
							//alert("连接实时通知服务器失败！");
						}

					}

				};

				websocket.onmessage = function(message) {
					console.log("=="+message.data);
					if(message.data!='response heart'){
                        var messageData =  JSON.parse(message.data);
                        if (messageData.type) {
                                 $.ajax({
                                     type : "GET",
                                     url : "${ctx}/guard/bigData/actualTimeRefresh",
                                     data:messageData
                                 }).done(function(response) {
                                    var resultResponse = jQuery.parseJSON(response);
                                        textToSpeach(resultResponse.tts);
                                     if(resultResponse.type=='4'){//交接事件
                                        var $tr = $("#eventMessages tr");
                                        $tr.removeClass("warning-fixed");
                                        $("#template").tmpl(resultResponse.data).hide().prependTo("#eventMessages").fadeIn();
                                        refreshConnectEventDetail(resultResponse.data.id);
                                     }else if(resultResponse.type=='5'){//告警事件
                                             insertAlarmEvent(resultResponse.data);
                                     }
                                 }).fail(function() {
                                 });
                        }
					}
				};
				websocket.onerror = function(event) {
					//alert("连接出错");

				};

			} else {
				alert("你的浏览器不支持websocket协议");
				window.close();
			}

			//监听窗口关闭事件，窗口关闭前，主动关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常
			window.onbeforeunload = function() {
				websocket.close();
			};
			/**
			 * 心跳检测
			 */
			var heartCheck = {
				timeout : 5000, //计时器设定为5s
				timeoutObj : null,
				serverTimeoutObj : null,
				//重置
				reset : function() {
					clearTimeout(this.timeoutObj);
					clearTimeout(this.serverTimeoutObj);
					this.start();
				},
				//开始
				start : function() {
					var self = this;
					this.timeoutObj = setTimeout(function() {
						websocket.send("HeartBeat");
						console.log("发送心跳");
					}, this.timeout);
				}
			};

		};
		/**
		 * 执行入口
		 */
		//离开页面 主动关闭websocket
		window.onbeforeunload = function() {
			whoClose = 1;
			if (websocket) {
				websocket.close();
			}
		}
        connect("ws://127.0.0.1:10004/?userId=1");
	</script>
<script id="template" type="text/x-jquery-tmpl">
        <tr  data-id="{{= id}}" class="warning-fixed">
            <td style="text-align:center;vertical-align:middle;">
               {{= officeName}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
                {{= companyName}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
                {{= areaName}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
                {{= carplate}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
                {{= carAdmin}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
                {{= carPhone}}
            </td>
            <td style="text-align:center;vertical-align:middle;">
            <button style="margin-left:0px;" class="chose_enter" onclick="refreshConnectEventDetail({{= id}})">详情</button>
            </td>
        </tr>
    </script>
<script>
var t = null;
    t = setTimeout(time,1000);//開始运行
    function time()
    {
       clearTimeout(t);//清除定时器
       dt = new Date();
		var y=dt.getFullYear();
		var mt=dt.getMonth()+1;
		var day=dt.getDate();
       var h=dt.getHours();//获取时
       var m=dt.getMinutes();//获取分
       var s=dt.getSeconds();//获取秒
       document.getElementById("showTime").innerHTML = y+"年"+mt+"月"+day+"日"+h+"时"+m+"分"+s+"秒";
       t = setTimeout(time,1000); //设定定时器，循环运行
    }

</script>
<script>
    var number=8;
    $(function () {
      if (window.screen.height <= 768) {
            number = 6;
        } else if (window.screen.height > 768 && window.screen.height <= 900) {
            number = 8;
        } else if (window.screen.height >= 1080) {
            number = 10;
        }
       leftmiddle();
       refreshData();
		setTimeout('fullScreen();',500);
		setTimeout('refreshConnectEvent()',1000);
		//wSizeWidth();
    });

    function errorImg(img) {
    		img.src = "${ctxStatic}/images/staff_img.jpg";
    		img.onerror = null;
    	}

    	function TimeControl(){
            $(".message_scroll_box").animate({marginTop:96},800,
                    function(){
                        $(".message_scroll_box").css({marginTop:0});    //把顶部的边界清零
                        $(".message_scroll_box .message_scroll:first").before($(".message_scroll_box .message_scroll:last"));    //在第一个新闻后面插入最后一个新闻

                    });
        }
        var T;    //开始定时
        $(".message_scroll_box").mouseenter(function(){
            clearInterval(T);    //停止定时
        })
        .mouseleave(function(){
            if(alarmEventData!=0){
                T=setInterval(TimeControl,2500);    //再次定时
            }
        })

    function IEVersion() {
    	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    	var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
    	var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    	var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    	if(isIE) {
    		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
    		reIE.test(userAgent);
    		var fIEVersion = parseFloat(RegExp["$1"]);
    		if(fIEVersion == 7) {
    			return 7;
    		} else if(fIEVersion == 8) {
    			return 8;
    		} else if(fIEVersion == 9) {
    			return 9;
    		} else if(fIEVersion == 10) {
    			return 10;
    		} else {
    			return 6;//IE版本<=7
    		}
    	} else if(isEdge) {
    		return 'edge';//edge
    	} else if(isIE11) {
    		return 11; //IE11
    	}else{
    		return -1;//不是ie浏览器
    	}
    }

    function insertAlarmEvent(alarmEvent){
        var innerHTMLText='';
        $('#gj_list').hide();
        $('#userContainerPrize1').hide();

        if(alarmEventData!=0){
            innerHTMLText = $('#message_scroll_box').html();
        }else{
            T=setInterval(TimeControl,2500);
        }
           innerHTMLText = innerHTMLText +  '<div class="message_scroll">'
                       +' <div class="scroll_top">'
                        +'    <span class="scroll_title"  style="font-weight:bold;">'+alarmEvent.eventType+'</span>'
                       +'     <button class="chose_enter" id="alarmEventBtn_'+alarmEvent.id+'" onclick="EventClick('+alarmEvent.id+')">处理</button>'

                       +'     <span class="scroll_timer">'+alarmEvent.time+'</span>'
                       +' </div>'
                       +' <div class="msg_cage">'
                       +'     <a class="localize_title">'+alarmEvent.officeName+'</a>'
                       +' </div>'
                     +' </div>';

                     $('#message_scroll_box')[0].innerHTML=innerHTMLText;
     }


    var speechSU;

    function textToSpeach(textToSpeach, speachRate) {
    	speachRate = speachRate || 2;
    	if(IEVersion() != -1){
    		if(typeof voiceObj == 'undefined'){
    			try {
    				voiceObj = new ActiveXObject("Sapi.SpVoice");
    			} catch (ex) {
    				console.log("Speak Sapi.SpVoice init error "+ex);
    			}
    		}

    		if(typeof voiceObj != 'undefined'){
    			voiceObj.Rate=speachRate; //语速
    			//voiceObj.Volume=60;
    			console.log(textToSpeach+"播报");
    		    voiceObj.Speak(textToSpeach);
                 voiceObj.WaitUntilDone(-1);
    			return true;
    		}
    	}

    	if(!(speechSU instanceof  SpeechSynthesisUtterance)){
    		try {
    			speechSU = new SpeechSynthesisUtterance();
    		} catch (ex) {
    			console.log("Speak SpeechSynthesisUtterance init error "+ex);
    		}
    	}
    	if(speechSU instanceof  SpeechSynthesisUtterance){

    		speechSU.text = textToSpeach;
    		speechSU.lang = 'zh-CN';
    		speechSU.rate =speachRate;
    		console.log(textToSpeach+"播报");

    		 window.speechSynthesis.speak(speechSU);
    		 speechSU=null;

    		return true;
    	}

    }

	function moneyBoxCaculate(pstotal,pszw,hstotal,hszw){
	     $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/caculateMoneyBox"
         }).done(function(response) {
            var resultResponse = jQuery.parseJSON(response);
             $('#ps_total').html(resultResponse.pstotal);
            $('#ps_zw').html(resultResponse.pszwtotal);
            $('#ps_db').html(resultResponse.pstotal-resultResponse.pszwtotal);

            $('#hs_total').html(resultResponse.hstotal);
            $('#hs_zw').html(resultResponse.hszwtotal);
            $('#hs_db').html(resultResponse.hstotal-resultResponse.hszwtotal);
         }).fail(function() {
         });
	}

	function fullScreen(){
	         var isFullscreen = document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || false;
            if (isFullscreen) {
                var efs = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
                if (efs) {
                    efs.call(document);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }

            } else {
                var el = document.documentElement;
                console.log(el);
                var rfs = el.requestFullscreen || el.webkitRequestFullscreen || el.mozRequestFullScreen || el.msRequestFullscreen;
                if (rfs) {
                    rfs.call(el);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }

            }
    	}

	function caculateAlarm(dw,kxcx,kxlx,rwcs,zyrz,ykyrz){
	    if(dw==0&&kxcx==0&&kxlx==0&&rwcs==0&&zyrz==0&&ykyrz==0){
	        var myChart =  echarts.init(document.getElementById('userContainerPrize'));
                            option = {
                                series: [{
                                    type: 'pie',
                                     center:['50%','40%'],
                                    radius: ['40%', '70%'],
                                    color:'#49bcf7',
                                    label: {
                                        normal: {
                                            position: 'center'
                                        }
                                    },
                                    data: [{
                                        value: 1,
                                        name: '女消费',
                                        itemStyle:{
                            				normal:{
                            					label:{
                            						show:true,
                            						position:'center',
                            						formatter:'无',
                                                    textStyle: {
                                                        fontSize: '30',
                                                        fontWeight: 'bold',
                                                        color:'white'
                                                    }
                            					},
                            					labelLine:{
                            						show:false
                            					}
                            				}
                            			}
                                    }, {
                                        value: 0,
                                        name: '',
                                        itemStyle: {
                                            normal: {
                            					labelLine:{
                            						show:false
                            					},
                                                color: 'rgba(255,255,255,.2)'
                                            },
                                            emphasis: {
                                                color: '#fff'
                                            }
                                        },
                                    }]
                                }]
                            };
                                    myChart.setOption(option);
                                    window.addEventListener("resize",function(){
                                        myChart.resize();
                                    });
	    }else{
	    var myChart = echarts.init(document.getElementById('userContainerPrize'));
                option = {
        			  tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
        			legend: {
                        orient: 'vertical',
                        x: 'right',
                        data:['断网','款箱错箱','款箱漏箱','任务超时','专员认证5次','押款员认证5次'],
                        textStyle:{
                            color:"#e9ebee"
                        }
                    },
                    series: [
                        {
                            name:'报警次数',
                            type:'pie',
                            center:['33%','38%'],
                            radius: ['30%', '80%'],
                            avoidLabelOverlap: false,
        					hoverAnimation:false,
                            label: {
                                normal: {
                                    show: true
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
        								position:'inner',
        								formatter:'{c}'
                                    },
                                    labelLine: {
                                        show: false
                                    }
                                }
                            },
                            data:[
                                {value:dw, name:'断网'},

                                {value:kxcx, name:'款箱错箱'},
                                {value:kxlx, name:'款箱漏箱'},
                                {value:rwcs, name:'任务超时'},
                                {value:zyrz, name:'专员认证5次'},
        						{value:ykyrz, name:'押款员认证5次'}
                            ]
                        }
                    ]
                };


                myChart.setOption(option);
                 window.addEventListener("resize",function(){
                            myChart.resize();
                        });
	    }

	}

    function EventClick(id){
        $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/dealAlarmEvent?id="+id
         }).done(function(response) {
            if(response=='success'){
                 document.getElementById('alarmEventBtn_'+id).disabled=true;
                 document.getElementById('alarmEventBtn_'+id).style.background='#C0C0C0';
                 document.getElementById('alarmEventBtn_'+id).innerHTML='已处理';
            }
         }).fail(function() {
         });
    }

    function refreshConnectEventDetail(eventId){
    var getTimestamp=new Date().getTime();
         $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/queryEventDetail?eventId="+eventId+"&&time="+getTimestamp
         }).done(function(response) {
            var resultResponse = jQuery.parseJSON(response);
            $('#officeName').html(resultResponse.officeName);
            $('#areaName').html(resultResponse.areaName);
            $('#taskType').html(resultResponse.taskType);
            $('#taskId').html(resultResponse.taskId);
            $('#taskName').html(resultResponse.taskName);
            $('#moneyBoxCodes').html(resultResponse.moneyBoxCodes);
            $('#time').html(resultResponse.time);
            $('#safeGuardOneName').html(resultResponse.safeGuardOneName);

             $('#safeGuardOneImageId').attr("src","${ctx}/guard/image/staff?id="+resultResponse.safeGuardOneImageId+"&&time="+getTimestamp);

            $('#commissionerOneName').html(resultResponse.commissionerOneName);

             $('#commissionerOneImageId').attr("src","${ctx}/guard/image/staff?id="+resultResponse.commissionerOneImageId+"&&time="+getTimestamp);

             if(resultResponse.commissionerTwoName){
                $('#carone').hide();
                $('#zytwo').show();
                 $('#commissionerTwoName').html(resultResponse.commissionerTwoName);

                 $('#commissionerTwoImageId').attr("src","${ctx}/guard/image/staff?id="+resultResponse.commissionerTwoImageId+"&&time="+getTimestamp);
             }else{
                $('#carone').show();
                $('#zytwo').hide();
                 $('#carPlate').html(resultResponse.carPlate);

                 $('#carImageId').attr("src","${ctx}/guard/image/car?id="+resultResponse.carId+"&&time="+getTimestamp);
             }

           $('#safeGuardOneZImageId').attr("src","${ctx}/guard/image?id="+resultResponse.safeGuardOneZImageId+"&&bigDataFlag=1");

           if(resultResponse.safeGuardTwoName){
                 $('#safeGuardTwoName').html(resultResponse.safeGuardTwoName);

                $('#safeGuardTwoImageId').attr("src","${ctx}/guard/image/staff?id="+resultResponse.safeGuardTwoImageId+"&&time="+getTimestamp);

                $('#safeGuardTwoZImageId').attr("src","${ctx}/guard/image?id="+resultResponse.safeGuardTwoZImageId+"&&bigDataFlag=1");
           }else{
                $('#safeGuardTwoName').html('无');

                $('#safeGuardTwoImageId').attr("src","${ctxStatic}/images/staff_img.jpg");

                $('#safeGuardTwoZImageId').attr("src","${ctxStatic}/images/staff_img.jpg");
           }
         }).fail(function() {
         });
    }

    function inserAlarmEventNoData(){
        if(!document.getElementById('userContainerPrize1')){
            return;
        }
         var myChart =  echarts.init(document.getElementById('userContainerPrize1'));
        option = {
            series: [{
                type: 'pie',
                 center:['50%','40%'],
                radius: ['50%', '80%'],
                color:'#49bcf7',
                label: {
                    normal: {
                        position: 'center'
                    }
                },
                data: [{
                    value: 1,
                    name: '女消费',
                    itemStyle:{
                        normal:{
                            label:{
                                show:true,
                                position:'center',
                                formatter:'无',
                                textStyle: {
                                    fontSize: '30',
                                    fontWeight: 'bold',
                                    color:'white'
                                }
                            },
                            labelLine:{
                                show:false
                            }
                        }
                    }
                }, {
                    value: 0,
                    name: '',
                    itemStyle: {
                        normal: {
                            labelLine:{
                                show:false
                            },
                            color: 'rgba(255,255,255,.2)'
                        },
                        emphasis: {
                            color: '#fff'
                        }
                    },
                }]
            }]
        };
                myChart.setOption(option);
                window.addEventListener("resize",function(){
                    myChart.resize();
                });
                clearInterval(T);
    }

    var alarmEventData = 1;
    function refreshAlarmEventList(){
         $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/queryAlarmEventList"
         }).done(function(list) {
            if(list.length == 0){
                 alarmEventData=0;
                inserAlarmEventNoData();
                return;
            }
            $('#gj_list').hide();
              $('#userContainerPrize1').hide();
            var innerHTMLText = "";
            for(var i=0;i<list.length;i++){
              innerHTMLText = innerHTMLText +  '<div class="message_scroll">'
               +' <div class="scroll_top">'
                +'    <span class="scroll_title"  style="font-weight:bold;">'+list[i].eventType+'</span>';
                if(list[i].handleTime){
               innerHTMLText = innerHTMLText +'      <button class="chose_enter" id="alarmEventBtn_'+list[i].id+'" disabled style="background:#C0C0C0;">已处理</button>';
                }else{
               innerHTMLText = innerHTMLText +'      <button class="chose_enter" id="alarmEventBtn_'+list[i].id+'" onclick="EventClick('+list[i].id+')">处理</button>';
                }

               innerHTMLText = innerHTMLText +'     <span class="scroll_timer" style="font-weight:12px;color:white;">'+list[i].time+'</span>'
               +' </div>'
               +' <div class="msg_cage">'
               +'     <a class="localize_title" style="font-weight:12px;color:white;">'+list[i].officeName+'</a>'
               +' </div>'
             +' </div>';
            }
             $('#message_scroll_box')[0].innerHTML=innerHTMLText;
             clearInterval(T);
             T=setInterval(TimeControl,2500);
         }).fail(function() {
         });

    }

    function refreshConnectEvent(){
   $("#eventMessages").find("tr").remove();
    var $tr = $("#eventMessages tr");
         $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/queryConnectEventList?size="+number
         }).done(function(list) {
            if(list.length == 0){
                return;
            }
            console.log(list);
            $tr.removeClass("warning-fixed");
            $("#template").tmpl(list).hide().prependTo("#eventMessages").fadeIn();
            refreshConnectEventDetail(list[0].id);
         }).fail(function() {
         });
    }

    function refreshData(){

         var getTimestamp=new Date().getTime();
         $.ajax({
             type : "GET",
             url : "${ctx}/guard/bigData/countMoneyBoxTimes?time="+getTimestamp
         }).done(function(response) {
            var resultResponse = jQuery.parseJSON(response);
             leftBottom(resultResponse.yy,resultResponse.sj,resultResponse.db);
         }).fail(function() {
         });

          $.ajax({
              type : "GET",
              url : "${ctx}/guard/bigData/countAuthorType?time="+getTimestamp
          }).done(function(response) {
             var resultResponse = jQuery.parseJSON(response);
               rightTop(resultResponse.zw,resultResponse.rl,resultResponse.mm,resultResponse.zwrl,resultResponse.zwmm);
          }).fail(function() {
          });

           $.ajax({
                type : "GET",
                url : "${ctx}/guard/bigData/countAlarmEventType?time="+getTimestamp
            }).done(function(response) {
               var resultResponse = jQuery.parseJSON(response);
                caculateAlarm(resultResponse.zero,resultResponse.four,resultResponse.five,resultResponse.three,resultResponse.two,resultResponse.one);
            }).fail(function() {
            });

           $.ajax({
               type : "GET",
               url : "${ctx}/guard/bigData/calculateRate?time="+getTimestamp
           }).done(function(response) {
              var resultResponse = jQuery.parseJSON(response);
               lefttop(resultResponse.ps,resultResponse.pstotal,resultResponse.hs,resultResponse.hstotal,resultResponse.dd,resultResponse.ddtotal);
           }).fail(function() {
           });
           moneyBoxCaculate();

            refreshAlarmEventList();
            moneyBoxCaculate();
    	}

	function rightTop(zw,rl,mm,zwrl,zwmm){
	    if(zw==0&&rl==0&&mm==0&&zwrl==0&&zwmm==0){
	        var myChart = echarts.init($("#rode01")[0]);
                option = {
                    series: [{
                        type: 'pie',
                         center:['50%','40%'],
                        radius: ['50%', '80%'],
                        color:'#49bcf7',
                        label: {
                            normal: {
                                position: 'center'
                            }
                        },
                        data: [{
                            value: 1,
                            name: '女消费',
                            itemStyle:{
                				normal:{
                					label:{
                						show:true,
                						position:'center',
                						formatter:'无',
                                        textStyle: {
                                            fontSize: '30',
                                            fontWeight: 'bold',
                                            color:'white'
                                        }
                					},
                					labelLine:{
                						show:false
                					}
                				}
                			}
                        }, {
                            value: 0,
                            name: '',
                            itemStyle: {
                                normal: {
                					labelLine:{
                						show:false
                					},
                                    color: 'rgba(255,255,255,.2)'
                                },
                                emphasis: {
                                    color: '#fff'
                                }
                            },
                        }]
                    }]
                };
                        myChart.setOption(option);
                        window.addEventListener("resize",function(){
                            myChart.resize();
                        });
	    }else{
	        var myChart = echarts.init($("#rode01")[0]);
                    option = {
            			  tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            x: 'right',
                            data:['指纹','人脸','密码','指纹+人脸','指纹+密码'],
                            textStyle:{
                                color:"#e9ebee"
                            }
                        },
                        series: [
                            {
                                name:'识别占比',
                                type:'pie',
                                center:['35%','40%'],
                                radius: ['30%', '80%'],
                                avoidLabelOverlap: false,
            					hoverAnimation:false,
                                label: {
                                    normal: {
                                        show: true
                                    },
                                    emphasis: {
                                        show: true,
                                        textStyle: {
                                            fontSize: '30',
                                            fontWeight: 'bold'
                                        }
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true,
            								position: 'inner',
            							formatter:'{d}%'
                                        },
                                        labelLine: {
                                            show: false
                                        }
                                    }
                                },
                                data:[
                                    {value:zw, name:'指纹'},
                                    {value:rl, name:'人脸'},
                                    {value:mm, name:'密码'},
                                    {value:zwrl, name:'指纹+人脸'},
                                    {value:zwmm, name:'指纹+密码'}
                                ]
                            }
                        ]
                    };


                    myChart.setOption(option);
                    window.addEventListener("resize",function(){
                                myChart.resize();
                            });
	    }
	}

	 function leftBottom(yy,sj,db) {
         var myChart = echarts.init($("#container6")[0]);
        var option = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                x: 46,
                y:30,
                x2:32,
                y2:40,
                borderWidth: 0
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    data:['当天预约','当天上缴','当天调拨'],

                    splitLine: {
                        show: false
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#a4a7ab',
                            align: 'center'
                        }
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    splitLine: {
                        show: true
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#a4a7ab'
                        }
                    }
                }
            ],
            series : [
                {
                    name:'次数',
                    type:'bar',
                    barWidth:'30',
                    data:[yy,sj,db],
                    itemStyle: {
                        normal: {
                            color:"#0aff6c"
                        }
                    }

                }
            ]
        };
        myChart.setOption(option);
         window.addEventListener("resize",function(){
                    myChart.resize();
                });
    }

	function lefttop(ps,pstotal,hs,hstotal,dd,ddtotal){
	    if(ps==0&&pstotal==0){
	        pstotal=1;
	    }
	     if(hs==0&&hstotal==0){
            hstotal=1;
        }
        if(dd==0&&ddtotal==0){
            ddtotal=1;
        }

    		var myChart = echarts.init(document.getElementById('zb1'));
    option = {
        series: [{

            type: 'pie',
            radius: ['60%', '80%'],
            color:'#49bcf7',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: ps,
                name: '女消费',
                itemStyle:{
    				normal:{
    					label:{
    						show:true,
    						position:'center',
    						formatter:'{d}%'
    					},
    					labelLine:{
    						show:false
    					}
    				}
    			}
            }, {
                value: pstotal-ps,
                name: '',
                itemStyle: {
                    normal: {
    					labelLine:{
    						show:false
    					},
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });

    	var myChart1 = echarts.init(document.getElementById('zb2'));

    option = {
        series: [{
            type: 'pie',
            radius: ['60%', '80%'],
            color:'#49bcf7',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: hs,
                name: '女消费',
                itemStyle:{
    				normal:{
    					label:{
    						show:true,
    						position:'center',
    						formatter:'{d}%'
    					},
    					labelLine:{
    						show:false
    					}
    				}
    			}
            }, {
                value: hstotal-hs,
                name: '',
                itemStyle: {
                    normal: {
    					labelLine:{
    						show:false
    					},
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
            myChart1.setOption(option);
            window.addEventListener("resize",function(){
                myChart1.resize();
            });

    	var myChart2 = echarts.init(document.getElementById('zb3'));

    option = {
        series: [{
            type: 'pie',
            radius: ['60%', '80%'],
            color:'#49bcf7',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: dd,
                name: '女消费',
                itemStyle:{
    				normal:{
    					label:{
    						show:true,
    						position:'center',
    						formatter:'{d}%'
    					},
    					labelLine:{
    						show:false
    					}
    				}
    			}
            }, {
                value: ddtotal-dd,
                name: '',
                itemStyle: {
                    normal: {
    					labelLine:{
    						show:false
    					},
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
            myChart2.setOption(option);
            window.addEventListener("resize",function(){
                myChart2.resize();
            });


    	}

	function leftmiddle(){
		drawLayer02Label($("#layer02_04 canvas").get(0),"派送款箱总数",40,170);
		drawLayer02Label($("#layer02_05 canvas").get(0),"早晚款箱数",50,170);
		drawLayer02Label($("#layer02_06 canvas").get(0),"调拨款箱数",50,170);

		drawLayer02Label($("#layer022_04 canvas").get(0),"回收款箱总数",40,170);
		drawLayer02Label($("#layer022_05 canvas").get(0),"早晚款箱数",50,170);
		drawLayer02Label($("#layer022_06 canvas").get(0),"调拨款箱数",50,170);
	}

	function drawLayer02Label(canvasObj,text,textBeginX,lineEndX){
		canvasObj.height = canvasObj.height;
		var colorValue = '#04918B';

		var ctx = canvasObj.getContext("2d");

		ctx.beginPath();
		ctx.arc(35,55,2,0,2*Math.PI);
		ctx.closePath();
		ctx.fillStyle = colorValue;
		ctx.fill();

		ctx.moveTo(35,55);
		ctx.lineTo(60,80);
		ctx.lineTo(lineEndX,80);
		ctx.lineWidth = 1;
		ctx.strokeStyle = colorValue;
		ctx.stroke();

		ctx.font='12px Georgia';
		ctx.fillStyle = colorValue;
		ctx.fillText(text,textBeginX,92);
	}
</script>



</body>
</html>



























