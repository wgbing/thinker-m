package com.jyou.thinker.common.datatable;

import lombok.Data;
import tk.mybatis.mapper.util.StringUtil;

/**
 * TODO: 表格查询输入参数
 * @author wgbing
 * @date 2019-09-27 16:46
 */
@Data
public class DataTableInput {

    // 页码
    private Integer page = 1;

    // 数量
    private Integer limit = 10;

    // 排序列
    private String field;

    // 排序类型
    private String type;

    // 排序列field配置一半对应Java类中的驼峰风格属性，将驼峰风格替换为下划线风格
    public void setField(String field) {
        if (StringUtil.isNotEmpty(field)) {
            field = StringUtil.camelhumpToUnderline(field);
        }
        this.field = field;
    }

}
