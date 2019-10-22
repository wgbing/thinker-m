package com.jyou.thinker.web.sys;

import cn.hutool.core.collection.CollUtil;
import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Org;
import com.jyou.thinker.domain.Role;
import com.jyou.thinker.domain.User;
import com.jyou.thinker.service.sys.OrgService;
import com.jyou.thinker.service.sys.RoleService;
import com.jyou.thinker.service.sys.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/sys/user")
public class UserController {
    public static final String TPL_PREFIX = "/sys/user";

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrgService orgService;

    @GetMapping("/index")
    public String index() {
        return TPL_PREFIX + "/user";
    }

    // 获取分页查询数据
    @ResponseBody
    @RequestMapping("/list")
    public DataTableOutput list(SearchParams params) {
        return this.userService.findByPaging(params);
    }

    @GetMapping("/add")
    public String add(Map<String,Object> map, Long id, Long orgId) {
        if (id != null) {
            // 获取当前用户信息
            User currUser = this.userService.getUserMapper().selectByPrimaryKey(id);
            orgId = currUser.getOrgId();
            map.put("user", currUser);
        }
        if (orgId != null) {
            Org currOrg = orgService.getOrgById(orgId);
            map.put("org", currOrg);
        }
        // 获取所有角色，并标记当前用户所用有权限选中状态
        List<Role> roleList = this.roleService.findAllWithMarkChecked(id);
        map.put("roleList", roleList);
        return TPL_PREFIX + "/add";
    }

    @GetMapping("/detail")
    public String detail(Map<String,Object> map, Long id) {
        User user = this.userService.getUserMapper().selectByPrimaryKey(id);
        Org org = orgService.getOrgById(user.getOrgId());
        String roleStr = "无";
        List<Role> roleList = roleService.getRoleMapper().listByUserId(id);
        if (roleList != null && roleList.size() > 0) {
            List<String> nameList = new ArrayList<>();
            roleList.forEach(role -> nameList.add(role.getName()));
            roleStr = CollUtil.join(nameList, "，");
        }

        map.put("user", user);
        map.put("org", org);
        map.put("roleStr", roleStr);
        return TPL_PREFIX + "/detail";
    }

    @ResponseBody
    @PostMapping("/save")
    public ApiResult save(@RequestBody User user) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = this.userService.save(user);
        } catch (Exception e) {
            log.error("保存用户失败，{}", e.getMessage());
        }
        return apiResult;
    }
}
