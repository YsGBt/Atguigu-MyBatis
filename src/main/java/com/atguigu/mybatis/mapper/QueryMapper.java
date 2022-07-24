package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface QueryMapper {

  // 根据id查询用户信息
  User getUserById(@Param("id") Integer id);

  // 查询所有的用户信息
  User[] getAllUser();
}
