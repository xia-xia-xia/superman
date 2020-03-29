package com.xiaomi.nrb.superman.service;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.domain.Comment;

public interface CommentService {
    /**
     * 添加评论
     */
    Comment addComment(Comment comment);


    /**
     * 评论列表
     */
    //PageInfo<CommentListInfo> listComment(ListCommentReq request);
}
