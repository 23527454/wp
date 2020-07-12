<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${index.sysName}</title>
<meta name="decorator" content="blank" />
<c:set var="tabmode" value="${empty cookie.tabmode.value ? '0' : cookie.tabmode.value}" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Jumbo Soft">
<link rel="shortcut icon" type="image/x-icon" href="${ctxStatic}${index.icoUrl}" />
 <link href="${ctxStatic}/metisMenu/dist/sb-admin-2.css" rel="stylesheet" />
<c:if test="${tabmode eq '1'}">
	<%-- <link rel="Stylesheet"
		href="${ctxStatic}/jerichotab/css/jquery.jerichotab.css" /> --%>
	<script type="text/javascript"
		src="${ctxStatic}/jerichotab/js/jquery.jerichotab.js"></script>
</c:if>
<!--[if lt IE 9]>
    	<link href="${ctxStatic}/ie/bootstrap4ie8.css" rel="stylesheet">
	  <script src="${ctxStatic}/ie/html5shiv.js"></script>
	  <script src="${ctxStatic}/ie/respond.min.js"></script>
	<![endif]-->
<script type="text/javascript" src="${ctxStatic}/guard/main.js"></script>
<style type="text/css">
#main, #header {
	padding: 0;
	margin: 0;
}

#main .container-fluid {
	padding: 0 4px 0 6px;
}

#footer {
	margin: 8px 0 0 0;
	padding: 3px 0 0 0;
	font-size: 11px;
	text-align: center;
	border-top: 2px solid #0663A2;
}

#footer, #footer a {
	color: #999;
}

#left {
	overflow-x: hidden;
	overflow-y: auto;
}

#left .collapse {
	position: static;
}

#userControl>li>a { /*color:#fff;*/
	text-shadow: none;
}

#userControl>li>a:hover, #user #userControl>li.open>a {
	background: transparent;
}

#header {
	max-height: 100% !important;
}

.metismenu ul a {
	padding: 10px 15px 10px 30px;
}

.metismenu ul {
	padding: 0;
	margin: 0;
	list-style: none;
}

.metismenu a {
	position: relative;
	display: block;
	padding: 13px 15px;
	outline-width: 0;
	transition: all .3s ease-out;
}

.metismenu ul li a.active {
	background-color: #eee;
}

.metismenu ul li a:hover {
	background-color: #eee;
}

.selected {
	color: #ffaaaa;
	background-color: #eee;
}

/* .modal-open[style="padding-right: 17px;"] .navbar-fixed-top,
		.modal-open[style="padding-right: 17px;"] .navbar-fixed-bottom {
			padding-right: 17px;
		} */
body, .navbar-fixed-top, .navbar-fixed-bottom {
	margin-right: 0 !important;
}

body.modal-open {
	padding-right: 0px !important;
	overflow: hidden;
}

body.modal-open[style] {
	padding-right: 0px !important;
}

.modal::-webkit-scrollbar {
	width: 0 !important; /*removes the scrollbar but still scrollable*/
	/* reference: http://stackoverflow.com/a/26500272/2259400 */
}

.navbar {min-height:30px;}

.navbar-nav>li>a {
	height:30px;
	padding-top:6px;
}



