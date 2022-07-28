package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.pojo.Employee;
import com.atguigu.mybatis.util.SqlSessionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class PageHelperTest {

  /**
   * limit index, pageSize
   * index: 当前页的开始索引
   * pageSize: 每页显示的条数
   * pageNum: 当前页的页码
   * index = (pageNum - 1) *pageSize
   */
  // 使用MyBatis的分页插件实现分页功能
  @Test
  public void testPageHelper() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

    int pageNum = 2;
    int pageSize = 3;
    // Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
    PageHelper.startPage(pageNum, pageSize);
    List<Employee> employeeList = mapper.getAllEmployee();
    // navigatePages: 导航页码数 例如如果导航页码数为5在显示第四页时导航应该显示 2 3 4 5 6 供用户选择
    PageInfo<Employee> page = new PageInfo<>(employeeList, 5);

    employeeList.forEach(System.out::println);
    System.out.println("page = " + page);
  }
}
