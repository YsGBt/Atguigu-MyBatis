package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class EmployeeMapperTest {

  @Test
  public void testGetAllEmployee() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
    List<Employee> allEmployee = mapper.getAllEmployee();
    allEmployee.forEach(System.out::println);
  }

  @Test
  public void testGetEmployeeWithDepartmentById() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
    System.out.println(mapper.getEmployeeWithDepartmentById(1));
  }

  @Test
  public void testGetEmployeeWithDepartmentByStep() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
    // 这里设置了分步查询和延迟加载，所以在没有调用department参数时不会加载department
    System.out.println(mapper.getEmployeeWithDepartmentByStepOne(2).getEmployeeName());
  }
}