<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@ page isELIgnored="false"%>
<script type="text/javascript">
	$(document).ready(function() {
		var selectedOfficeId = '${selectedOfficeId}';
		parent.refreshTree(selectedOfficeId);
	});
</script>
</head>
</html>