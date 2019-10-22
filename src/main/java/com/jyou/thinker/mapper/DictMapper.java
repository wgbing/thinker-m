package com.jyou.thinker.mapper;


import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Dictionary;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface DictMapper extends MyMapper<Dictionary> {

    List<Dictionary> listDictByParentId(@Param("parentId") Long parentId);

    List<Dictionary> listDict(SearchParams params);

    int countDictByParentId(@Param("parentId") Long parentId);

    @Select(value = "SELECT COUNT(*) FROM sys_dictionary a WHERE a.config_key = #{configKey}")
    int countDictByConfigKey(String configKey);

    List<Dictionary> listDictByConfigKey(@Param("configKey") String configKey);
}