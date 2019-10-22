<!DOCTYPE html>
<html>
<head>
    <title>角色管理-添加/编辑</title>
    <#include "../../include/header.ftl">
</head>
<body>
<form class="layui-form" id="add-form" name="add-form" lay-filter="add-form" style="padding: 20px 30px 0 0;">
    <input type="hidden" name="id" value="${(role.id)!}" />
    <input type="hidden" name="permIds" value="" />
    <div class="layui-tab layui-tab-brief">
        <ul class="layui-tab-title">
            <li class="layui-this">角色信息</li>
            <li>权限分配</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <div class="sub-page-form-blank"></div>
                <div class="layui-form-item">
                    <label class="layui-form-label">角色名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="name" value="${(role.name)!}" lay-verify="required" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">角色描述</label>
                    <div class="layui-input-block">
                        <textarea name="description" class="layui-textarea">${(role.description)!}</textarea>
                    </div>
                </div>
                <div class="layui-form-item layui-hide">
                    <input type="button" lay-submit lay-filter="add-form-submit" id="add-form-submit" value="确认添加">
                </div>
            </div>
            <div class="layui-tab-item">
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>
    </div>
</form>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    var layer = layui.layer;
    var form = layui.form;
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

    //监听提交
    form.on('submit(add-form-submit)', function(data){
        var field = data.field; //获取提交的字段
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        // 获取选中的节点
        var nodeStr = "";
        var nodes = tree.getCheckedNodes();
        if (nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                if (i > 0) {
                    nodeStr += ",";
                }
                nodeStr += nodes[i].id;
            }
        }

        field.permIds = nodeStr;

        //提交 Ajax 成功后，关闭当前弹层并重载表格
        $.SaveForm({
            url: '/sys/role/save',
            param: field,
            headers:{
                'X-CSRF-TOKEN':"${_csrf.token?default('')}"
            },
            success: function(data) {
                parent.layui.table.reload('dataGrid'); //重载表格
                parent.layer.close(index); //再执行关闭
            }
        });
    });
</script>
</body>
</html>