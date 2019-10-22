<!DOCTYPE html>
<html>
<head>
    <title>系统管理-权限管理</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md2">
            <div class="layui-card">
                <div class="layui-card-header">权限管理</div>
                <div class="layui-card-body" id="treePanel" style="overflow: auto;">
                    <ul id="permTree" class="ztree"></ul>
                </div>
            </div>
        </div>
        <div class="layui-col-md10">
            <div class="layui-card">
                <div class="layui-card-header">权限信息</div>
                <div class="layui-card-body">
                    <div class="layui-row">
                        <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                            <div class="layui-form-item" id="search-form">
                                <div class="layui-inline">
                                    <label class="layui-form-label">菜单名称</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="name" placeholder="请输入" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">授权标识</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="resKey" placeholder="请输入" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">类型</label>
                                    <div class="layui-input-inline">
                                        <select name="type">
                                            <option value="">请选择</option>
                                            <option value="1">目录</option>
                                            <option value="2">菜单</option>
                                            <option value="3">按钮</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <button class="layui-btn" lay-submit lay-filter="search">
                                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                                    </button>
                                    <button class="layui-btn" lay-submit lay-filter="reset">
                                        <i class="layui-icon layui-icon-refresh layuiadmin-button-btn"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-row">
                        <table id="dataGrid" lay-filter="dataGrid"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="add">添加</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="batchdel">批量删除</button>
    </div>
</script>
<script type="text/html" id="row-toolbar">
    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
    <a class="layui-btn layui-btn-xs" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>新增</a>
</script>
<!-- Scripts -->
<#include "../../include/footer.ftl">

<script>
    var ztree;
    var table = layui.table;
    var form = layui.form;

    $(function () {
        $("#treePanel").css('height', $(window).height()-110);
        $(window).resize(function() {
            $("#treePanel").css('height', $(window).height()-110);
        });
        zTreeInit();
    });

    var setting = {
        async : {
            enable: true,
            type: "get",
            url: "/sys/permission/tree",
            autoParam: ["id"]
        },
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "parentId"
            },
            key : {
                url : "nourl"
            }
        },
        callback : {
            onClick : function(event, treeId, treeNode) {
                table.reload('dataGrid', {
                    page: { curr: 1 },
                    where: {
                        "map[parentId]":treeNode.id
                    }
                });
            }
        }
    };

    function zTreeInit(){
        //初始化权限树
        $.get('/sys/permission/tree', {id: null},function(r) {
            ztree = $.fn.zTree.init($("#permTree"), setting, r);
        });
    }

    //刷新当前节点
    function refreshNode() {
        var nodes = ztree.getSelectedNodes();
        if(nodes.length == 0){
            zTreeInit();
        }else {
            /*强行异步加载父节点的子节点。[setting.async.enable = true 时有效]*/
            ztree.reAsyncChildNodes(nodes[0], "refresh", false);
        }
    }

    //表格执行渲染
    table.render({
        elem: '#dataGrid',
        url: '/sys/permission/list',
        height: 'full-230',
        toolbar: '#toolbar',
        cellMinWidth: 80,
        page: true, //开启分页
        where:{
            "map[parentId]":null
        },
        cols: [[{
            field: 'id',
            hide:true
        },{
            field: 'parentId',
            hide:true
        },{
            fixed: 'left',
            type:'numbers'
        },{
            fixed: 'left',
            type:'checkbox'
        },{
            field: 'name',
            title: '菜单名称',
            width:150
        },{
            field: 'parentName',
            title: '上级菜单',
            width:120
        },{
            field: 'type',
            title: '类型',
            width:60,
            templet: function(d){
                return CODE_MAP.PERM.TYPE[d.type];
            }
        },{
            field: 'resUrl',
            title: '菜单URL'
        },{
            field: 'resKey',
            title: '授权标识',
            width:120
        },{
            field: 'sortNo',
            title: '排序',
            width:60
        },{
            fixed: 'right',
            title:'操作',
            toolbar: '#row-toolbar',
            width: 220
        }]]

    });

    //监听搜索
    form.on('submit(search)', function(data){
        //执行重载
        table.reload('dataGrid', {
            where: {
                raw: JSON.stringify(data.field)
            }
        });
    });
    //监听重置
    form.on('submit(reset)', function(data){
        $("#search-form").find("input[type='text'], select").val("");
        //执行重载
        table.reload('dataGrid', {
            where: {
                raw: JSON.stringify(null)
            }
        });
    });

    //监听头工具条
    table.on('toolbar(dataGrid)', function(obj){
        var nodes = ztree.getSelectedNodes();
        if(nodes.length > 1){
            dialogAlert("只能选择一个权限根节点！","warn");
            return;
        }
        var parentId = 0;
        if(nodes.length > 0){
            parentId = nodes[0].id;
        }
        switch(obj.event){
            case 'add':
                layer.open({
                    type: 2,
                    title: '新增权限',
                    content: '/sys/permission/add?parentId='+parentId,
                    maxmin: true,
                    area: ['500px', '500px'],
                    btn: ['确定', '取消'],
                    yes: function(index, layero){
                        //点击确认触发 iframe 内容中的按钮提交
                        var submit = layero.find('iframe').contents().find("#add-form-submit");
                        submit.click();
                    }
                });
                break;
            case 'batchdel':
                var rows = table.checkStatus(obj.config.id);
                if(rows.data.length <=0){
                    dialogAlert("请选择数据","warn");
                    return;
                }
                var ids = [];
                $.each(rows.data, function(idx, item){
                    ids[idx] = item.id;
                });
                $.RemoveForm({
                    url: '/sys/permission/remove',
                    param: ids,
                    success: function(data) {
                        if (data.code == 0) {
                            table.reload("dataGrid");
                            refreshNode();
                        }
                    }
                });
                break;
        };
    });

    //监听行工具条
    table.on('tool(dataGrid)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var parentId = 0;
        if(data.parentId != null){
            parentId = data.parentId;
        }
        switch(layEvent){
            case 'edit':
                layer.open({
                    type: 2,
                    title: '编辑权限',
                    content: '/sys/permission/add?id='+data.id+'&parentId='+parentId,
                    maxmin: true,
                    area: ['500px', '500px'],
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
                    title: '新增权限',
                    content: '/sys/permission/add?parentId='+data.id,
                    maxmin: true,
                    area: ['500px', '500px'],
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
                    url: '/sys/permission/remove',
                    param: ids,
                    success: function(data) {
                        if (data.code == 0) {
                            table.reload("dataGrid");
                            refreshNode();
                        }
                    }
                });
                break;
        };
    });

</script>

</body>
</html>