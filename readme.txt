1. 创建MyBatis的核心配置文件
   - 习惯上命名为 mybatis-config.xml (不是强制要求)。整合Spring后此配置文件可以省略
   - 核心配置文件主要用于配置连接数据库的环境以及MyBatis的全局配置信息

   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">

   <configuration>
      <!--设置连接数据库的环境-->
      <environments default="development">
           <environment id="development">
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="com.mysql.jdbc.Driver"/>
                   <property name="url" value="jdbc:mysql://localhost:3306/MyBatis"/>
                   <property name="username" value="root"/>
                   <property name="password" value="123456"/>
               </dataSource>
           </environment>
      </environments>

      <!--引入映射文件-->
      <mappers>
           <mapper resource="mappers/UserMapper.xml"/>
      </mappers>
   </configuration>

2. 创建mapper接口
   - MyBatis中的mapper接口相当于以前的dao。但是区别在于，mapper仅仅是接口，我们不需要提供实现类。
       public interface UserMapper {
         // 添加用户信息
         int addUser();
       }

3. 创建MyBatis的映射文件
   - ORM (Object Relationship Mapping) 对象关系映射
     - 对象: Java的实体类对象
     - 关系: 关系型数据库
     - 映射: 二者之间的对应关系

   1) 映射文件的命名规则:
      - 表所对应的实体类的类名+Mapper.xml (ex. Table t_user -> Class User -> UserMapper.xml)
      - MyBatis映射文件存放的位置是src/main/resources/mappers目录下

   2) MyBatis 面向接口编程的两个一致:
      a. 映射文件的 namespace 要和mapper接口的全类名保持一致
      b. 映射文件中sql语句的 id 要和mapper接口中的方法名一致

4. 通过junit测试功能
   - SqlSession:代表Java程序和数据库之间的会话。(HttpSession是Java程序和浏览器之间的会话)
   - SqlSessionFactory:是“生产”SqlSession的“工厂”。
   - 工厂模式:如果创建某一个对象，使用的过程基本固定，那么我们就可以把创建这个对象的
     相关代码封装到一个“工厂类”中，以后都使用这个工厂类来“生产”我们需要的对象。

   @Test
   public void testMyBatis() throws IOException {
     // 加载核心配置文件
     InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
     // 获取 SqlSessionFactoryBuilder
     SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
     // 获取 SqlSessionFactory
     SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

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

5. 加入log4j日志功能
   1) 加入依赖
   2) 加入log4j的配置文件，存放的位置是src/main/resources目录下
   3) 日志的级别:
      - FATAL(致命)>ERROR(错误)>WARN(警告)>INFO(信息)>DEBUG(调试)
      - 从左到右打印的内容越来越详细

6. 核心配置文件详解 (在mybatis-config.xml)

