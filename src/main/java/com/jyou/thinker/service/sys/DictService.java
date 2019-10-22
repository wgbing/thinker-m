package com.jyou.thinker.service.sys;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Dictionary;
import com.jyou.thinker.mapper.DictMapper;
import com.jyou.thinker.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: 数据字典
 * @author wgbing
 * @date 2019/4/20 11:10
 */
@Slf4j
@Service
@Transactional
public class DictService {

    private static DictService dictService;

    @Autowired
    private DictMapper dictMapper;

    @PostConstruct
    public void init(){
        dictService = this;
        dictService.dictMapper = this.dictMapper;
    }


    public List<Dictionary> listDictByParentId(Long parentId) {
        List<Dictionary>  list = dictMapper.listDictByParentId(parentId);
        if(list != null && !list.isEmpty()){
            for(Dictionary dict:list){
                dict.checkParent();
            }
        }
        return list;
    }

    public DataTableOutput listDict(SearchParams params) {
        PageHelper.startPage(params.getPage(),params.getLimit());
        List<Dictionary> list = dictMapper.listDict(params);

        return new DataTableOutput(list);
    }

    public Dictionary getDictById(Long id) {
        Dictionary dict = new Dictionary();
        if(id != null){
            dict = dictMapper.selectByPrimaryKey(id);
        }
        return dict;
    }

    public ApiResult save(Dictionary dictionary) {
        int count = 0;
        int countByConfigKey = 0;
        Date currDate = new Date();
        if(dictionary.getParentId() != null && dictionary.getParentId() == 0){
            dictionary.setParentId(null);
        }
        if(dictionary.getLocked() == null){
            dictionary.setLocked(false);
        }
        if(!StringUtils.isEmpty(dictionary.getConfigKey())){
            countByConfigKey = dictMapper.countDictByConfigKey(dictionary.getConfigKey());
        }
        if(dictionary.getId() != null){
            Dictionary oldDict = dictMapper.selectByPrimaryKey(dictionary.getId());
            if(!oldDict.getConfigKey().equals(dictionary.getConfigKey())){
                if(countByConfigKey >= 1){
                    return ApiResult.failure("字典配置唯一，不能有重复！");
                }
            }
            dictionary.setUpdateTime(currDate);
            count = dictMapper.updateByPrimaryKeySelective(dictionary);
        }else {
            if(countByConfigKey >= 1){
                return ApiResult.failure("字典配置唯一，不能有重复！");
            }
            dictionary.setCreateTime(currDate);
            count = dictMapper.insert(dictionary);
        }
        return CommonUtils.msg(count);
    }

    public ApiResult batchRemoveDict(Long[] ids) {
        boolean children = false;
        boolean locked = false;
        for(Long id : ids) {
            int count = dictMapper.countDictByParentId(id);
            if(CommonUtils.isIntThanZero(count)) {
                children = true;
            }
            Dictionary dict = dictMapper.selectByPrimaryKey(id);
            if(dict.getLocked() != null && dict.getLocked()){
                locked = true;
            }
        }
        if(children) {
            return ApiResult.failure("操作失败，当前所选数据有子节点数据！");
        }
        if(locked){
            return ApiResult.failure("操作失败，有被锁定数据，不能删除！");
        }
        int count = dictMapper.batchRemove(ids);
        return CommonUtils.msg(ids, count);
    }

    public List<Dictionary> listDictByConfigKey(String configKey) {
        List<Dictionary> list = new ArrayList<>();
        if(StrUtil.isNotEmpty(configKey)){
            list = dictMapper.listDictByConfigKey(configKey);
        }
        return list;
    }

    public String getDictByConfigKey(String configKey){
        List<Dictionary> list = new ArrayList<>();
        if(StrUtil.isNotEmpty(configKey)){
            list = dictService.dictMapper.listDictByConfigKey(configKey);
        }
        return JSON.toJSONString(list);
    }

}
