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
        <!-- 主体内容 -->
        <div class="layui-col-md12">
            <div class="layui-card">
                <!-- 查询条件 -->
                <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                    <div class="layui-form-item" id="search-form">
                        <div class="layui-inline">
                            <label class="layui-form-label">角色名称</label>
                            <div class="layui-input-block">
                                <input type="text" name="name" placeholder="请输入" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">创建时间起</label>
                            <div class="layui-input-block">
                                <input type="text" name="startTime" class="layui-input" placeholder="yyyy-MM-dd">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">创建时间止</label>
                            <div class="layui-input-block">
                                <input type="text" name="endTime" class="layui-input" placeholder="yyyy-MM-dd">
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

    $(function () {
        // 初始化时间选择
        laydate.render({ elem: "input[name='startTime']", calendar: true });
        laydate.render({ elem: "input[name='endTime']", calendar: true });

        // 初始化表格
        var options = {
            elem: '#dataGrid',
            toolbar: '#toolbar',
            url: '/sys/role/list',
            height: 'full-140',
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
                field: 'id',
                hide:true
            },{
                fixed: 'left',
                type:'numbers'
            },{
                fixed: 'left',
                type:'checkbox'
            },{
                field:'name',
                title: '角色名称',
                sort: true
            },{
                field:'description',
                title: '角色描述',
                sort: true
            },{
                field:'createTime',
                title: '创建时间',
                sort: true
            },{
                fixed: 'right',
                title:'操作',
                toolbar: '#row-toolbar',
                width: 220
            }]]
        };
        table.render(options);

        // 监听排序事件
        table.on('sort(dataGrid)', function(obj){
            table.reload('dataGrid', { initSort: obj ,where: { field: obj.field, type: obj.type } });
        });

        // 监听顶部工具栏
        table.on('toolbar(dataGrid)', function(obj){
            switch(obj.event){
                case 'add':
                    layer.open({
                        type: 2
                        ,title: '添加角色'
                        ,content: '/sys/role/add'
                        ,maxmin: true
                        ,shadeClose: true
                        ,area: ['520px', '470px']
                        ,btn: ['确定', '取消']
                        ,yes: function(index, layero){
                            //点击确认触发 iframe 内容中的按钮提交
                            var submit = layero.find('iframe').contents().find("#add-form-submit");
                            submit.click();
                        }
                    });
                    break;
                case 'delete':
                    // 校验选中状态
                    var data = table.checkStatus(obj.config.id).data;
                    if (data.length === 0) {
                        layer.msg("请选择要删除的数据项");
                        return;
                    }

                    var ids = [];
                    $.each(data, function(idx, item){
                        ids[idx] = item.id;
                    });
                    $.RemoveForm({
                        url: '/sys/role/remove',
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
                case 'detail':
                    layer.open({
                        type: 2
                        ,title: '查看角色'
                        ,content: '/sys/role/detail?id={0}'.format(data.id)
                        ,maxmin: true
                        ,shadeClose: true
                        ,area: ['520px', '470px']
                    });
                    break;
                case 'edit':
                    layer.open({
                        type: 2,
                        title: '编辑角色',
                        content: '/sys/role/add?id={0}'.format(data.id),
                        maxmin: true,
                        shadeClose: true,
                        area: ['520px', '470px'],
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
                        url: '/sys/role/remove',
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

    });

</script>
</body>
</html>