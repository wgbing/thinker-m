package com.jyou.thinker.web.sys;

import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Org;
import com.jyou.thinker.service.sys.DictService;
import com.jyou.thinker.service.sys.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/sys/org")
public class OrgController {
    public static final String TPL_PREFIX = "/sys/org";

    @Autowired
    private OrgService orgService;

    @GetMapping("/index")
    public String index(Map<String,Object> map) {
        map.put("dictService",new DictService());
        return TPL_PREFIX + "/org";
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
        return orgService.list(params);
    }

    /**
     * TODO: 新增或编辑页面
     * @author wgbing
     * @date 2019/4/17 15:51
     * @param id 组织部门Id
     * @param parentId 组织部门的父级区域Id
     * @return
     * @throws
     */
    @GetMapping("/add")
    public String add(Long id, Long parentId, Map<String,Object> map) {
        Org org = new Org();
        Org parentOrg = new Org();
        if(id != null){
            org = orgService.getOrgById(id);
        }
        if(parentId != null){
            parentOrg = orgService.getOrgById(parentId);
        }
        map.put("org",org);
        map.put("parentOrg",parentOrg);
        return TPL_PREFIX + "/add";
    }

    @ResponseBody
    @RequestMapping("/save")
    public ApiResult save(@RequestBody Org org) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = orgService.save(org);
        } catch (Exception e) {
            log.error("保存组织失败，{}", e.getMessage());
        }
        return apiResult;
    }

    @ResponseBody
    @RequestMapping("/remove")
    public ApiResult remove(@RequestBody Long[] ids) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = orgService.batchRemoveOrg(ids);
        } catch (Exception e) {
            log.error("删除组织部门失败，{}", e.getMessage());
        }
        return apiResult;
    }

    /**
     * 根据父级Id查询子节点，异步加载树形目录
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree")
    public List<Org> tree(@RequestParam(name = "id") Long parentId) {
        return orgService.listOrgByParentId(parentId);
    }

}
