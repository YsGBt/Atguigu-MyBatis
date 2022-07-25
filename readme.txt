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

7. MyBatis获取参数值的两种方式 (重点)
   - MyBatis获取参数的两种方式:
     a. ${} 本质是字符串拼接
     b. #{} 本质是占位符赋值

   1) mapper接口方法的参数为单个的字面量类型
      - 此时可以使用${}和#{}以任意的名称获取参数的值，注意${}需要手动加单引号

        <!-- User getUserByUsername(String username); -->
        <select id="getUserByUsername" resultType="User">
          select * from t_user where username = #{username}
        </select>

   2) mapper接口方法的参数为多个时
      - 此时MyBatis会自动将这些参数放在一个map集合中，以arg0,arg1...为键，以参数为值;
        或者以 param1,param2...为键，以参数为值;
        因此只需要通过${}和#{}访问map集合的键就可以获取相对应的值，注意${}需要手动加单引号

        <!-- User checkLogin(String username, String password); -->
        <select id="checkLogin" resultType="User">
          select * from t_user where username = #{arg0} and password = #{arg1}
        </select>

   3) 若mapper接口方法的参数有多个时，可以手动将这个参数放在一个map中存储
      - 若mapper接口中的方法需要的参数为多个时，此时可以手动创建map集合，将这些数据放在map中
        只需要通过${}和#{}访问map集合的键就可以获取相对应的值，注意${}需要手动加单引号

        <!-- User checkLoginByMap(Map<String, Object> map); -->
        <select id="checkLoginByMap" resultType="User">
          select * from t_user where username = #{username} and password = #{password}
        </select>

   4) mapper接口方法的参数是实体类类型的参数
      - 此时可以使用${}和#{}，通过访问实体类对象中的属性名获取属性值，注意${}需要手动加单引号

        <!-- int createUser(User user); -->
        <insert id="createUser">
          insert into t_user values(null, #{username}, #{password}, #{age}, #{gender}, #{email})
        </insert>

   5) 使用 @Param 命名参数
      - 可以通过@Param注解标识mapper接口中的方法参数
        此时，会将这些参数放在map集合中，以@Param注解的value属性值为键，以参数为值;
        或者以 param1,param2...为键，以参数为值;
        只需要通过${}和#{}访问map集合的键就可以获取相对应的值， 注意${}需要手动加单引号

        <!-- User checkLoginByParam(@Param("username") String username, @Param("password") String password); -->
        <select id="checkLoginByParam" resultType="User">
          select * from t_user where username = #{username} and password = #{password}
        </select>

8. MyBatis的各种查询功能
   查询单个数据:
   1) 查询单个数据
         - MyBatis中设置了默认的类型别名
           Java.lang.Integer --> int, integer
           int               --> _int, _integer
           Map               --> map
           String            --> string

           <!-- Integer getUserCount(); -->
           <select id="getUserCount" resultType="Integer">
             select count(*) from t_user
           </select>

   查询单个实体类对象数据:
   2) 查询一个实体类对象
      - 若查询出的数据只有一条，可以通过实体类对象或者集合/实体类对象数组接收
      - 若查询出的数据有多条，可以通过集合/实体类对象数组接收，但一定不能通过实体类对象接收，此时会抛出异常TooManyResultsException

        <!-- User getUserById(@Param("id") Integer id); -->
        <select id="getUserById" resultType="User">
          select * from t_user where id = #{id}
        </select>

   3) 查询一条数据为map集合

           <!-- Map<String, Object> getMapById(@Param("id") Integer id); -->
           <select id="getMapById" resultType="map">
             select * from t_user where id = #{id}
           </select>
           结果: {password=123, gender=女, id=1, age=23, email=12345@qq.com, username=李四}


   查询多条数据:
   4) 查询一个List集合/实体类对象数组

        <!-- User[] getAllUser(); or List<User> getAllUser(); -->
        <select id="getAllUser" resultType="User">
          select * from t_user
        </select>

   5) 查询多条数据并用map集合的list接收

        <!-- List<Map<String, Object>> getUserMap(); -->
        <select id="getUserMap" resultType="map">
          select * from t_user;
        </select>

        或者使用 @MapKey 注解:
        - @MapKey注解设置map集合的键，值是每条数据所对应的map集合

        <!--
        @MapKey("id")
        Map<String, Object> getUserIdMap();
        -->
        <select id="getUserIdMap" resultType="map">
          select * from t_user;
        </select>

        结果: {1={password=123, gender=女, id=1, age=23, email=12345@qq.com, username=李四},
              2={password=123, gender=女, id=2, age=23, email=12345@qq.com, username=张三}}

        当然，
        @MapKey("id")
        Map<String, User> getUserIdMap();
        也是没有问题的

