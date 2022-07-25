package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.User;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class SpecialSQLMapperTest {

  @Test
  public void testGetUserByFuzzyName() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    SpecialSQLMapper mapper = sqlSession.getMapper(SpecialSQLMapper.class);
    List<User> list = mapper.getUserByFuzzyName("三");
    System.out.println(list);
  }

  @Test
  public void testDeleteBatchUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    SpecialSQLMapper mapper = sqlSession.getMapper(SpecialSQLMapper.class);
    int result = mapper.deleteBatchUser("4,5,6");
    System.out.println(result);
  }

  @Test
  public void testUserByTableName() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    SpecialSQLMapper mapper = sqlSession.getMapper(SpecialSQLMapper.class);
    List<User> userList = mapper.getUserByTableName("t_user");
    userList.forEach(System.out::println);
  }

  @Test
  public void testCreateUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    SpecialSQLMapper mapper = sqlSession.getMapper(SpecialSQLMapper.class);
    User user = new User(null, "Steven", "123", 21, "男", "123@qq.com");
    mapper.createUser(user);
    System.out.println(user);
  }
}