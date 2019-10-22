<!DOCTYPE html>
<html>
<head>
    <title>权限管理-新增/编辑</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-form" lay-filter="add-form" id="add-form" style="padding: 20px 30px 0 0;">
    <input type="hidden" name="id" value="${(perm.id)!''}">
    <input type="hidden" name="parentId" value="${(parentPerm.id)!'0'}">
    <div class="layui-form-item">
        <label class="layui-form-label">类型</label>
        <div class="layui-input-block">
            <input type="radio" name="type" lay-filter="type" value="1" title="目录">
            <input type="radio" name="type" lay-filter="type" value="2" title="菜单" checked>
            <input type="radio" name="type" lay-filter="type" value="3" title="按钮">
            <input type="radio" name="type" lay-filter="type" value="4" title="其他">
        </div>
    </div>
    <#if (parentPerm.id)??>
        <div class="layui-form-item">
            <label class="layui-form-label">上级菜单</label>
            <div class="layui-input-inline">
                <input type="text" name="parentName" lay-verify="required" value="${(parentPerm.name)!''}"
                       autocomplete="off" class="layui-input" readonly="readonly">
            </div>
        </div>
    </#if>
    <div class="layui-form-item">
        <label class="layui-form-label">名称</label>
        <div class="layui-input-inline">
            <input type="text" name="name" lay-verify="required"
                   placeholder="名称" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" id="perm_url">
        <label class="layui-form-label">菜单URL</label>
        <div class="layui-input-inline">
            <input type="text" name="resUrl"
                   placeholder="菜单请求资源URL" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" id="perm_key">
        <label class="layui-form-label">授权标识</label>
        <div class="layui-input-inline">
            <input type="text" name="resKey" lay-verify="required" placeholder="请输入授权标识" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" id="perm_icon" hidden>
        <label class="layui-form-label">图标</label>
        <div class="layui-input-inline">
            <input type="text" id="icon" name="icon"
                   placeholder="icon" autocomplete="off" class="layui-input" readonly="readonly" onclick="icon()">
        </div>
    </div>
    <div class="layui-form-item" id="perm_sortNo">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" name="sortNo" lay-verify="number"
                   placeholder="请输入数字" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item layui-hide">
        <input type="button" lay-submit lay-filter="add-form-submit" id="add-form-submit" value="确认添加">
    </div>
</div>
<!-- Scripts -->
<#include "../../include/footer.ftl">

<script>
    var form = layui.form;
    <#if perm?? && (perm.type)??>
        changeRadio("${perm.type}");
    </#if>

    form.val("add-form", {
        "name": "${(perm.name)!''}",
        "resUrl": "${(perm.resUrl)!''}",
        "resKey": "${(perm.resKey)!''}",
        "type": "${(perm.type)!''}",
        "icon": "${(perm.icon)!''}",
        "sortNo": "${(perm.sortNo)!'1'}",
        "description": "${(perm.description)!''}"
    });

    //监听提交
    form.on('submit(add-form-submit)', function(data){
        var field = data.field; //获取提交的字段
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引

        //提交 Ajax 成功后，关闭当前弹层并重载表格
        $.SaveForm({
            url: '/sys/permission/save',
            param: field,
            headers:{
                'X-CSRF-TOKEN':"${_csrf.token?default('')}"
            },
            success: function(data) {
                parent.layui.table.reload('dataGrid'); //重载表格
                parent.refreshNode();//局部刷新区域树
                parent.layer.close(index); //再执行关闭
            }
        });
    });

    form.on('radio(type)', function(data){
        changeRadio(data.value);
    });

    function changeRadio(value) {
        switch (value){
            case '1':
                $("#perm_icon").show();
                $("#perm_url").hide();
                // $("#perm_key").hide();
                $("#perm_sortNo").show();
                break;
            case '2':
                $("#perm_url").show();
                $("#perm_icon").hide();
                $("#perm_sortNo").show();
                break;
            case '3':
                $("#perm_url").hide();
                $("#perm_icon").hide();
                $("#perm_sortNo").hide();
                break;
            case '4':
                $("#perm_url").show();
                $("#perm_icon").hide();
                $("#perm_sortNo").show();
                break;
        }
    }
    function icon() {
        parent.layer.open({
            type : 2,
            title: '选择图标',
            content : '/sys/permission/icon',
            closeBtn : 1,
            anim: -1,
            isOutAnim: false,
            shadeClose : false,
            shade : 0.3,
            area : ["500px", "400px"],
            btn: ['确定', '取消'],
            yes : function(index, layero) {
                var iframeWin = layero.find('iframe')[0].contentWindow;
                var icon = iframeWin.getIcon();//调用子页面的方法
                if(icon != undefined && icon != null && icon != ''){
                    $("#icon").val(icon);
                    parent.layer.close(index);//关闭子窗体
                }
            }
        })
    }

</script>

</body>
</html>