<!DOCTYPE html>
<html>
<head>
    <title></title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-fluid">
<div class="layui-row layui-col-space15">
<div class="layui-col-md12">
<div class="layui-card">
    <div class="layui-tab layui-tab-brief">
        <ul class="layui-tab-title">
            <li class="layui-this">角色信息</li>
            <li>权限信息</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">

                <div class="layui-form">
                    <table class="layui-table 展示">
                        <colgroup>
                            <col width="30%">
                            <col width="70%">
                        </colgroup>
                        <tbody>
                            <tr>
                                <td>ID</td>
                                <td>${(role.id)!}</td>
                            </tr>
                            <tr>
                                <td>角色名称</td>
                                <td>${(role.name)!}</td>
                            </tr>
                            <tr>
                                <td>角色描述</td>
                                <td>
                                    <textarea name="description" class="layui-textarea view-textarea" readonly>${(role.description)!}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>创建时间</td>
                                <td>${(role.createTime?datetime)!}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="layui-tab-item">
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    var tree;
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey : "id",
                pIdKey : "parentId"
            }
        },
        check: {
            enable: true,
            chkStyle: "checkbox" ,
            chkboxType: { "Y": "ps", "N": "ps" }
        },
        view: {
            nameIsHTML: true
        }
    };

    function zTreeInit(){
        //初始化树
        $.get('/sys/permission/getTreeNodes',{roleId:"${(role.id)!0}"}, function(r) {
            tree = $.fn.zTree.init($("#tree"), setting, r);
        });
    }

    $(function () {
        //初始化Ztree
        zTreeInit();
    });
</script>
</body>
</html>