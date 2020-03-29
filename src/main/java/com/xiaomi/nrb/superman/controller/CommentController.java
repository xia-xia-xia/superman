package com.xiaomi.nrb.superman.controller;

import com.xiaomi.nrb.superman.annotation.CheckLogin;
import com.xiaomi.nrb.superman.common.ApiEnum;
import com.xiaomi.nrb.superman.common.Result;
import com.xiaomi.nrb.superman.domain.Comment;
import com.xiaomi.nrb.superman.request.AddCommentReq;
import com.xiaomi.nrb.superman.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/superman/comment/talk")
public class CommentController {
    @Resource
    private CommentService commentService;

    @RequestMapping("/addComment")
    @CheckLogin
    public Result addComment(@RequestBody AddCommentReq request){
        try {
            Comment comment = new Comment();
            return Result.ok(commentService.addComment(comment));
        } catch (Exception e) {
            log.error("CommentController.addComment.error:", e);
            return Result.fail(ApiEnum.ERROR.getCode());
        }

    }
}
