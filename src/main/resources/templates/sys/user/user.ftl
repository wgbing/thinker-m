<!DOCTYPE html>
<#assign security=JspTaglibs["/META-INF/security.tld"] />
<html>
<head>
    <title></title>
    <#include "../../include/header.ftl">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-sm4 layui-col-md2">
            <div class="layui-card">
                <div class="layui-card-header">组织机构</div>
                <div class="layui-card-body" id="treePanel" style="overflow: auto;">
                    <ul id="orgTree" class="ztree"></ul>
                </div>
            </div>
        </div>
        <div class="layui-col-sm8 layui-col-md10">
            <div class="layui-card">
                <!-- 查询条件 -->
                <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                    <div class="layui-form-item" id="search-form">
                        <div class="layui-inline">
                            <label class="layui-form-label">姓名</label>
                            <div class="layui-input-block">
                                <input type="text" name="nickName" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <button id="btn-search" class="layui-btn" lay-submit lay-filter="search">
                                <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                            </button>
                            <button id="btn-reset" class="layui-btn" lay-submit lay-filter="reset">
                                <i class="layui-icon layui-icon-refresh layuiadmin-button-btn"></i>
                            </button>
                        </div>
                    </div>
                </div>

                <!-- 数据表格 -->
                <div class="layui-card-body">
                    <table id="dataGrid" lay-filter="dataGrid"></table>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- 工具条模板 -->
<script type="text/html" id="toolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>添加</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete"><i class="layui-icon layui-icon-delete"></i>批量删除</button>
    </div>
</script>

<!-- 操作列模板 -->
<script type="text/html" id="row-toolbar">
    <a class="layui-btn layui-btn-xs" lay-event="detail"><i class="layui-icon layui-icon-about"></i>查看</a>
    <a class="layui-btn layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
    <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
</script>

