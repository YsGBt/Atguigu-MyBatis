package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import com.atguigu.mybatis.util.SqlSessionUtil;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class CacheMapperTest {

  // 通过同一个SqlSession两次查询相同的sql语句在log中只输出了一次sql语句
  @Test
  public void testGetEmployeeByEidCache() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();

    CacheMapper mapper = sqlSession.getMapper(CacheMapper.class);
    Employee employee = mapper.getEmployeeByEid(1);
    System.out.println("employee = " + employee);

    // 但是如果两次查询中使用不同的SqlSession或者出现了增删改，那么就需要再次查询
    // mapper.insertEmployee(new Employee(null, "abc", 23, "男", "123@qq.com"));
    // SqlSession sqlSession2 = SqlSessionUtil.getSqlSession();
    // 如果手动清空缓存也需要再次查询
    sqlSession.clearCache();

    CacheMapper mapper2 = sqlSession.getMapper(CacheMapper.class);
    Employee employee2 = mapper2.getEmployeeByEid(1);
    System.out.println("employee = " + employee2);
  }

  @Test
  public void testLevel2Cache() throws IOException {
    InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

    SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);

    CacheMapper mapper1 = sqlSession1.getMapper(CacheMapper.class);
    CacheMapper mapper2 = sqlSession2.getMapper(CacheMapper.class);

    System.out.println(mapper1.getEmployeeByEid(1));
    sqlSession1.close();
    System.out.println(mapper2.getEmployeeByEid(1));
  }
}