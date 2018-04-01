package com.zc.cris.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zc.cris.mybatis.bean.Department;
import com.zc.cris.mybatis.bean.EmpStatus;
import com.zc.cris.mybatis.bean.Employee;
import com.zc.cris.mybatis.dao.DepartmentMapper;
import com.zc.cris.mybatis.dao.EmployeeMapper;

class TestMybatis {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}

	public SqlSession getSession() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		return session;
	}

	/*
	 * mybatis中的两级缓存 1. 一级缓存（本地缓存）：与数据库同一次会话期间，查询到的数据会放到本地缓存中，以后如果需要获取相同的数据
	 * 直接从缓存中拿，没必要查询数据库，一级缓存也叫做sqlSession 级别的缓存，原理就是一个map - 一级缓存失效的四种情况： 1.
	 * 不同sqlSession之间无法共享缓存 2. sqlSession第一次查询某个数据 3.
	 * sqlSession两次查询之间执行了增删该操作（此次操作可能会对缓存的数据造成影响） 4. sqlSession手动清除了一级缓存（清空一级缓存）
	 * 
	 * 2. 二级缓存（全局缓存）：基于namespace 级别的缓存，一个namespace 对应一个二级缓存 工作机制： 1.
	 * 一个会话，查询的数据自动放入当前会话的一级缓存中 2. 如果会话关闭，一级缓存中的数据会被放入到二级缓存中，新的会话就会参照二级缓存中的数据 3.
	 * sqlSession---》employeeMapper---》employee ---》departmentMapper---》department
	 * 注意：不同的namespace查出的数据会各自放到自己对应的缓存中（map）
	 * 
	 * 效果：数据会从二级缓存中获取 查出的数据都会被默认先放在一级缓存中。 注意：只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中 使用：
	 * 1）、开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>
	 * 2）、去mapper.xml中配置使用二级缓存： <cache></cache> 3）、我们的POJO需要实现序列化接口
	 * 4).同一个sqlSessionFactory 得到不同的sqlSession 对象才可以正确测试二级缓存
	 * 
	 * * 和缓存有关的设置/属性： 1）、cacheEnabled=true：false：关闭缓存（二级缓存关闭）(一级缓存一直可用的)
	 * 2）、每个select标签都有useCache="true"： false：不使用缓存（一级缓存依然使用，二级缓存不使用）
	 * 3）、【每个增删改标签的：flushCache="true"：（一级二级都会清除）】 增删改执行完成后就会清楚缓存；
	 * 测试：flushCache="true"：一级缓存就清空了；二级也会被清除； 查询标签：flushCache="false"：
	 * 如果flushCache=true;每次查询之后都会清空缓存；缓存是没有被使用的；
	 * 4）、sqlSession.clearCache();只是清楚当前session的一级缓存；
	 * 5）、localCacheScope：本地缓存作用域：（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中；
	 * STATEMENT：可以禁用一级缓存；一般不用设置
	 *
	 * *第三方缓存整合*（以ehcache 缓存产品为例）： 1）、导入第三方缓存包即可； 2）、导入与第三方缓存整合的适配包；
	 * 3）、mapper.xml中使用自定义缓存 <cache
	 * type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
	 */

	@Test
	void testSecondLevelCache() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		SqlSession session2 = sqlSessionFactory.openSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);

		Employee emp = mapper.getById(1);
		System.out.println(emp);
		session.close();

		Employee emp2 = mapper2.getById(1);
		System.out.println(emp2);
		session2.close();

	}

	@Test
	void testFirstLevelCache() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee emp = mapper.getById(1);
		System.out.println(emp);

		// 默认开启一级缓存
		Employee emp1 = mapper.getById(1);
		System.out.println(emp1);
	}

	/*
	 * 测试分页插件（注意插件在代码中的顺序是不能随意更改的，否则无法正确使用分页插件）
	 */
	@Test
	void testPageHelper() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		// 使用 page 对象封装分页信息（索引从1开始，每页显示1条数据）
		Page<Object> page = PageHelper.startPage(5, 1);

		List<Employee> emps = mapper.getEmps();
		// 使用更加强大的 pageInfo
