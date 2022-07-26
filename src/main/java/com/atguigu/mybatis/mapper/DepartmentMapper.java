package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Department;
import org.apache.ibatis.annotations.Param;

public interface DepartmentMapper {

  // 通过分步查询 查询员工以及员工所对应的部门信息
  // 分步查询第二步 根据did查询员工所对应的部门
  Department getEmployeeWithDepartmentByStepTwo(@Param("did") Integer did);

  // 获取部门以及部门中所有的员工信息
  Department getDepartmentAndEmployee(@Param("did") Integer did);

  // 通过分步查询 查询部门以及部门中所有员工信息
  // 分步查询第一步 根据did查询部门信息
  Department getDepartmentAndEmployeeByStepOne(@Param("did") Integer did);
}
