package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.util.List;
import java.util.Map;
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

  @Test
  public void testGetUserCount() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    System.out.println("queryMapper.getUserCount() = " + queryMapper.getUserCount());
  }

  @Test
  public void testGetMapById() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    Map<String, Object> map = queryMapper.getMapById(1);
    System.out.println(map);
  }

  @Test
  public void testGetUserMap() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    List<Map<String, Object>> mapList = queryMapper.getUserMap();
    mapList.forEach(System.out::println);
  }

  @Test
  public void testGetUserIdMap() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    QueryMapper queryMapper = sqlSession.getMapper(QueryMapper.class);
    Map<String, Object> map = queryMapper.getUserIdMap();
    System.out.println(map);
  }
}