package com.xiaomi.nrb.superman.dao;

import com.xiaomi.nrb.superman.dao.quary.ListCommentQuaryParam;
import com.xiaomi.nrb.superman.domain.Comment;
import com.xiaomi.nrb.superman.domain.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CommentMapper {
    int insertSelective(Comment record);

    Plan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    List<Comment> listBySelective(ListCommentQuaryParam quaryParam);

    int countBySelective(ListCommentQuaryParam quaryParam);
}
