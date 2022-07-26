package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class DepartmentMapperTest {

  @Test
  public void testGetDepartmentAndEmployee() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
    System.out.println(
        "mapper.getDepartmentAndEmployee(1) = " + mapper.getDepartmentAndEmployee(1));
  }

  @Test
  public void testGetDepartmentAndEmployeeByStemp() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
    System.out.println(
        "mapper.getDepartmentAndEmployee(1) = " + mapper.getDepartmentAndEmployeeByStepOne(1));
  }
}