9. 特殊SQL的执行
   1) 模糊查询

      <!-- List<User> getUserByFuzzyName(@Param("username") String username); -->
      <select id="getUserByFuzzyName" resultType="User">
        <!-- select * from t_user where username like "%"#{username}"%" 或者: -->
        select * from t_user where username like concat('%', #{username}, '%')
      </select>

   2) 批量删除 (注意这里只能用${}，因为#{}会自动给参数两边添加''。而使用${}会带来SQL注入问题所以需要手动检测输入参数是否合法)

      <!-- int deleteBatchUser(@Param("ids") String ids); -->
      <delete id="deleteBatchUser">
        delete from t_user where id in (${ids})
      </delete>

   3) 动态设置表名 (这里也需要手动处理SQL注入问题)

      <!-- List<User> getUserByTableName(@Param("tableName") String tableName); -->
      <select id="getUserByTableName" resultType="User">
        select * from ${tableName}
      </select>

   4) 添加功能获取自增的主键
      - useGeneratedKeys: 设置使用自增的主键
      - keyProperty: 因为增删改有统一的返回值是受影响的行数，因此只能将获取的自增的主键放在传入的参
                     数user对象的某个属性中

      <!-- void createUser(@Param("user") User user); -->
      <insert id="createUser" useGeneratedKeys="true" keyProperty="id">
        insert into t_user values(null, #{user.username}, #{user.password}, #{user.age}, #{user.gender}, #{user.email})
      </insert>

10. 自定义映射resultMap
    1) 解决字段名和属性名不一致的情况
       a) 为字段起别名，保持和属性名一致
          <!-- List<Employee> getAllEmployee(); -->
          <select id="getAllEmployee" resultType="Employee">
            select eid, employee_name employeeName, age, gender, email from t_employee
          </select>

       b) 在 mybatis-config.xml 设置全局配置，将下划线自动映射为驼峰
          <!-- 设置MyBatis的全局配置 -->
          <settings>
            <!-- 将_自动映射为驼峰，employee_name:employeeName -->
            <setting name="mapUnderscoreToCamelCase" value="true"/>
          </settings>

       c) 通过 resultMap 设置自定义的映射关系
          <resultMap id="employeeMap" type="com.atguigu.mybatis.pojo.Employee">
            <id property="eid" column="eid"></id>
            <result property="employeeName" column="employee_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
          </resultMap>

          <!-- List<Employee> getAllEmployee(); -->
          <select id="getAllEmployee" resultMap="employeeMap">
            select * from t_employee
          </select>

    2) 处理多对一映射关系
       a) 级联属性赋值 （使用较少)
          <resultMap id="empAndDeptResultMapOne" type="Employee">
            <id property="eid" column="eid"></id>
            <result property="employeeName" column="employee_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
            <result property="department.did" column="did"></result>
            <result property="department.departmentName" column="department_name"></result>
          </resultMap>

          <!-- Employee getEmployeeWithDepartmentById(@Param("eid") Integer eid); -->
          <select id="getEmployeeWithDepartmentById" resultMap="empAndDeptResultMapOne">
            select * from t_employee left join t_department on t_employee.did = t_department.did where t_employee.eid = #{eid}
          </select>

       b) 通过使用 association 标签处理多对一映射关系
          - association: 处理多对一的映射关系
            - property: 需要处理多对一的映射关系的属性名
            - javaType: 该属性的类型

          <resultMap id="empAndDeptResultMapTwo" type="Employee">
            <id property="eid" column="eid"></id>
            <result property="employeeName" column="employee_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
            <association property="department" javaType="com.atguigu.mybatis.pojo.Department">
              <id property="did" column="did"></id>
              <result property="departmentName" column="department_name"></result>
            </association>
          </resultMap>

       c) 分步查询 (效率高，查询表数量少但查询数据量大的情况下推荐使用)
          - select: 设置分步查询的sql的唯一表示 (namespace.SQLID 或 mapper接口的全类名.方法名)
          - column: 设置分步查询的条件

          <resultMap id="empAndDeptByStepResultMap" type="Employee">
            <id property="eid" column="eid"></id>
            <result property="employeeName" column="employee_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
            <association property="department"
              select="com.atguigu.mybatis.mapper.DepartmentMapper.getEmployeeWithDepartmentByStepTwo"
              column="did"></association>
          </resultMap>

          <!-- Employee getEmployeeWithDepartmentByStepOne(@Param("eid") Integer eid); -->
          <select id="getEmployeeWithDepartmentByStepOne" resultMap="empAndDeptByStepResultMap">
            select * from t_employee where eid = #{eid}
          </select>

          然后在DepartmentMapper里:
          <!-- Department getEmployeeWithDepartmentByStepTwo(@Param("did") Integer did); -->
          <select id="getEmployeeWithDepartmentByStepTwo" resultType="Department">
            select * from t_department where did = #{did}
          </select>

    3)









