package com.jyou.thinker.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * TODO: 继承自己的MyMapper
 * @author wgbing
 * @date 2019-08-29 22:19
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    /**
     * 批量删除
     * @param id
     * @return
     */
    int batchRemove(Object[] id);
}