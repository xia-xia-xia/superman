package com.xiaomi.nrb.superman.request;

import lombok.Data;

@Data
public class ListCommentReq extends BaseRequest {
    /**
     * 目标用户id
     */
    private Long toUid;
}
