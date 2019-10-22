<!DOCTYPE html>
<html>
<head>
    <title>系统管理-数据字典</title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md2">
            <div class="layui-card">
                <div class="layui-card-header">字典目录</div>
                <div class="layui-card-body" id="treePanel" style="overflow: auto;">
                    <ul id="dictTree" class="ztree"></ul>
                </div>
            </div>
        </div>
        <div class="layui-col-md10">
            <div class="layui-card">
                <div class="layui-card-header">字典信息</div>
                <div class="layui-card-body">
                    <div class="layui-row">
                        <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                            <div class="layui-form-item" id="search-form">
                                <div class="layui-inline">
                                    <label class="layui-form-label">字典名称</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="name" placeholder="请输入" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">字典配置</label>
                                    <div class="layui-input-inline">
                                        <input type="text" name="configKey" placeholder="请输入" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <button class="layui-btn" lay-submit lay-filter="search">
                                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                                    </button>
                                </div>
                                <div class="layui-inline">
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
    var pageHeight;
    var ztree;
    var table = layui.table;
    var form = layui.form;
    var setting = {
        async : {
            enable: true,
            type: "get",
            url: "/sys/dict/tree",
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
        //初始化区域树
        $.get('/sys/dict/tree', {id: null},function(r) {
            ztree = $.fn.zTree.init($("#dictTree"), setting, r);
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
        url: '/sys/dict/list',
        height: 'full-190',
        toolbar: '#toolbar',
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
            title: '字典名称',
            minWidth:100
        },{
            field: 'configKey',
            title: '字典配置',
            minWidth:100
        },{
            field: 'code',
            title: '国标编码',
            minWidth:100
        },{
            field: 'value',
            title: '字典项值',
            minWidth:100
        },{
            field: 'orderNo',
            title: '排序',
            width:80
        },{
            field: 'note',
            title: '备注',
            width:120
        },{
            field: 'locked',
            title: '是否锁定',
            width:90,
            templet: function(d){
                return CODE_MAP.DICT.LOCKED[d.locked?1:0];
            }
        },{
            fixed: 'right',
            title:'操作',
            toolbar: '#row-toolbar',
            width: 250
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
            dialogAlert("只能选择一个字典根节点！","warn");
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
                    title: '新增数据字典',
                    content: '/sys/dict/add?parentId='+parentId,
                    maxmin: true,
                    area: ['350px', '550px'],
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
                    url: '/sys/dict/remove',
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
                    title: '编辑数据字典',
                    content: '/sys/dict/add?id='+data.id+'&parentId='+parentId,
                    maxmin: true,
                    area: ['350px', '550px'],
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
                    title: '新增子级字典',
                    content: '/sys/dict/add?parentId='+data.id,
                    maxmin: true,
                    area: ['350px', '550px'],
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
                    url: '/sys/dict/remove',
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

    $(function () {
        $("#treePanel").css('height', $(window).height()-110);
        $(window).resize(function() {
            $("#treePanel").css('height', $(window).height()-110);
        });
        zTreeInit();
    });

</script>

</body>
</html>