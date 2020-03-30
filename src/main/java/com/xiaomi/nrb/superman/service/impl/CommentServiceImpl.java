package com.xiaomi.nrb.superman.service.impl;

import com.xiaomi.nrb.superman.common.PageInfo;
import com.xiaomi.nrb.superman.dao.CommentMapper;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.dao.quary.ListCommentQuaryParam;
import com.xiaomi.nrb.superman.domain.Comment;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.domain.User;
import com.xiaomi.nrb.superman.request.ListCommentReq;
import com.xiaomi.nrb.superman.response.CommentListInfo;
import com.xiaomi.nrb.superman.service.CommentService;
import com.xiaomi.nrb.superman.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private PlanMapper planMapper;

    @Resource
    private UserService userService;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Comment addComment(Comment comment) {
        commentMapper.insertSelective(comment);
        return null;
    }

    //对日期字符串进行解析和格式化输出
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

    @Override
    public PageInfo<CommentListInfo> listComment(ListCommentReq request) {
        List<Comment> commentList = null;
        int commentTotal = 0;
        ListCommentQuaryParam quaryParam = new ListCommentQuaryParam();
        quaryParam.setPageNo((request.getPageNo() - 1) * request.getPageSize());
        quaryParam.setPageSize(request.getPageSize());
        quaryParam.setPlanId(request.getPlanId());
        commentList = commentMapper.listBySelective(quaryParam);
        commentTotal = commentMapper.countBySelective(quaryParam);


        //数据聚合

        //评论数据
        List<CommentListInfo> comments = new ArrayList<>();
        //回复数据
        Map<Long, List<CommentListInfo>> replyMaps = new HashMap<>();

        commentList.forEach(k -> {
            User user = userService.getUserByUserId(k.getUid());
            CommentListInfo commentListInfo = new CommentListInfo();
            commentListInfo.setId(k.getId());
            commentListInfo.setUid(k.getUid());
            commentListInfo.setToUid(k.getToUid());
            commentListInfo.setComment(k.getComment());
            commentListInfo.setAvartarUrl(user.getAvartarUrl());
            commentListInfo.setNickName(user.getNickName());
            commentListInfo.setReplyId(k.getReplyId());
            commentListInfo.setCommentCtime(simpleDateFormat.format(k.getCommentCtime()));
            if (k.getToUid() == k.getUid()) {
                commentListInfo.setAuthor(true);
            }
            //0代表评论
            if (k.getReplyId() == 0) {
                comments.add(commentListInfo);
            } else {
                //代表是回复
                List<CommentListInfo> replyList = replyMaps.get(k.getReplyId());
                if (CollectionUtils.isEmpty(replyList)) replyList = new ArrayList<>();
                replyList.add(commentListInfo);
                replyMaps.put(k.getReplyId(), replyList);
            }

        });

        //评论里面加回复
        comments.forEach(k -> {
            k.setReplyList(replyMaps.get(k.getId()));
        });

        return PageInfo.<CommentListInfo>builder().list(comments).total(commentTotal).build();
    }
}
