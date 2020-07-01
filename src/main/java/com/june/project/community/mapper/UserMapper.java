package com.june.project.community.mapper;

import com.june.project.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author June
 * @date 2020/6/30 - 22:22
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified) values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);
}
