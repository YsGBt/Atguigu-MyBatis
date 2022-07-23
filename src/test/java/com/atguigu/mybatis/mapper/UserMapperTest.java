package com.atguigu.mybatis.mapper;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class UserMapperTest {

  @Test
  public void testMyBatis() throws IOException {
    // 加载核心配置文件
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    // 获取 SqlSessionFactoryBuilder
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    // 获取 SqlSessionFactory
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

    // SqlSession默认不自动提交事务，若需要自动提交事务则使用sqlSessionFactory.openSession(true)
    // 创建SqlSession对象，此时通过SqlSession对象所操作的sql都必须手动提交或回滚事务
    // SqlSession sqlSession = sqlSessionFactory.openSession();

    // 创建SqlSession对象，此时通过SqlSession对象所操作的sql都会自动提交
    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    // 获取mapper接口对象
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

    // 测试功能
    System.out.println("userMapper.addUser() = " + userMapper.addUser());

    // 提交事务
    // sqlSession.commit();
  }

  @Test
  public void testUpdate() throws IOException {
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    userMapper.updateUser();
  }

  @Test
  public void testDelete() throws IOException {
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    userMapper.deleteUser();
  }

  @Test
  public void testSelect() throws IOException {
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    System.out.println("userMapper.getUserById() = " + userMapper.getUserById());
    System.out.println("------------------");
    userMapper.getAllUser().forEach(System.out::println);
  }
}