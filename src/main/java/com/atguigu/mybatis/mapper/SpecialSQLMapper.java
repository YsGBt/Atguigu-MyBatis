package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpecialSQLMapper {

  // 根据用户名模糊查询用户信息
  List<User> getUserByFuzzyName(@Param("username") String username);

  // 批量删除
  int deleteBatchUser(@Param("ids") String ids);

  // 查询指定表中的数据
  List<User> getUserByTableName(@Param("tableName") String tableName);

  // 添加用户信息
  void createUser(@Param("user") User user);
}
