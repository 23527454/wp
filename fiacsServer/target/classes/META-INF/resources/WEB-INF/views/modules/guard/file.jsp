<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p>单文件上传</p>
<form action="http://localhost:8181/web/guard/file/upload" method="POST" enctype="multipart/form-data">
    文件：<input type="file" name="file"/>
    <input type="submit"/>
</form>
<hr/>
<p>文件下载</p>
<a href="download">下载文件</a>
<hr/>
<p>多文件上传</p>
<form method="POST" enctype="multipart/form-data" action="batch">
    <p>文件1：<input type="file" name="file"/></p>
    <p>文件2：<input type="file" name="file"/></p>
    <p><input type="submit" value="上传"/></p>
</form>
</body>
</html>
