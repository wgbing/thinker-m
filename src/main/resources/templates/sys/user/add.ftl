<!DOCTYPE html>
<html>
<head>
    <title>系统管理-用户管理-添加/编辑</title>
    <#include "../../include/header.ftl">
    <style type="text/css">
        .layui-tab-item .layui-form-checkbox {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="layui-fluid">
<div class="layui-row layui-col-space15">
<div class="layui-col-md12">
<div class="layui-card">
    <form class="layui-form" id="add-form" name="add-form" lay-filter="add-form">
        <input type="hidden" name="id" value="${(user.id)!}" />
        <input type="hidden" name="orgId" value="${(org.id)!}" />
        <div class="layui-tab layui-tab-brief">
            <ul class="layui-tab-title">
                <li class="layui-this">用户信息</li>
                <li>角色分配</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="layui-form-item">
                        <label class="layui-form-label">所属机构</label>
                        <div class="layui-input-block">
                            <input type="text" name="orgName" value="${(org.shortName)!'--'}" disabled="disabled" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="required-sing"></span>用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="userName" value="${(user.userName)!}" lay-verify="required" <#if user??>disabled</#if> autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"><#if !user??><span class="required-sing"></span></#if>密码</label>
                        <div class="layui-input-block">
                            <input type="text" name="password" value="" <#if !user??>lay-verify="required"</#if> autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="required-sing"></span>姓名</label>
                        <div class="layui-input-block">
                            <input type="text" name="nickName" value="${(user.nickName)!}" lay-verify="required" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="required-sing"></span>性别</label>
                        <div class="layui-input-block">
                            <input type="radio" name="sex" value="1" title="男" <#if ((user.sex)!1) == 1>checked</#if>>
                            <input type="radio" name="sex" value="0" title="女" <#if ((user.sex)!1) == 0>checked</#if>>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label"><span class="required-sing"></span>是否启用</label>
                        <div class="layui-input-block">
                            <input type="radio" name="enable" value="true" title="是" <#if ((user.enable)!true)>checked</#if>>
                            <input type="radio" name="enable" value="false" title="否" <#if !((user.enable)!true)>checked</#if>>
                        </div>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <#if roleList??>
                    <#list roleList as item>
                        <span style="margin-bottom:10px;">
                            <input type="checkbox" name="roleIds" <#if ((item.checked)!false)>checked</#if> value="${(item.id)!}" title="${(item.name)!}">
                        </span>
                    </#list>
                    </#if>
                </div>
            </div>
        </div>
        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="add-form-submit" id="add-form-submit" value="确认添加">
        </div>
    </form>
</div>
</div>
</div>
</div>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    var form = layui.form;

    //监听提交
    form.on('submit(add-form-submit)', function(data){
        var field = data.field; //获取提交的字段
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        var arr = new Array();
        $("input:checkbox[name='roleIds']:checked").each(function(i){
            arr[i] = $(this).val();
        });
        field.roleIds = arr.join(",");

        //提交 Ajax 成功后，关闭当前弹层并重载表格
        $.SaveForm({
            url: '/sys/user/save',
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