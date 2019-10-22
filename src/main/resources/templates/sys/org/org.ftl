<!DOCTYPE html>
<#assign security=JspTaglibs["/META-INF/security.tld"] />
<#assign datas=dictService.getDictByConfigKey("ZZLX")/>
<html>
<head>
    <title>组织管理</title>
    <#include "../../include/header.ftl">
    <style>
        /** 箭头未展开 */
        #treeGrid + .treeTable .treeTable-icon .layui-icon-triangle-d:before {
            content: "\e602";
        }
        /** 箭头展开 */
        #treeGrid + .treeTable .treeTable-icon.open .layui-icon-triangle-d:before {
            content: "\e61a";
        }
        /** 文件图标 */
        #treeGrid + .treeTable .treeTable-icon .layui-icon-file:before {
            content: "";
        }
        /** 文件夹未展开 */
        #treeGrid + .treeTable .treeTable-icon .layui-icon-layer:before {
            content: "\e637";
        }
        /** 文件夹展开 */
        #treeGrid + .treeTable .treeTable-icon.open .layui-icon-layer:before {
            content: "\e716";
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <table id="treeGrid" class="layui-table" lay-filter="treeGrid"></table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="add">添加</button>
        <button class="layui-btn layui-btn-sm layui-btn-normal" id="expandOrFold" lay-event="expandOrFold">折叠</button>
    </div>
</script>
<!-- 操作列 -->
<script type="text/html" id="row-toolbar">
    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
    <a class="layui-btn layui-btn-xs" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>新增</a>
</script>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    var renderTable;
    layui.config({
        base: '/plugins/layuiadmin/layui_exts/'
    }).extend({
        treetable: 'treetable/treetable'
    }).use(['treetable'], function () {
        var table = layui.table;
        var treetable = layui.treetable;

        // 渲染表格
        renderTable = function () {
            layer.load(2);
            treetable.render({
                treeColIndex: 1,
                treeSpid: 0,
                treeIdName: 'id',//id字段的名称
                treePidName: 'parentId',//pid字段的名称
                treeIcon:true,//开启图标 true=显示图标 false=不显示图标，默认不显示图标
                elem: '#treeGrid',
                url: '/sys/org/list',
                page: false,
                height: 'full-100',
                toolbar: '#toolbar',
                cols: [[{
                    type: 'numbers'
                },{
                    field: 'name',
                    title: '组织名称'
                },{
                    field: 'shortName',
                    title: '组织简称'
                },{
                    field: 'type',
                    title: '组织类型',
                    templet: function(d){
                        return selectDictLabel(${datas},d.type);
                        // return CODE_MAP.ORG.TYPE[d.type];
                    }
                },{
                    field: 'enable',
                    title: '状态',
                    width:80,
                    templet: function(d){
                        return CODE_MAP.ORG.ENABLE[d.enable?1:0];
                    }
                },{
                    field: 'remark',
                    title: '备注'
                },{
                    templet: '#row-toolbar',
                    title: '操作',
                    width:250
                },{
                    field: 'id',
                    hide:true
                },{
                    field: 'parentId',
                    hide:true
                }]],
                done: function () {
                    layer.closeAll('loading');
                }
            });
        };

        renderTable();

        //监听头工具条
        table.on('toolbar(treeGrid)', function(obj){
            switch(obj.event){
                case 'add':
                    layer.open({
                        type: 2,
                        title: '新增组织',
                        content: '/sys/org/add',
                        maxmin: true,
                        area: ['350px', '500px'],
                        btn: ['确定', '取消'],
                        yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                case 'expandOrFold':
                    var text = $('#expandOrFold').text();
                    if(text == "折叠"){
                        treetable.foldAll('#treeGrid');
                        $('#expandOrFold').text("展开");
                    }else if(text == "展开"){
                        treetable.expandAll('#treeGrid');
                        $('#expandOrFold').text("折叠");
                    }
                    break;
            };
        });

        //监听行工具条
        table.on('tool(treeGrid)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            switch(layEvent){
                case 'edit':
                    layer.open({
                        type: 2,
                        title: '编辑组织',
                        content: '/sys/org/add?id='+data.id+'&parentId='+data.parentId,
                        maxmin: true,
                        area: ['350px', '530px'],
                        btn: ['确定', '取消'],
                        yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                case 'add':
                    layer.open({
                        type: 2,
                        title: '新增子级组织',
                        content: '/sys/org/add?parentId='+data.id,
                        maxmin: true,
                        area: ['350px', '530px'],
                        btn: ['确定', '取消'],
                        yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                case 'del':
                    var ids = [];
                    ids.push(data.id);
                    $.RemoveForm({
                        url: '/sys/org/remove',
                        param: ids,
                        headers:{
                            'X-CSRF-TOKEN':"${_csrf.token?default('')}"
                        },
                        success: function(data) {
                            if (data.code == 0) {
                                renderTable();
                            }
                        }
                    });
                    break;
            };
        });

    });

    //刷新表格
    function refresh() {
        renderTable();
    }
</script>
</body>
</html>