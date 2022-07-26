package com.atguigu.mybatis.mapper;

import static org.junit.Assert.*;

import com.atguigu.mybatis.pojo.Employee;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class DynamicSQLMapperTest {

  @Test
  public void testGetEmployeeByCondition() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
    Employee employee = new Employee(null, "", 12, "男", "123@qq.com");
    List<Employee> employeeList = mapper.getEmployeeByCondition(employee);
    for (Employee cur : employeeList) {
      System.out.println("employee = " + cur);
    }
  }

  @Test
  public void testGetEmployeeByChoose() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
    Employee employee = new Employee(null, "张三", 12, "", "");
    List<Employee> employeeList = mapper.getEmployeeByChoose(employee);
    for (Employee cur : employeeList) {
      System.out.println("employee = " + cur);
    }
  }

  @Test
  public void testDeletEmployeeBatchByArray() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
    int res = mapper.deleteEmployeeBatchByArray(new Integer[]{9, 10, 11});
    System.out.println("res = " + res);
  }

  @Test
  public void testInsertEmployeeBatchByList() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
    Employee employee1 = new Employee(null, "a", 23, "男", "123@qq.com");
    Employee employee2 = new Employee(null, "b", 23, "男", "123@qq.com");
    Employee employee3 = new Employee(null, "c", 23, "男", "123@qq.com");
    List<Employee> employeeList = Arrays.asList(employee1, employee2, employee3);
    int res = mapper.insertEmployeeBatchByList(employeeList);
    System.out.println("res = " + res);
  }
}