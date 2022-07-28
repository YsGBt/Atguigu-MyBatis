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
          - fetchType: 当开启了全局的延迟加载之后，可通过此属性手动控制延迟加载的效果 fetchType="eager"

          <resultMap id="empAndDeptByStepResultMap" type="Employee">
            <id property="eid" column="eid"></id>
            <result property="employeeName" column="employee_name"></result>
            <result property="age" column="age"></result>
            <result property="gender" column="gender"></result>
            <result property="email" column="email"></result>
            <association property="department"
              select="com.atguigu.mybatis.mapper.DepartmentMapper.getEmployeeWithDepartmentByStepTwo"
              column="did"
              fetchType="lazy"></association>
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

    3) 延迟加载
       - 开启延迟加载, 在mybatis-config中添加配置:
         <!-- 开启延迟加载 -->
         <setting name="lazyLoadingEnabled" value="true"/>
       - 此时如果Employee中的Department属性未被调用，则不会执行分步查询中查询Department的语句。只有在调用
         Department属性时才会执行。

    4) 处理一对多的映射关系
       a) 使用 collection 标签处理一对多映射关系
          - property: 处理一对多的映射关系的属性
          - ofType: 表示该属性所对应的集合中存储数据的类型

          <resultMap id="deptAndEmpResultMap" type="Department">
            <id property="did" column="did"></id>
            <result property="departmentName" column="department_name"></result>
            <collection property="employeeList" ofType="Employee">
              <id property="eid" column="eid"></id>
              <result property="employeeName" column="employee_name"></result>
              <result property="age" column="age"></result>
              <result property="gender" column="gender"></result>
              <result property="email" column="email"></result>
            </collection>
          </resultMap>

          <!-- Department getDepartmentAndEmployee(@Param("did") Integer did); -->
          <select id="getDepartmentAndEmployee" resultMap="deptAndEmpResultMap">
            select * from t_department left join t_employee on t_department.did = t_employee.did where t_department.did = #{did}
          </select>

       b) 分步查询

          <resultMap id="deptAndEmpByStepResultMap" type="Department">
            <id property="did" column="did"></id>
            <result property="departmentName" column="department_name"></result>
            <collection property="employeeList"
                        select="com.atguigu.mybatis.mapper.EmployeeMapper.getDepartmentAndEmployeeByStepTwo"
                        column="did">
            </collection>
          </resultMap>

          <!-- Department getDepartmentAndEmployeeByStepOne(@Param("did") Integer did); -->
          <select id="getDepartmentAndEmployeeByStepOne" resultMap="deptAndEmpByStepResultMap">
            select * from t_department where did = #{did}
          </select>

