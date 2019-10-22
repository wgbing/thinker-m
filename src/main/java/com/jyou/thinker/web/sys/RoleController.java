package com.jyou.thinker.web.sys;

import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Role;
import com.jyou.thinker.service.sys.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TODO: 角色管理
 * @author wgbing
 * @date 2019-10-06 19:43
 */
@Slf4j
@Controller
@RequestMapping("/sys/role")
public class RoleController {
    private static final String TPL_PREFIX = "/sys/role";

    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public String index() {
        return TPL_PREFIX + "/role";
    }

    /**
     * @Description 获取分页数据
     * @author wgbing
     */
    @ResponseBody
    @RequestMapping("/list")
    public DataTableOutput list(SearchParams params) {
        return this.roleService.findByPaging(params);
    }

    @GetMapping("/add")
    public String add(Map<String,Object> map, Long id) {
        if (id != null) {
            map.put("role", this.roleService.getRoleMapper().selectByPrimaryKey(id));
        }
        return TPL_PREFIX + "/add";
    }

    @GetMapping("/detail")
    public String detail(Map<String,Object> map, Long id) {
        if (id != null) {
            map.put("role", this.roleService.getRoleMapper().selectByPrimaryKey(id));
        }
        return TPL_PREFIX + "/detail";
    }

    @ResponseBody
    @PostMapping("/save")
    public ApiResult save(@RequestBody Role role) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = this.roleService.save(role);
        } catch (Exception e) {
            log.error("保存角色失败，{}", e.getMessage());
        }
        return apiResult;
    }

    @ResponseBody
    @RequestMapping("/remove")
    public Object remove(@RequestBody Long[] ids) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = this.roleService.batchRemove(ids);
        } catch (Exception e) {
            log.error("删除角色失败，{}", e.getMessage());
        }
        return apiResult;
    }

}