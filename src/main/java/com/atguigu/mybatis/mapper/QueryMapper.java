package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

public interface QueryMapper {

  // 根据id查询用户信息
  User getUserById(@Param("id") Integer id);

  // 查询所有的用户信息
  User[] getAllUser();

  // 查询用户信息的总记录数
  Integer getUserCount();

  // 根据id查询用户信息为一个map集合
  Map<String, Object> getMapById(@Param("id") Integer id);

  // 查询所有用户信息为map集合
  List<Map<String, Object>> getUserMap();
  // 或者
  @MapKey("id")
  Map<String, Object> getUserIdMap();

}
