package com.xiaomi.nrb.superman.dao.quary;

import com.xiaomi.nrb.superman.domain.BaseQuaryParam;
import lombok.Data;

import java.util.List;

@Data
public class ListCommentQuaryParam extends BaseQuaryParam {
    /**
     * 评论的感悟id
     */
    private Long planId;
    /**
     * 评论ids
     */
    private List<Long> commentIds;
}
