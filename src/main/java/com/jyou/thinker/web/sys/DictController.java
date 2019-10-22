package com.jyou.thinker.web.sys;

import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Dictionary;
import com.jyou.thinker.service.sys.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TODO: 数据字典
 * @author wgbing
 * @date 2019/4/16 16:58
 */
@Slf4j
@Controller
@RequestMapping("/sys/dict")
public class DictController {
    private static final String TPL_PREFIX = "/sys/dict";

    @Autowired
    private DictService dictService;

    /**
     * TODO: 数据字典页面
     * @author wgbing
     * @date 2019/4/16 16:59
     */
    @GetMapping("/index")
    public String dict() {
        return TPL_PREFIX + "/dict";
    }


    /**
     * 根据父级Id查询子节点，树形目录
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree")
    public List<Dictionary> tree(@RequestParam(name = "id") Long parentId) {
        return dictService.listDictByParentId(parentId);
    }

    /**
     * TODO: 获取分页数据
     * @author wgbing
     * @date 2019/4/17 10:48
     * @param params
     * @return 
     * @throws 
     */
    @ResponseBody
    @RequestMapping("/list")
    public DataTableOutput list(SearchParams params) {
        return dictService.listDict(params);
    }

    /**
     * TODO: 新增或编辑页面
     * @author wgbing
     * @date 2019/4/17 15:51
     * @param id 数据字典Id
     * @param parentId 数据字典的父级区域Id
     * @return 
     * @throws 
     */
    @GetMapping("/add")
    public String add(Long id, Long parentId, Map<String,Object> map) {
        Dictionary dictionary = new Dictionary();
        Dictionary parentDict = new Dictionary();
        if(id != null){
            dictionary = dictService.getDictById(id);
        }
        if(parentId != null){
            parentDict = dictService.getDictById(parentId);
        }
        map.put("dict",dictionary);
        map.put("parentDict",parentDict);
        return TPL_PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    public ApiResult save(@RequestBody Dictionary dictionary) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = dictService.save(dictionary);
        } catch (Exception e) {
            log.error("保存数据字典失败，{}", e.getMessage());
        }
        return apiResult;
    }

    @ResponseBody
    @RequestMapping("/remove")
    public ApiResult remove(@RequestBody Long[] ids) {
        ApiResult apiResult = ApiResult.failure();
        try {
            apiResult = dictService.batchRemoveDict(ids);
        } catch (Exception e) {
            log.error("删除数据字典失败，{}", e.getMessage());
        }
        return apiResult;
    }

    /**
     * 根据父级Id查询子节点，树形目录
     * @return
     */
    @ResponseBody
    @RequestMapping("/listDictByConfigKey")
    public List<Dictionary> listDictByConfigKey(@RequestParam(name = "configKey") String configKey) {
        return dictService.listDictByConfigKey(configKey);
    }
}