package com.jyou.thinker.common.param;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jyou.thinker.common.datatable.DataTableInput;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: 查询条件参数封装
 * @author wgbing
 * @date 2019-09-29 8:14
 */
@Data
public class SearchParams extends DataTableInput {

    // 原始参数（JSON字符串）
    private String raw = "";

    // Map参数
    private Map<String, String> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void setRaw(String raw) {
        try {
            if (StrUtil.isNotEmpty(raw)) {
                map.putAll(JSONUtil.toBean(raw, Map.class));
            }
        } catch (Exception ignored) { }
        this.raw = raw;
    }
}