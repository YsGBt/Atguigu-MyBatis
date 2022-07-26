package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DynamicSQLMapper {

  // 多条件查询
  List<Employee> getEmployeeByCondition(@Param("employee") Employee employee);

  // 测试choose, when, otherwise
  List<Employee> getEmployeeByChoose(@Param("employee") Employee employee);

  // 通过数组实现批量删除
  int deleteEmployeeBatchByArray(@Param("eids") Integer[] eids);

  // 通过List集合实现批量添加
  int insertEmployeeBatchByList(@Param("emps") List<Employee> employeeList);
}
