package com.xiaomi.nrb.superman.dao.quary;

import com.xiaomi.nrb.superman.domain.BaseQuaryParam;
import lombok.Data;

@Data
public class ListRelationQuaryParam extends BaseQuaryParam {
    /**
     * 用户id
     */
    private Long userId;
}
