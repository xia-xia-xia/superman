package com.xiaomi.nrb.superman.request;

import lombok.Data;

@Data
public class AddCommentReq extends BaseRequest{
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 回复的评论id
     */
    private Long replyId;

}