//		PageInfo<Employee> pageInfo = new PageInfo<>(emps);
		// 使用可以进行分页导航的 pageInfo（即保证前台页面显示5条页码）
		PageInfo<Employee> pageInfo = new PageInfo<>(emps, 5);
		

		for (Employee employee : emps) {
			System.out.println(employee);
		}

		System.out.println("page查询当前页码：" + page.getPageNum());
		System.out.println("page查询总记录数：" + page.getTotal());
		System.out.println("page查询每页记录数：" + page.getPageSize());
		System.out.println("page查询总共多少页：" + page.getPages());

		System.out.println("pageInfo查询当前页码：" + pageInfo.getPageNum());
		System.out.println("pageInfo查询总记录数：" + pageInfo.getTotal());
		System.out.println("pageInfo查询每页记录数：" + pageInfo.getPageSize());
		System.out.println("pageInfo查询总共多少页：" + pageInfo.getPages());
		System.out.println("pageInfo查询当前页码是否是第一页：" + pageInfo.isIsFirstPage());
		System.out.println("pageInfo查询当前页码是否是最后一页：" + pageInfo.isIsLastPage());
		
		// 取出需要展示的页码
		int[] pageNum = pageInfo.getNavigatepageNums();
		for (int i : pageNum) {
			System.out.println(i);
		}
	}
	
	/*
	 * 测试mybatis 的批量处理功能
	 */
	@Test
	void testBatch() throws IOException {
		// 获取普通session
//		SqlSession session = getSession();
		
		// 获取批处理session（实际是批处理executor）
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			mapper.addEmp(new Employee(null,"名字", '1', "邮箱"));
		}
		session.commit();
		long end = System.currentTimeMillis();
		// 共耗时：2787(非批量处理流程：预编译sql--设置参数--执行sql)1000次流程
		// 共耗时：758（批处理流程：预编译sql一次--设置参数1000次---执行一次）
		System.out.println("共耗时："+ (end - start));
		
	}
	
	/*
	 * mybatis 如何调用存储过程（以oracle 的分页查询为例）
	 * oracle 创建存储过程语句：
	 * 		create or replace procedure
	 * 				page_query(
	 * 					p_start in int, p_end in int, p_count out int, p_emps out sys_refcursor
	 * 			) as
	 * 		begin
	 * 				select count(*) into p_count from employees;
	 * 				open p_emps for
	 * 					select * from (select rownum rn, e.* from employees e where rownum <= p_end)
	 * 					where rn>=p_start;
	 * 		end page_query;
	 */
	@Test
	void testProcedure() throws IOException {
		
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		com.zc.cris.mybatis.bean.Page page = new com.zc.cris.mybatis.bean.Page();
		page.setStart(1);
		page.setEnd(5);
		mapper.getPageByProcedure(page);
		System.out.println("总记录数："+page.getTotalCount());
		System.out.println("查出的数据："+page.getEmps());
		
	}
	
	/*
	 * 如何使用java 的枚举
	 */
	@Test
	void testEnum() {
		EmpStatus empStatus = EmpStatus.logout;
		// 枚举实例的名字
		System.out.println(empStatus.name());
		// 枚举实例的索引（0开始）
		System.out.println(empStatus.ordinal());
	}
	
	/*
	 * 测试mybatis 如何处理枚举类型的数据
	 * 默认mybatis 使用EnumTypeHandler 处理枚举类型数据，即保存到数据库为枚举实例的名字
	 * 如果需要使用 EnumOrdinalTypeHandler 处理枚举数据，即保存到数据库为枚举的索引，需要在mybatis 的总配置文件设置
	 */
	@Test
	void testEnumHandler() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Employee employee = new Employee(null, "泷泽萝拉", '0', "123@qq.com");
		mapper.addEmp(employee);
		System.out.println(employee.getId());
		session.commit();
	}
	
	/*
	 * 测试自定义的枚举类型处理器
	 */
	@Test
	void testMyEnumHandler() throws IOException {
		SqlSession session = getSession();
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
//		Employee employee = new Employee(null, "拉克丝", '0', "123@qq.com");
//		mapper.addEmp(employee);
//		System.out.println(employee.getId());
		Employee emp = mapper.getById(1024);
		System.out.println(emp.getStatus().getMsg());
		session.commit();
	}
	
	
	
	
	
	
	
	
	
	

}
