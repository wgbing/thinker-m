<!DOCTYPE html>
<html>
<head>
    <title>数据字典-新增/编辑</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-form" lay-filter="add-form" id="add-form" style="padding: 20px 30px 0 0;">
    <input type="hidden" name="id" value="${(dict.id)!''}">
    <input type="hidden" name="parentId" value="${(parentDict.id)!'0'}">
    <#if (parentDict.id)??>
        <div class="layui-form-item">
            <label class="layui-form-label">父级字典</label>
            <div class="layui-input-inline">
                <input type="text" name="parentName" lay-verify="required" value="${(parentDict.name)!''}"
                       autocomplete="off" class="layui-input" readonly="readonly">
            </div>
        </div>
    </#if>
    <div class="layui-form-item">
        <label class="layui-form-label">字典名称</label>
        <div class="layui-input-inline">
            <input type="text" name="name" lay-verify="required"
                   placeholder="字典名称" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典配置</label>
        <div class="layui-input-inline">
            <input type="text" name="configKey" lay-verify="required"
                   placeholder="字典名称拼音的大写首字母" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">国标编码</label>
        <div class="layui-input-inline">
            <input type="text" name="code" placeholder="请输入国标编码" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典项值</label>
        <div class="layui-input-inline">
            <input type="text" name="value" placeholder="请输入字典项值" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" name="orderNo" lay-verify="number"
                   placeholder="请输入数字" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-inline">
            <input type="text" name="note" placeholder="请输入备注" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否锁定</label>
        <div class="layui-input-inline">
            <input type="checkbox" lay-verify="required" lay-filter="locked"
                   <#if (dict.locked)?? && (dict.locked)?c == "true">checked</#if>
                   name="locked" lay-skin="switch" lay-text="锁定|不锁" value="true">
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
    form.val("add-form", {
        "name": "${(dict.name)!''}",
        "configKey": "${(dict.configKey)!''}",
        "code": "${(dict.code)!''}",
        "value": "${(dict.value)!''}",
        "orderNo": "${(dict.orderNo)!'1'}",
        "note": "${(dict.note)!''}"
    });

    //监听提交
    form.on('submit(add-form-submit)', function(data){
        var field = data.field; //获取提交的字段
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引

        //提交 Ajax 成功后，关闭当前弹层并重载表格
        $.SaveForm({
            url: '/sys/dict/save',
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
</script>

</body>
</html>