package com.june.project.community.mapper;

import com.june.project.community.model.Question;
import com.june.project.community.model.QuestionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionExtMapper {
    int incView(Question record);
}