<!DOCTYPE html>
<html>
<head>
    <title>用户管理-用户详情页</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-fluid">
<div class="layui-row layui-col-space15">
<div class="layui-col-md12">
<div class="layui-card">
    <table class="layui-table 展示">
        <colgroup>
            <col width="30%">
            <col width="70%">
        </colgroup>
        <tbody>
        <tr>
            <td>ID</td>
            <td>${(user.id)!}</td>
        </tr>
        <tr>
            <td>所属机构</td>
            <td>${(org.shortName)!'--'}</td>
        </tr>
        <tr>
            <td>用户名</td>
            <td>${(user.userName)!}</td>
        </tr>
        <tr>
            <td>姓名</td>
            <td>${(user.nickName)!}</td>
        </tr>
        <tr>
            <td>性别</td>
            <td><script>document.write(CODE_MAP.GENERAL.SEX[${(user.sex)!1}]);</script></td>
        </tr>
        <tr>
            <td>状态</td>
            <td><script>document.write(CODE_MAP.GENERAL.ENABLE[${((user.enable)!ture)?string(1,0)}]);</script></td>
        </tr>
        <tr>
            <td>所属角色</td>
            <td>${(roleStr)!}</td>
        </tr>
        <tr>
            <td>创建时间</td>
            <td>${(user.createTime?datetime)!}</td>
        </tr>
        <tr>
            <td>上次登录IP</td>
            <td>${(user.lastLoginIp)!}</td>
        </tr>
        <tr>
            <td>上次登录时间</td>
            <td>${(user.createTime?datetime)!}</td>
        </tr>
        </tbody>
    </table>
</div>
</div>
</div>
</div>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    $(function () {
        // ...
    });
</script>
</body>
</html>