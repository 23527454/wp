<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p>���ļ��ϴ�</p>
<form action="http://localhost:8181/web/guard/file/upload" method="POST" enctype="multipart/form-data">
    �ļ���<input type="file" name="file"/>
    <input type="submit"/>
</form>
<hr/>
<p>�ļ�����</p>
<a href="download">�����ļ�</a>
<hr/>
<p>���ļ��ϴ�</p>
<form method="POST" enctype="multipart/form-data" action="batch">
    <p>�ļ�1��<input type="file" name="file"/></p>
    <p>�ļ�2��<input type="file" name="file"/></p>
    <p><input type="submit" value="�ϴ�"/></p>
</form>
</body>
</html>