</style>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						// <c:if test="${tabmode eq '1'}"> 初始化页签
						$.fn.initJerichoTab({
							renderTo : '#right',
							uniqueId : 'jerichotab',
							contentCss : {
								'height' : $('#right').height()
										- tabTitleHeight
							},
							tabs : [],
							loadOnce : true,
							tabWidth : 110,
							titleHeight : tabTitleHeight
						});//</c:if>

						// 绑定菜单单击事件
						$("#menu a.menu")
								.click(
										function() {
										console.log('click');
											// 一级菜单焦点
											$("#menu li.menu").removeClass(
													"active");
											$(this).parent().addClass("active");
											// 左侧区域隐藏
											if ($(this).attr("target") == "mainFrame") {
												$("#left,#openClose").hide();
												wSizeWidth();
												// <c:if test="${tabmode eq '1'}"> 隐藏页签
												$(".jericho_tab").hide();
												$("#mainFrame").show();//</c:if>
												return true;
											}
											// 左侧区域显示
											$("#left,#openClose").show();
											if (!$("#openClose").hasClass(
													"close")) {
												$("#openClose").click();
											}
											// 显示二级菜单
											var menuId = "#menu-"
													+ $(this).attr("data-id");
											if ($(menuId).length > 0) {
												$("#left .metismenu").hide();
												$(menuId).show();
												// 初始化点击第一个二级菜单
												if (!$(menuId + " li:first")
														.hasClass('active')) {
													$(menuId + " li:first a")
															.click();
												}
												if ($(
														menuId
																+ " li:first ul:first")
														.is(":visible")) {
													$(
															menuId
																	+ " li:first ul:first a:first i")
															.trigger("click");
												}
												// 初始化点击第一个三级菜单
												/*$(menuId + " .accordion-body li:first li:first a:first i").click(); */
											} else {
												// 获取二级菜单数据
												$
														.get(
																$(this)
																		.attr(
																				"data-href"),
																function(data) {
																	if (data
																			.indexOf("id=\"loginForm\"") != -1) {
																		alert('未登录或登录超时。请重新登录，谢谢！');
																		top.location = "${ctx}";
																		return false;
																	}
																	$(
																			"#left .metismenu")
																			.hide();
																	$("#left")
																			.append(
																					data);
																	$(menuId)
																			.metisMenu();
																	// 链接去掉虚框
																	/* $(menuId + " a").bind("focus",function() {
																		if(this.blur) {this.blur()};
																	}); */

																	if (!$(
																			menuId
																					+ " li:first")
																			.hasClass(
																					'active')) {
																		$(
																				menuId
																						+ " li:first a")
																				.click();
																	}
																	if ($(
																			menuId
																					+ " li:first ul:first")
																			.is(
																					":visible")) {
																		$(
																				menuId
																						+ " li:first ul:first a:first i")
																				.trigger(
																						"click");
																	}
																});
											}
											// 大小宽度调整
											wSizeWidth();
											return false;
										});
						// 初始化点击第一个一级菜单
						$("#menu a.menu:first span").click();
						// <c:if test="${tabmode eq '1'}"> 下拉菜单以选项卡方式打开
						$("#userInfo .dropdown-menu a").mouseup(function() {
							return addTab($(this), true);
						});// </c:if> */
						// 鼠标移动到边界自动弹出左侧菜单
						$("#openClose").mouseover(function() {
							if ($(this).hasClass("open")) {
								$(this).click();
							}
						});

					});
	// <c:if test="${tabmode eq '1'}"> 添加一个页签
	function addTab($this, refresh) {
		$(".jericho_tab").show();
		$("#mainFrame").hide();
		$.fn.jerichoTab.addTab({
			tabFirer : $this,
			title : $this.text(),
			closeable : true,
			data : {
				dataType : 'iframe',
				dataLink : $this.attr('href')
			}
		}).loadData(refresh);
		return false;
	}// </c:if>
/* 	var msg;
	var ints = setInterval("checkUserOnline()", 500)
	function checkUserOnline() {
		$.ajax({
			type : "POST",
			url : "${adminPath}/web/sys/login/checkUserOnline",
			data : {},
			success : function(data) {
				msg = data;
			}
		});
		if (msg == 'null' || msg == '' || typeof (msg) == 'undefined') {
			return;
		} else {
			alert(msg);
		}
	} */
