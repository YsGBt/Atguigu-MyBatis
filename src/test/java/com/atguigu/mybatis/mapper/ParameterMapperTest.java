package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class ParameterMapperTest {

  @Test
  public void testGetAllUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    mapper.getAllUser().forEach(System.out::println);
  }

  @Test
  public void testGetUserByUsername() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    System.out.println("mapper.getUserByUsername(\"张三\") = " + mapper.getUserByUsername("张三"));
  }

  @Test
  public void testCheckLogin() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    System.out.println(mapper.checkLogin("张三", "123"));
  }

  @Test
  public void testCheckLoginByMap() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    Map<String, Object> map = new HashMap<>();
    map.put("username", "张三");
    map.put("password", "123");
    System.out.println(mapper.checkLoginByMap(map));
  }

  @Test
  public void testCreateUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    System.out.println(mapper.createUser(new User(null, "王五", "123", 23, "男", "123@qq.com")));
  }

  @Test
  public void testCheckLoginByParam() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
    System.out.println(mapper.checkLoginByParam("张三", "123"));
  }
}