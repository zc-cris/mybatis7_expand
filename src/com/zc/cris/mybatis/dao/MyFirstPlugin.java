package com.zc.cris.mybatis.dao;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

// 完成插件的签名：告诉mybatis当前插件用来拦截哪个对象的哪个方法
@Intercepts({
	@Signature(type=StatementHandler.class, method="parameterize", args= {java.sql.Statement.class})
	
})
public class MyFirstPlugin implements Interceptor {

	/*
	 * intercept:拦截
	 * 		拦截目标对象的目标方法的执行,也就是执行代理对象方法的时候需要调用的方法（包括目标方法的执行）
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("拦截到的目标方法："+invocation.getMethod());
		
		//动态的改变一下sql运行的参数:实现根据id查询员工都只会查询到5号员工的资料
		Object target = invocation.getTarget();
		System.out.println("当前拦截到的对象："+target);
		//拿到：StatementHandler==>ParameterHandler===>parameterObject
		//拿到target的元数据
		MetaObject metaObject = SystemMetaObject.forObject(target);
		Object value = metaObject.getValue("parameterHandler.parameterObject");
		System.out.println("sql语句用的参数是："+value);
		//修改完sql语句要用的参数
		metaObject.setValue("parameterHandler.parameterObject", 5);
		
		// 执行目标方法（这个方法很重要，一般都要执行target的方法，通过代理执行）
		Object result = invocation.proceed();
		//返回执行后的返回值
		return result;
	}

	/*
	 * plugin：包装目标对象----》为目标对象创建代理对象
	 */
	@Override
	public Object plugin(Object target) {
		System.out.println("四大对象创建的时候就会调用拦截器栈，"
				+ "而返回包装后的代理对象还是原四大对象取决于当前对象是否在我们的拦截器签名中："+target);
		
		// 借助Plugin的wrap 方法来使用当前Interceptor 包装我们目标对象
		Object wrap = Plugin.wrap(target, this);
		// 返回为当前target 创建的动态代理对象
		return wrap;
	}

	/*
	 * 将插件注册时的property 属性设置进来
	 */
	@Override
	public void setProperties(Properties properties) {
		System.out.println("插件的配置信息："+properties);
	}

}
