package com.june.project.community.mapper;

import com.june.project.community.model.Comment;
import com.june.project.community.model.CommentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}