</script>
</head>
<body>
	<div id="main">
	 <div id="headerlogo" class="navbar navbar-default navbar-static-top" style="margin-bottom:1px;height:60px;">
		 <div class="navbar-header">
			<div class="navbar-brand">
				<image style="display:inline;height:45px;width:45px;margin-top:-10px;" src="${ctxStatic}${index.logoUrl}"/>${index.sysName}
			</div>
		</div>
		<div>
			<ul id="userControl" class="nav navbar-nav navbar-right" style="padding-top:0px;">
					<li id="userInfo" class="dropdown" ><a class="dropdown-toggle"
						data-toggle="dropdown" href="#" title="个人信息">
						<div style="display:block;">
						<span style="margin-bottom:15px;font-size:15px;" class="glyphicon glyphicon-time"/><span id="ctime" style="font-size:14px;"/>
						</div>
							<span class="glyphicon glyphicon-user" style="font-size:15px;"></span>${fns:getUser().name}<span>/</span><span>${fns:getOneRole().name}</span><span id="notifyNum"
							class="label label-info hide"></span>
					</a>
						</li>
						 <li style="opacity:.3;margin-top:20px;"><span>&nbsp;&nbsp;|&nbsp;&nbsp;</span></li>
					<li style="margin-top:13px;"><a href="${ctx}/logout" title="退出登录" ><span class="glyphicon glyphicon-off" style="font-size:25px;"/><span style="font-size:14px;">退出</span></a></li>
					<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
				</ul>
		</div>
	</div> 
	<%-- <div class="container-fluid	">   <!--头部存放容器-->
           <div id="headerlogo" class="navbar navbar-default navbar-static-top" style="margin-bottom:30px;">
           <div class="container-fluid	"> 
		<div class="navbar-brand"><image style="display:inline;" src="${ctxStatic}/favicon.ico"/>${index.sysName}</div>
	</div> 
        </div> --%>

		<div id="header" class="navbar navbar-default navbar-static-top" style="height:30px;">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="  sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="  icon-bar"></span>
				</button>
			</div>

			<%-- <c:if test="${cookie.theme.value eq 'cerulean'}">
					<div id="user" style="position:absolute;top:0;right:0;"></div>
					<div id="logo" style="background:url(${ctxStatic}/images/logo_bg.jpg) right repeat-x;width:100%;">
						<div style="background:url(${ctxStatic}/images/logo.jpg) left no-repeat;width:100%;height:70px;"></div>
					</div>
					<script type="text/javascript">
						$("#productName").hide();$("#user").html($("#userControl"));$("#header").prepend($("#user, #logo"));
					</script>
				</c:if> --%>
			<div class="collapse navbar-collapse">
				<%-- <ul id="userControl" class="nav navbar-nav navbar-right">
					 <li id="themeSwitch" class="dropdown"><a
						class="dropdown-toggle" data-toggle="dropdown" href="#" title=" "><i
							class="icon-th-large"></i></a>
						<ul class="dropdown-menu">
							<c:forEach items="${fns:getDictList('theme')}" var="dict">
								<li><a href="#"
									onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li>
							</c:forEach>
							<li><a
								href="javascript:cookie('tabmode','${tabmode eq '1' ? '0' : '1'}');location=location.href">${tabmode eq '1' ? '关闭' : '开启'}页签模式</a></li>
						</ul> <!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
					</li> 
					<li id="userInfo" class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#" title="个人信息">您好,
							${fns:getUser().name}&nbsp;<span id="notifyNum"
							class="label label-info hide"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${ctx}/sys/user/info" target="mainFrame"><i
									class="icon-user"></i>&nbsp; 个人信息</a></li>
							<li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i
									class="icon-lock"></i>&nbsp; 修改密码</a></li>
						</ul></li>
					<li><a href="${ctx}/logout" title="退出登录">退出</a></li>
					<li>&nbsp;</li>
				</ul> --%>
				<ul id="menu" class="nav navbar-nav"
					style="*white-space: nowrap; float: none;">
					<c:set var="firstMenu" value="true" />
					<c:forEach items="${fns:getMenuList()}" var="menu"
						varStatus="idxStatus">
						<c:if test="${menu.parent.id eq '1000000000'&&menu.isShow eq '1'}">
							<li
								class="menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
								<c:if test="${empty menu.href}">
									<a class="menu" href="javascript:"
										data-href="${ctx}/sys/menu/tree?parentId=${menu.id}"
										data-id="${menu.id}"><span style="font-weight:bold;">${menu.name}</span></a>
								</c:if> <c:if test="${not empty menu.href}">
									<a class="menu"
										href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}"
										data-id="${menu.id}" target="mainFrame"><span style="font-weight:bold;">${menu.name}</span></a>
								</c:if>
							</li>
							<c:if test="${firstMenu}">
								<c:set var="firstMenuId" value="${menu.id}" />
							</c:if>
							<c:set var="firstMenu" value="false" />
						</c:if>
					</c:forEach>
					<%--
						<shiro:hasPermission name="cms:site:select">
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fnc:getSite(fnc:getCurrentSiteId()).name}<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<c:forEach items="${fnc:getSiteList()}" var="site"><li><a href="${ctx}/cms/site/select?id=${site.id}&flag=1">${site.name}</a></li></c:forEach>
							</ul>
						</li>
						</shiro:hasPermission> --%>
				</ul>

			</div>
			<!--/.nav-collapse -->

		</div>
	</div>

	<div class="container-fluid" >
		<div id="content" class="row">
			<div id="left">
				<%-- 
					<iframe id="menuFrame" name="menuFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe> --%>


			</div>
			<div id="openClose" class="close">&nbsp;</div>
			<div id="right">
				<iframe id="mainFrame" name="mainFrame" src=""
					style="overflow: visible;" scrolling="yes" frameborder="no"
					width="100%" height="650"></iframe>
			</div>
		</div>
		<div id="footer" class="row footer" style="background-color:#54b4eb;"><%-- Copyright &copy;
			2012-${fns:getConfig('copyrightYear')} ${index.sysName} Company: ${index.corporationName}- Versions
			${index.versionNumber} --%>
			<span style="float:left;margin-left:20px;font-weight:bold;color:#ffffff;">
				${index.corporationName}
			</span>	
			<span style="float:right;margin-right:20px;font-weight:bold;color:#ffffff;">版本号-${index.versionNumber}</span>
			<input type="text" value="${index.mainColor}" id="mainColor" style="display:none;"/>
		</div>
	</div>
	<script type="text/javascript">
		var leftWidth = 160; // 左侧窗口大小
		var tabTitleHeight = 33; // 页签的高度
		var htmlObj = $("html"), mainObj = $("#main");
		var headerObj = $("#header"), footerObj = $("#footer"),headerLogoObj=$("#headerlogo");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize() {
			var minHeight = 500, minWidth = 980;
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({
				"overflow-x" : strs[1] < minWidth ? "auto" : "hidden",
				"overflow-y" : strs[0] < minHeight ? "auto" : "hidden"
			});
			mainObj.css("width", strs[1] < minWidth ? minWidth - 10 : "auto");
			frameObj.height((strs[0] < minHeight ? minHeight : strs[0])
					- headerObj.height() - footerObj.height()-headerLogoObj.height()
					- (strs[1] < minWidth ? 42 : 28)+10);
			$("#openClose").height($("#openClose").height() - 5);// <c:if test="${tabmode eq '1'}"> 
			$(".jericho_tab iframe").height(
					$("#right").height() - tabTitleHeight); // </c:if>
			wSizeWidth();
			
			document.getElementById('ctime').innerHTML = new Date()
                                .toLocaleString();
                        setInterval(
                                "document.getElementById('ctime').innerHTML=new Date().toLocaleString();",
                                1000);

		}
		function wSizeWidth() {
			if (!$("#openClose").is(":hidden")) {
				var leftWidth = ($("#left").width() < 0 ? 0 : $("#left")
						.width());
				$("#right").width(
						$("#content").width() - leftWidth
								- $("#openClose").width() - 5);
			} else {
				$("#right").width("100%");
			}
		}// <c:if test="${tabmode eq '1'}"> 
		function openCloseClickCallBack(b) {
			$.fn.jerichoTab.resize();
		} // </c:if>
		document.getElementById('headerlogo').style.background=document.getElementById('mainColor').value;
		document.getElementById('footer').style.background=document.getElementById('mainColor').value;
		document.getElementById('header').style.background=document.getElementById('mainColor').value;
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>