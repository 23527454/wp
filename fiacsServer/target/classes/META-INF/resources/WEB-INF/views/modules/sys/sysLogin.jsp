<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${index.sysName} 登录</title>
	<meta name="decorator" content="blank"/>
	<link rel="shortcut icon" type="image/x-icon" href="${ctxStatic}${index.icoUrl}"/>
	<link href="${ctxStatic}/custom/form-elements.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/awesome/css/font-awesome.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/custom/jquery.backstretch.min.js" type="text/javascript"></script>
	
	<style type="text/css">
      html,body,table{width:100%;text-align:center;}.form-signin-heading{font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:relative;text-align:left;width:300px;padding:25px 29px 29px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#999;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin .btn.btn-large{font-size:16px;} .form-signin 
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:80px;padding-top:20px;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;width:270px;font-weight:normal;color:inherit;margin:0;}
	    body {
		    font-family: 'Roboto', sans-serif;
		    font-size: 16px;
		    font-weight: 300;
		    color: #888;
		    line-height: 30px;
		    text-align: center;
		}
		
		strong { font-weight: 500; }
		
		a, a:hover, a:focus {
		color:red;
			text-decoration: none;
		    -o-transition: all .3s; -moz-transition: all .3s; -webkit-transition: all .3s; -ms-transition: all .3s; transition: all .3s;
		}
		
		h1, h2 {
			margin-top: 10px;
			font-size: 38px;
		    font-weight: 100;
		    color: #555;
		    line-height: 50px;
		}
		
		h3 {
			font-size: 22px;
		    font-weight: 300;
		    color: #555;
		    line-height: 30px;
		}
		
		img { max-width: 100%; }
		
		.form-control-feedback {
 		   line-height: 50px;
  		
		} 
		
		.form-center .username input,.form-center .password input{
    height: 50px;
    padding-left: 60px;
}
    </style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},
					password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
			$.backstretch("${ctxStatic}/images/login-bk.png");
		});
		// 如果在框架或在对话框中，则弹出提示并跳转到首页
		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
			alert('未登录或登录超时。请重新登录，谢谢！');
			top.location = "${ctx}";
		}
	</script>
</head>
<body>
	<!--[if lte IE 6]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
	 <div class="header">
		<div id="messageBox" class="alert alert-danger alert-dismissible ${empty message ? 'hide' : ''}" role="alert">
  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  			<label id="loginError"><strong>失败!</strong> ${message}</label>
		</div>
	</div> 
	<%--  <div id="headerlogo" class="navbar navbar-default navbar-static-top" style="margin-bottom:1px;">
		 <div class="navbar-header">
			<div class="navbar-brand">
				<image style="display:inline;max-height:45px;margin-top:-12px;" src="${ctxStatic}/favicon.ico"/>${index.sysName}
			</div>
		</div></div> --%>
	<div class="top-content">
            <div class="inner-bg" style="padding-bottom:0px;">
                <div class="container-fluid">
                    <div class="row" style="position:absolute;top:20%;width:100%;">
                        <div class="col-xs-8 col-xs-offset-2 text">
                            <h1><strong>${index.sysName}</strong></h1>
                        </div>
                    </div> 
                    <div class="row" style="opacity:.85;position:absolute;top:30%;width:100%;">
                        <div class="col-xs-6 col-xs-offset-3 form-box" style="width:23%;margin-left:50%;">
                        	 <div class="form-top" style="padding:0 0 0 0;">
                        		<div  class="row">
                        			<div class="text-center">
	                        			<h1 style="font-weight:bold;font-size:20px;">用户登录</h1>
                        			</div>
                        		</div>
                            </div> 
                            <div class="form-bottom" style="margin-bottom:20px;">
			                    <form id="loginForm" class="login-form" action="${ctx}/login" method="POST">
			                   		<div class="form-group has-feedback">
				                   		<div class="useranname">	
				                   		<label class="sr-only" for="username">密码</label>										 
				                   		<span class="glyphicon glyphicon-user form-control-feedback"></span>
											<input type="text" id="username" name="username" class="form-username form-control" value="${username}">
				                   		</div>
									</div>
									<div class="form-group has-feedback">
										<div class="password">
											<label class="sr-only" for="password">密码</label>
											<span class="fa fa-key form-control-feedback"></span>
											<input type="password" id="password" name="password" class="form-password form-control">
										</div>
									</div>
									<%--<c:if test="${isValidateCodeLogin}"><div class="validateCode">
										<div class="form-group">
											<label class="input-label mid" for="validateCode">验证码</label>
											<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
										</div>
									</div></c:if>
									<label for="mobile" title="手机登录"><input type="checkbox" id="mobileLogin" name="mobileLogin" ${mobileLogin ? 'checked' : ''}/></label> --%>
									<label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}/> 记住我</label>
									<input class="btn btn-large btn-primary" type="submit" value="登 录" style="float:right;"/>&nbsp;&nbsp;
									<%-- <div id="themeSwitch" class="dropdown pull-right">
										<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}<b class="caret"></b></a>
										<ul class="dropdown-menu">
										  <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
										</ul>
										<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
									</div> --%>
								</form>
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
	
	<%-- <div class="footer">
		Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="${pageContext.request.contextPath}${fns:getFrontPath()}">${index.sysName}</a> - Powered By <a href="http://www.jumbo-soft.com" target="_blank">Jumbo Soft</a> ${fns:getConfig('version')} 
	</div>
	<script src="${ctxStatic}/flash/zoom.min.js" type="text/javascript"></script> --%>
</body>
</html>