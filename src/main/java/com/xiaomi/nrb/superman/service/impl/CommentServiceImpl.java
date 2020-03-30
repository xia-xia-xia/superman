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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        List<Comment> commentList=null;
        int commentTotal=0;
        ListCommentQuaryParam quaryParam = new ListCommentQuaryParam();
        quaryParam.setPageNo((request.getPageNo() - 1) * request.getPageSize());
        quaryParam.setPageSize(request.getPageSize());
        quaryParam.setPlanId(request.getPlanId());
        commentList = commentMapper.listBySelective(quaryParam);
        commentTotal = commentMapper.countBySelective(quaryParam);

        List<CommentListInfo>  commentListInfos = new ArrayList<>();
        commentList.forEach(k -> {
            User user = userService.getUserByUserId(k.getUid());
            CommentListInfo commentListInfo = new CommentListInfo();
            commentListInfo.setComment(k.getComment());
            commentListInfo.setAvartarUrl(user.getAvartarUrl());
            commentListInfo.setNickName(user.getNickName());
            commentListInfo.setCommentCtime(simpleDateFormat.format(k.getCommentCtime()));
            Plan plan =planMapper.selectByPrimaryKey(request.getPlanId());
            if (k.getToUid()==plan.getUserId()){
                commentListInfo.setToUid(k.getToUid());
                commentListInfo.setReplyId(k.getReplyId());
            }
            commentListInfos.add(commentListInfo);
            });
        return PageInfo.<CommentListInfo>builder().list(commentListInfos).total(commentTotal).build();
    }
}