11. 动态SQL
    - Mybatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决
      拼接SQL语句字符串时的痛点问题。

    1) if 标签(常用): 根据标签中test属性所对应的表达式决定标签中的内容是否需要拼接到SQL中
       <!-- List<Employee> getEmployeeByCondition(@Param("employee") Employee employee); -->
       <select id="getEmployeeByCondition" resultType="Employee">
         select * from t_employee where 1=1
         <if test="employee.employeeName != null and employee.employeeName != ''">
           and employee_name = #{employee.employeeName}
         </if>
         <if test="employee.age != null and employee.age != ''">
           and age = #{employee.age}
         </if>
         <if test="employee.gender != null and employee.gender != ''">
           and gender = #{employee.gender}
         </if>
         <if test="employee.email != null and employee.email != ''">
           and email = #{employee.email}
         </if>
       </select>

    2) where 标签(常用):
       - 当where标签中有内容时，会自动生成where关键字，并且将内容前多余的and或or去掉 (不能去掉内容后多余的and和or)
       - 当where标签中没有内容时，不会生成where关键字
       <!-- List<Employee> getEmployeeByCondition(@Param("employee") Employee employee); -->
       <select id="getEmployeeByCondition" resultType="Employee">
         select * from t_employee
         <where>
           <if test="employee.employeeName != null and employee.employeeName != ''">
             employee_name = #{employee.employeeName}
           </if>
           <if test="employee.age != null and employee.age != ''">
             and age = #{employee.age}
           </if>
           <if test="employee.gender != null and employee.gender != ''">
             and gender = #{employee.gender}
           </if>
           <if test="employee.email != null and employee.email != ''">
             and email = #{employee.email}
           </if>
         </where>
       </select>

    3) trim 标签:
       - 若标签中有内容时:
         - prefix|suffix: 将trim标签内容前面或后面添加指定内容
         - suffixOverrides|prefixOverrides: 将trim标签内容前面或后面去掉指定内容
       - 若标签中没有内容时，trim标签没有任何效果
       <!-- List<Employee> getEmployeeByCondition(@Param("employee") Employee employee); -->
       <select id="getEmployeeByCondition" resultType="Employee">
         select * from t_employee
         <trim prefix="where" suffixOverrides="and|or">
           <if test="employee.employeeName != null and employee.employeeName != ''">
             employee_name = #{employee.employeeName} and
           </if>
           <if test="employee.age != null and employee.age != ''">
             age = #{employee.age} and
           </if>
           <if test="employee.gender != null and employee.gender != ''">
             gender = #{employee.gender} or
           </if>
           <if test="employee.email != null and employee.email != ''">
             email = #{employee.email}
           </if>
         </trim>
       </select>

    4) choose, when, otherwise 标签: 相当于 if, else if, else
       - when至少有一个，otherwise最多一个
       <!-- List<Employee> getEmployeeByChoose(@Param("employee") Employee employee); -->
       <select id="getEmployeeByChoose" resultType="Employee">
         select * from t_employee
         <where>
           <choose>
             <when test="employee.employeeName != null and employee.employeeName != ''">
               employee_name = #{employee.employeeName}
             </when>
             <when test="employee.age != null and employee.age != ''">
               age = #{employee.age}
             </when>
             <when test="employee.gender != null and employee.gender != ''">
               gender = #{employee.gender}
             </when>
             <when test="employee.email != null and employee.email != ''">
               email = #{employee.email}
             </when>
             <otherwise>
               did = 1
             </otherwise>
           </choose>
         </where>
       </select>

    5) foreach 标签(常用):
       - collection: 设置需要循环的数组或集合
       - item: 表示数组或集合中的每一个数据
       - separator: 循环体之间的分隔符，会自动在分隔符左右添加空格
       - open|close: 和trim中的prefix|suffix一样，但是在foreach没有内容的情况下依旧会生效

       a) 批量删除的两种方式:
          <!-- int deleteEmployeeBatchByArray(@Param("eids") Integer[] eids); -->
          <delete id="deleteEmployeeBatchByArray">
            delete from t_employee where
            <foreach collection="eids" item="eid" separator="or">
              eid = #{eid}
            </foreach>
          </delete>

          <delete id="deleteEmployeeBatchByArrayUsingIn">
            delete from t_employee where eid in
            <foreach collection="eids" item="eid" separator="," open="(" close=")">
              #{eid}
            </foreach>
          </delete>

       b) 批量添加:
          <!-- int insertEmployeeBatchByList(@Param("emps") List<Employee> employeeList); -->
          <insert id="insertEmployeeBatchByList">
            insert into t_employee values
            <foreach collection="emps" item="emp" separator=",">
              (null, #{emp.employeeName}, #{emp.age}, #{emp.gender}, #{emp.email}, null)
            </foreach>
          </insert>

    6) sql 标签:
       - 设置sql片段:
       <sql id="employeeColumns">eid,employee_name,age,sex,email</sql>

       - 引用sql片段 (使用 include 标签):
       <select id="getEmployeeByChoose" resultType="Employee">
          select <include refid="employeeColumns"></include> from t_employee
       </select>

