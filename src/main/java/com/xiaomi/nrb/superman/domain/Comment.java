package com.xiaomi.nrb.superman.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    private Long id;
    /**
     * 评论用户id
     */
    private Long uid;
    /**
     * 回复的评论id
     */
    private Long replyId;
    /**
     * 目标用户id
     */
    private Long toUid;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 评论时间
     */
    private Date commentCtime;
    /**
     * 关系状态，0正常、-1已删除
     */
    private Integer status;
    /**
     * 评论的感悟id
     */
    private Long planId;

}
