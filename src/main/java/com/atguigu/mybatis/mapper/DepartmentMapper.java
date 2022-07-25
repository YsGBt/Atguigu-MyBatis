package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Department;
import org.apache.ibatis.annotations.Param;

public interface DepartmentMapper {

  // 通过分步查询 查询员工以及员工所对应的部门信息
  // 分步查询第二步 根据did查询员工所对应的部门
  Department getEmployeeWithDepartmentByStepTwo(@Param("did") Integer did);
}
