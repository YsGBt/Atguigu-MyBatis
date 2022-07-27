package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import org.apache.ibatis.annotations.Param;

public interface CacheMapper {

  // 根据eid查询员工信息，测试缓存
  Employee getEmployeeByEid(@Param("eid") Integer eid);

  // 添加员工信息
  void insertEmployee(@Param("employee") Employee employee);
}
