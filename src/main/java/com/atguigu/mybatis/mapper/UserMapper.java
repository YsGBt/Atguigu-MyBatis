package com.atguigu.mybatis.mapper;

public interface UserMapper {

  /**
   * MyBatis 面向接口编程的两个一致:
   * 1. 映射文件的 namespace 要和 mapper 接口的全类名保持一致
   * 2. 映射文件中 sql 语句的 id 要和 mapper 接口中的方法名一致
   */

  // 添加用户信息
  int addUser();
}
