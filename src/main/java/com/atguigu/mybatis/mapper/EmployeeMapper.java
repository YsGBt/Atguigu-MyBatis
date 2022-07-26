package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {
  // 查询所有的员工信息
  List<Employee> getAllEmployee();

  // 查询员工以及员工所对应的部门信息
  Employee getEmployeeWithDepartmentById(@Param("eid") Integer eid);

  // 通过分步查询 查询员工以及员工所对应的部门信息
  // 分步查询第一步 查询员工信息
  Employee getEmployeeWithDepartmentByStepOne(@Param("eid") Integer eid);

  // 通过分步查询 查询部门以及部门所有员工的信息
  // 分步查询第二步 根据部门查询所用员工信息
  List<Employee> getDepartmentAndEmployeeByStepTwo(@Param("did") Integer did);
}
