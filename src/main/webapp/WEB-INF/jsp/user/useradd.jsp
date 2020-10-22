<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2020/10/15
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fm:form modelAttribute="user" method="post">
    <div>用户编码：<fm:input path="userCode"/><fm:errors path="userCode"/></div>
    <div>用户名称：<fm:input path="userName"/><fm:errors path="userName"/></div>
    <div>用户密码：<fm:password path="userPassword"/><fm:errors path="userPassword"/></div>
    <div>用户性别：<fm:radiobutton path="gender" value="1"/>男
        <fm:radiobutton path="gender" value="0"/>女
    </div>
    <div>用户生日：<fm:input path="birthday" Class="Wdate" readonly="readonly" onclick="WdatePicker();"/><fm:errors path="birthday"/></div>
    <div>用户地址：<fm:input path="address"/></div>
    <div>联系电话：<fm:input path="phone"/></div>
    <div>用户角色：<fm:radiobutton path="userRole" value="1"/>系统管理员
        <fm:radiobutton path="userRole" value="2"/>經理
        <fm:radiobutton path="userRole" value="3"/>普通员工
    </div>
    <div><input type="submit" value="提交"></div>
</fm:form>

<script src="/statics/calendar/WdatePicker.js"></script>
</body>
</html>
