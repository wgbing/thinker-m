<!DOCTYPE html>
<html>
<head>
    <title>组织管理-新增/编辑</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-form" lay-filter="add-form" id="add-form" style="padding: 20px 30px 0 0;">
    <input type="hidden" name="id" value="${(org.id)!''}">
    <input type="hidden" name="parentId" value="${(parentOrg.id)!'0'}">
    <#if (parentOrg.id)??>
        <div class="layui-form-item">
            <label class="layui-form-label">上级组织</label>
            <div class="layui-input-inline">
                <input type="text" name="parentName" lay-verify="required" value="${(parentOrg.name)!''}"
                       autocomplete="off" class="layui-input" readonly="readonly">
            </div>
        </div>
    </#if>
    <div class="layui-form-item">
        <label class="layui-form-label">组织名称</label>
        <div class="layui-input-inline">
            <input type="text" name="name" lay-verify="required"
                   placeholder="组织名称" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">组织简称</label>
        <div class="layui-input-inline">
            <input type="text" name="shortName" lay-verify="required"
                   placeholder="组织简称" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">组织类型</label>
        <div class="layui-input-inline">
            <select id="type" name="type" lay-verify="required">
                <option value="">请选择</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-inline">
            <input type="checkbox" lay-verify="required" lay-filter="enable"
                   <#if (org.enable)?? && (org.enable)?c == "true">checked</#if>
                   name="enable" lay-skin="switch" lay-text="正常|停用" value="true">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-inline">
            <textarea name="remark" placeholder="请输入备注" class="layui-textarea"></textarea>
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
        "name": "${(org.name)!''}",
        "shortName": "${(org.shortName)!''}",
        "remark": "${(org.remark)!''}"
    });

    $.ajax({
        url: "/sys/dict/listDictByConfigKey",
        type: "get",
        data: {
            configKey:"ZZLX"
        },
        success: function(data) {
            $.each(data, function (index, item) {
                var option = new Option(item.name, item.value);
                if("${(org.type)!''}" != null && "${(org.type)!''}" != ""){
                    if("${(org.type)!''}" == item.value){
                        option.setAttribute("selected",'true');
                    }
                }

                $('#type').append(option);//下拉菜单里添加元素
            });
            form.render("select");
        },
        error: ajaxErrorHandler
    });


    //监听提交
    form.on('submit(add-form-submit)', function(data){
        var field = data.field; //获取提交的字段
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引

        //提交 Ajax 成功后，关闭当前弹层并重载表格
        $.SaveForm({
            url: '/sys/org/save',
            param: field,
            headers:{
                'X-CSRF-TOKEN':"${_csrf.token?default('')}"
            },
            success: function(data) {
                parent.refresh();//刷新表格
                parent.layer.close(index); //再执行关闭
            }
        });
    });
</script>

</body>
</html>