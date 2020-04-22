package com.xiaomi.nrb.superman.response;

import lombok.Data;

import java.util.List;


@Data
public class CommentListInfo {
    /**
     * 评论的感悟id
     */
    private Long planId;
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
    private String commentCtime;
    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String avartarUrl;
    /**
     * 回复列表
     */
    private List<CommentListInfo> replyList;
    /**
     * 是否是作者
     */
    private boolean isAuthor;
}
