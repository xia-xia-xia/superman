package com.xiaomi.nrb.superman.dao.quary;

import com.xiaomi.nrb.superman.domain.BaseQuaryParam;

import java.util.List;

public class ListCommentQuaryParam extends BaseQuaryParam {
    /**
     * 目标用户id
     */
    private Long toUid;
    /**
     * 评论的感悟id
     */
    private Long planId;
    /**
     * 评论ids
     */
    private List<Long> commentIds;
}
