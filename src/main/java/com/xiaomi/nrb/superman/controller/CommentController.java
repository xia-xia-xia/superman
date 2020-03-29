package com.xiaomi.nrb.superman.controller;

import com.xiaomi.nrb.superman.annotation.CheckLogin;
import com.xiaomi.nrb.superman.common.ApiEnum;
import com.xiaomi.nrb.superman.common.Result;
import com.xiaomi.nrb.superman.dao.CommentMapper;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.domain.Comment;
import com.xiaomi.nrb.superman.domain.Plan;
import com.xiaomi.nrb.superman.request.AddCommentReq;
import com.xiaomi.nrb.superman.request.ListCommentReq;
import com.xiaomi.nrb.superman.request.ListPlanReq;
import com.xiaomi.nrb.superman.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/superman/comment/talk")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private PlanMapper planMapper;
    @Resource
    private CommentMapper commentMapper;

    @RequestMapping("/addComment")
    @CheckLogin
    public Result addComment(@RequestBody AddCommentReq request){
        try {
            Comment comment = new Comment();
            comment.setComment(request.getComment());
            comment.setPlanId(request.getPlanId());
            comment.setUid(request.getUserId());
            comment.setCommentCtime(new Date());
            Plan plan =planMapper.selectByPrimaryKey(request.getPlanId());
            if (null == plan){
                //错误提示
                return Result.fail(ApiEnum.ERROR.getCode());
            }
            comment.setToUid(plan.getUserId());
            //如果是回复评论
            if(null!= request.getReplyId()){
                Comment replyComment = commentMapper.selectByPrimaryKey(request.getReplyId());
                if (replyComment==null){
                    return Result.fail(ApiEnum.ERROR.getCode());
                }
                comment.setReplyId(request.getReplyId());
            }
            return Result.ok(commentService.addComment(comment));
        } catch (Exception e) {
            log.error("CommentController.addComment.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }

    }
    @RequestMapping("/listComment")
    @CheckLogin
    public Result listComment(@RequestBody ListCommentReq request) {
        try {
            return Result.ok(commentService.listComment(request));
        } catch (Exception e) {
            log.error("CommentController.listComment.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }
    }
}