12. MyBatis的缓存
    1) MyBatis的一级缓存 (默认开启)
       - 一级缓存是SqlSession级别的，通过同一个SqlSession查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，
         不会从数据库重写访问

       - 使一级缓存失效的四种情况:
         a) 不同的SqlSession对应不同的一级缓存
         b) 同一个SqlSession但是查询条件不同
         c) 同一个SqlSession两次查询期间执行了任何一次增删改操作
         d) 同一个SqlSession两次查询期间手动清空了缓存

    2) MyBatis的二级缓存 (需要手动开启)
       - 二级缓存是SqlSessionFactory级别的，通过同一个SqlSessionFactory创建的SqlSession查询的结果都会被缓存

       - 二级缓存开启的步骤:
         a) 在核心配置文件mybatis-config.xml中设置全局配置属性cacheEnabled="true"，默认属性为true所以不需要手动开启
         b) 在映射文件中设置标签 <cache />
         c) 二级缓存必须在SqlSession关闭或提交之后有效 (没有关闭或提交时会被保存在一级缓存中)
         d) 查询的数据所转换的实体类型必须实现序列化接口 Serializable

       - 使二级缓存失效的清空:
         a) 两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效

       - 二级缓存的相关配置 <cache />
         a) eviction属性: 缓存回收策略
            LRU(Least Recently Used) – 最近最少使用的:移除最长时间不被使用的对象。
            FIFO(First in First out) – 先进先出:按对象进入缓存的顺序来移除它们。
            SOFT – 软引用:移除基于垃圾回收器状态和软引用规则的对象。
            WEAK – 弱引用:更积极地移除基于垃圾收集器状态和弱引用规则的对象。
            默认的是 LRU。
         b) flushInterval属性: 刷新间隔，单位毫秒
            默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新
         c) size属性: 引用数目，正整数
            代表缓存最多可以存储多少个对象，太大容易导致内存溢出
         d) readOnly属性: 只读，true/false
            true:只读缓存;会给所有调用者返回缓存对象的相同实例。因此这些对象不应该被修改。这提供了很重要的性能优势。
            false:读写缓存;会返回缓存对象的拷贝(通过序列化)。这会慢一些，但是安全，因此默认是 false。

    3) MyBatis缓存的查询顺序
       a) 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用。
       b) 如果二级缓存没有命中，再查询一级缓存
       c) 如果一级缓存也没有命中，则查询数据库
       d) SqlSession关闭之后，一级缓存中的数据会写入二级缓存

    4) 整合第三方缓存 EHCache
       a) 添加依赖
       b) 创建EHCache的配置文件 ehcache.xml (配置文件名不能更改)
       c) 设置二级缓存的类型
          <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
       d) 加入logback日志
          存在SLF4J时，作为简易日志的log4j将失效，此时我们需要借助SLF4J的具体实现logback来打印日志。
          创建logback的配置文件logback.xml (配置文件名不能更改)

13. MyBatis的逆向工程
    - 正向工程:先创建Java实体类，由框架负责根据实体类生成数据库表。Hibernate是支持正向工程的。
    - 逆向工程:先创建数据库表，由框架负责根据数据库表，反向生成如下资源:
      - Java实体类
      - Mapper接口
      - Mapper映射文件

    1) 添加依赖和插件
    2) 创建MyBatis的核心配置文件
    3) 创建逆向工程的配置文件 文件名必须是 generatorConfig.xml
    4) 执行MBG插件的generate目标

14. 分页插件
    1) 分页插件配置
       a) 添加依赖
          <!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
          <dependency>
              <groupId>com.github.pagehelper</groupId>
              <artifactId>pagehelper</artifactId>
              <version>5.2.0</version>
          </dependency>

       b) 配置分页插件 (在MyBatis的核心配置文件中配置插件)
          <plugins>
              <!--设置分页插件-->
              <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
          </plugins>

    2) 分页插件使用
       a) 在查询功能之前使用PageHelper.startPage(int pageNum, int pageSize)开启分页功能
          - limit index, pageSize
          - index: 当前页的开始索引
          - pageSize: 每页显示的条数
          - pageNum: 当前页的页码
          - index = (pageNum - 1) *pageSize

       b) 在查询获取list集合之后，使用PageInfo<T> pageInfo = new PageInfo<>(List<T> list, int navigatePages)获取分页相关数据
          - list:分页之后的数据
          - navigatePages:导航分页的页码数

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

       c) 分页相关数据
          PageInfo{
          pageNum=8, pageSize=4, size=2, startRow=29, endRow=30, total=30, pages=8,
          list=Page{count=true, pageNum=8, pageSize=4, startRow=28, endRow=32, total=30, pages=8, reasonable=false, pageSizeZero=false},
          prePage=7, nextPage=0, isFirstPage=false, isLastPage=true, hasPreviousPage=true,
          hasNextPage=false, navigatePages=5, navigateFirstPage=4, navigateLastPage=8, navigatePageNums=[4, 5, 6, 7, 8]
          }
          常用数据:
          - pageNum:当前页的页码
          - pageSize:每页显示的条数
          - size:当前页显示的真实条数
          - total:总记录数
          - pages:总页数
          - prePage:上一页的页码
          - nextPage:下一页的页码
