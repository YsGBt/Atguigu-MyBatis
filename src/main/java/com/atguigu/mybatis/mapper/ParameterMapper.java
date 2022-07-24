package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ParameterMapper {

  // 查询所有的员工信息
  List<User> getAllUser();

  // 根据用户名查询用户信息
  User getUserByUsername(String username);

  // 验证登陆
  User checkLogin(String username, String password);

  // 验证登陆 (参数为map)
  User checkLoginByMap(Map<String, Object> map);

  // 验证登陆 (使用@Param)
  User checkLoginByParam(@Param("username") String username, @Param("password") String password);

  // 添加用户信息
  int createUser(User user);
}
