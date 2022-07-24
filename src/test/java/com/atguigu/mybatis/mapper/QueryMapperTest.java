package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import com.atguigu.mybatis.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class QueryMapperTest {

  @Test
  public void testGetUserById() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    System.out.println("queryMapper.getUserById(1) = " + queryMapper.getUserById(1));
  }

  @Test
  public void testGetAllUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    for (User user : queryMapper.getAllUser()) {
      System.out.println(user);
    }
  }
}