<!-- Scripts -->
<#include "../../include/footer.ftl">
<script>
    var layer = layui.layer;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;
    var ztree;
    var setting = {
        async : {
            enable: true,
            type: "get",
            url: "/sys/org/tree",
            autoParam: ["id"]
        },
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "parentId"
            },
            key : {
                url : "nourl",
                name: "shortName"
            }
        },
        callback : {
            onClick : function(event, treeId, treeNode) {
                var orgId = treeNode.id;
                //执行重载
                table.reload("dataGrid", {
                    page: {curr: 1},
                    where: {
                        "map[orgId]": orgId
                    }
                });
            }
        }
    };

    function zTreeInit(){
        //初始化树
        //初始化组织树(异步加载)
        $.get('/sys/org/tree', {id: null},function(r) {
            ztree = $.fn.zTree.init($("#orgTree"), setting, r);
            /* 获取所有树节点 */
            var nodes = ztree.transformToArray(ztree.getNodes());
            //展开第一级树
            ztree.expandNode(nodes[0], true);
        });
    }


    //获取一个组织
    function getOrg() {
        var nodes = ztree.getSelectedNodes();
        if(nodes.length == 0){
            dialogAlert("请选择组织！","warn");
        }else if(nodes.length > 1){
            dialogAlert("只能选择一个组织！","warn");
        }
        return nodes[0];
    }

    //获取多个组织
    function listOrg() {
        var nodes = ztree.getSelectedNodes();
        if(nodes.length == 0){
            dialogAlert("请选择组织！","warn");
        }
        return nodes;
    }

    $(function () {
        $("#treePanel").css('height', $(window).height()-110);
        $(window).resize(function() {
            $("#treePanel").css('height', $(window).height()-110);
        });
        zTreeInit();


        // 初始化时间选择
        laydate.render({ elem: "input[name='startTime']", calendar: true });
        laydate.render({ elem: "input[name='endTime']", calendar: true });

        // 初始化表格
        var options = {
            elem: '#dataGrid',
            toolbar: '#toolbar',
            url: '/sys/user/list',
            height: 'full-150',
            cellMinWidth: 80,
            page: true,
            limit: 10,
            autoSort: false,
            initSort: {
                field: 'id',
                type: 'desc'
            },
            where: {
                field: 'id',
                type: 'desc'
            },
            cols: [[{
                fixed: 'left',
                type:'numbers'
            },{
                fixed: 'left',
                type:'checkbox'
            },{
                field:'id',
                title: 'ID',
                width: 80,
                sort: true
            },{
                field:'userName',
                title: '用户名',
                sort: true
            },{
                field:'nickName',
                title: '姓名',
                sort: true
            },{
                field:'sex',
                title: '性别',
                width: 80,
                sort: true,
                templet: function (row) {
                    return CODE_MAP.GENERAL.SEX[row.sex];
                }
            },{
                field:'lastLoginIp',
                title: '上次登录IP',
                width: 120
            },{
                field:'lastLoginTime',
                title: '上次登录时间',
                sort: true
            },{
                field:'enable',
                title: '状态',
                sort: true,
                width: 80,
                templet: function (row) {
                    var clazz = row.enable ? "layui-bg-blue" : "layui-bg-orange";
                    var value = CODE_MAP.GENERAL.ENABLE[row.enable?1:0];
                    return "<span class='layui-badge {0}'>{1}</span>".format(clazz, value);
                }
            },{
                field:'createTime',
                title: '创建时间',
                sort: true
            },{
                fixed: 'right',
                title:'操作',
                toolbar: '#row-toolbar',
                width: 210
            }]]
        };
        table.render(options);

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

        // 监听排序事件
        table.on('sort(dataGrid)', function(obj){
            table.reload('dataGrid', { initSort: obj ,where: { field: obj.field, type: obj.type } });
        });

        // 监听顶部工具栏
        table.on('toolbar(dataGrid)', function(obj){
            switch(obj.event){
                // 添加
                case 'add':
                    var snode = getOrg();
                    if (!snode) {
                        return;
                    }

                    layer.open({
                        type: 2
                        ,title: '添加用户'
                        ,content: '/sys/user/add?orgId={0}'.format(snode.id)
                        ,maxmin: true
                        ,shadeClose: true
                        ,area: ['420px', '520px']
                        ,btn: ['确定', '取消']
                        ,yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                // 批量删除
                case 'delete':
                    // 校验选中状态
                    var data = table.checkStatus(obj.config.id).data;
                    if (data.length === 0) {
                        layer.msg("请选择要删除的数据项");
                        return;
                    }

                    // 获取选中行ID数组
                    var ids = [];
                    for (var i = 0; i < data.length; i++) {
                        ids.push(data[i].id);
                    }

                    var ids = [];
                    $.each(data, function(idx, item){
                        ids[idx] = item.id;
                    });
                    $.RemoveForm({
                        url: '/sys/user/remove',
                        param: ids,
                        headers:{
                            'X-CSRF-TOKEN':"${_csrf.token?default('')}"
                        },
                        success: function(data) {
                            if (data.code == 0) {
                                table.reload("dataGrid");
                            }
                        }
                    });
                    break;
            }
        });

        // 监听工具条
        table.on('tool(dataGrid)', function(obj){
            var data = obj.data;        // 获得当前行数据

            switch(obj.event){
                // 查看
                case 'detail':
                    layer.open({
                        type: 2
                        ,title: '查看用户'
                        ,content: '/sys/user/detail?id={0}'.format(data.id)
                        ,maxmin: true
                        ,shadeClose: true
                        ,area: ['420px', '520px']
                    });
                    break;
                // 编辑
                case 'edit':
                    layer.open({
                        type: 2
                        ,title: '编辑用户'
                        ,content: '/sys/user/add?id={0}'.format(data.id)
                        ,maxmin: true
                        ,shadeClose: true
                        ,area: ['420px', '520px']
                        ,btn: ['确定', '取消']
                        ,yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                // 删除
                case 'del':
                    var ids = [];
                    ids.push(data.id);
                    $.RemoveForm({
                        url: '/sys/user/remove',
                        param: ids,
                        headers:{
                            'X-CSRF-TOKEN':"${_csrf.token?default('')}"
                        },
                        success: function(data) {
                            if (data.code == 0) {
                                table.reload("dataGrid");
                            }
                        }
                    });
                    break;
            }
        });
    });

</script>
</body>
</html>