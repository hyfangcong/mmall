<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<html>
<body>
<h2>Hello World!</h2>

springMVC上传文件
<form name="form1" action="/mmall_war/manage/product/upload.do" method="post" enctype="multipart/form-data" >
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件"/>
</form>
</body>
</html>
