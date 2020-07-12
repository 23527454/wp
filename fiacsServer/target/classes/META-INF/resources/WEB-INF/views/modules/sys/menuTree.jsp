<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%><%--
<html>
<head>
	<title>菜单导航</title>
	<meta name="decorator" content="blank"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".accordion-heading a").click(function(){
				$('.accordion-toggle i').removeClass('icon-chevron-down');
				$('.accordion-toggle i').addClass('icon-chevron-right');
				if(!$($(this).attr('href')).hasClass('in')){
					$(this).children('i').removeClass('icon-chevron-right');
					$(this).children('i').addClass('icon-chevron-down');
				}
			});
			$(".accordion-body a").click(function(){
				$("#menu-${param.parentId} li").removeClass("active");
				$("#menu-${param.parentId} li i").removeClass("icon-white");
				$(this).parent().addClass("active");
				$(this).children("i").addClass("icon-white");
				//loading('正在执行，请稍等...');
			});
			//$(".accordion-body a:first i").click();
			//$(".accordion-body li:first li:first a:first i").click();
		});
	</script>
</head>
<body> --%>

<ul class="nav metismenu" id="menu-${param.parentId}">
	<c:set var="menuList" value="${fns:getMenuList()}" />
	<c:set var="firstMenu" value="true" />
	<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
		<c:if
			test="${menu.parent.id eq (not empty param.parentId ? param.parentId:1)&&menu.isShow eq '1'}">

			<li><a class="has-arrow" aria-expanded="true"
				data-toggle="collapse" data-parent="#menu-${param.parentId}"
				data-href="#collapse-${menu.id}" href="#collapse-${menu.id}"
				title="${menu.remarks}">&nbsp;${menu.name}</a>
				<ul aria-expanded="true">
					<c:forEach items="${menuList}" var="menu2">
						<c:if test="${menu2.parent.id eq menu.id&&menu2.isShow eq '1'}">

							<li><a aria-expanded="true" data-href=".menu3-${menu2.id}"
								href="${fn:indexOf(menu2.href, '://') eq -1 ? ctx : ''}${not empty menu2.href ? menu2.href : '/404'}"
								target="${not empty menu2.target ? menu2.target : 'mainFrame'}"><i
									class="icon-${not empty menu2.icon ? menu2.icon : 'circle-arrow-right'}"></i>&nbsp;${menu2.name}</a>
							</li>
						</c:if>
					</c:forEach>
				</ul></li>

		</c:if>
	</c:forEach>
</ul>


<%--
</body>
</html> --%>