package com.xiaomi.nrb.superman.service.impl;

import com.xiaomi.nrb.superman.dao.CommentMapper;
import com.xiaomi.nrb.superman.dao.PlanMapper;
import com.xiaomi.nrb.superman.dao.RelationMapper;
import com.xiaomi.nrb.superman.dao.UserMapper;
import com.xiaomi.nrb.superman.domain.Comment;
import com.xiaomi.nrb.superman.service.CommentService;
import com.xiaomi.nrb.superman.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private PlanMapper planMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Comment addComment(Comment comment) {
        commentMapper.insertSelective(comment);
        return null;
    }
}
