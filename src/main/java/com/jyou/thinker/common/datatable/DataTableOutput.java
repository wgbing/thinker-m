package com.jyou.thinker.common.datatable;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;


/**
 * TODO: 表格查询输出参数
 * @author wgbing
 * @date 2019-09-27 16:46
 */
@Data
public class DataTableOutput {
    /** 状态码 默认0 */
    private Integer code;

    /** 附加信息 */
    private String msg;

    /** 总记录数 */
    private Long count;

    /** 结果集 */
    private List data;

    public DataTableOutput() {
    }

    public DataTableOutput(Integer code, String msg, Long count, List data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public DataTableOutput(List list) {
        PageInfo pageInfo = new PageInfo(list);
        this.code = 0;
        this.msg = "";
        this.count = pageInfo.getTotal();
        this.data = pageInfo.getList();
    }

    public DataTableOutput(PageInfo pageInfo) {
        this.code = 0;
        this.msg = "";
        this.count = pageInfo.getTotal();
        this.data = pageInfo.getList();
    }

    public DataTableOutput(Long count, List data) {
        this.code = 0;
        this.msg = "";
        this.count = count;
        this.data = data;
    }

}
