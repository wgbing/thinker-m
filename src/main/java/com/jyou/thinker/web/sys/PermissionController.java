package com.jyou.thinker.web.sys;

import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.service.sys.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TODO: 权限控制
 * @author wgbing
 * @date 2019-10-09 15:09
 */
@Slf4j
@Controller
@RequestMapping("/sys/permission")
public class PermissionController {
    private static final String TPL_PREFIX = "/sys/permission";


    @Autowired
    private PermissionService permissionService;

    /**
     * TODO: 权限管理页面
     * @author wgbing
     * @date 2019/4/16 16:59
     */
    @GetMapping("/index")
    public String index() {
        return TPL_PREFIX + "/index";
    }

    /**
     * 根据父级Id查询子节点，树形目录
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree")
    public List<Permission> tree(@RequestParam(name = "id") Long parentId) {
        return permissionService.listByParentId(parentId);
    }

    /**
     * TODO: 获取分页数据
     * @author wgbing
     * @date 2019/4/17 10:48
     * @param params
     */
    @ResponseBody
    @RequestMapping("/list")
    public DataTableOutput list(SearchParams params) {
        return permissionService.list(params);
    }

    /**
     * TODO: 新增或编辑页面
     * @author wgbing
     * @date 2019/4/17 15:51
     * @param id 数据字典Id
     * @param parentId 数据字典的父级区域Id
     */
    @GetMapping("/add")
    public String add(Long id, Long parentId, Map<String,Object> map) {
        Permission permission = new Permission();
        Permission parentPerm = new Permission();
        if(id != null){
            permission = permissionService.getById(id);
        }
        if(parentId != null){
            parentPerm = permissionService.getById(parentId);
        }
        map.put("perm",permission);
        map.put("parentPerm",parentPerm);
        return TPL_PREFIX + "/add";
    }

    /**
     * @Title 菜单图标
     * @author wgbing
     */
    @GetMapping("/icon")
    public String icon(){
        return TPL_PREFIX + "/icon";
    }

    @ResponseBody
    @PostMapping("/save")
    public ApiResult save(@RequestBody Permission permission) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = permissionService.save(permission);
        } catch (Exception e) {
            log.error("保存权限失败，{}", e.getMessage());
        }
        return apiResult;
    }

    @ResponseBody
    @RequestMapping("/remove")
    public ApiResult remove(@RequestBody Long[] ids) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = permissionService.batchRemove(ids);
        } catch (Exception e) {
            log.error("删除权限失败，{}", e.getMessage());
        }
        return apiResult;
    }

    /**
     * 获取权限节点
     * @param roleId 角色ID
     * @return
     */
    @ResponseBody
    @GetMapping("/getTreeNodes")
    public Object getTreeNodes(Long roleId) {
        return this.permissionService.getTreeNodesHandler(roleId);
    }

}
