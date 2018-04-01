package com.zc.cris.mybatis.dao;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

//完成插件的签名：告诉mybatis当前插件用来拦截哪个对象的哪个方法
@Intercepts({
	@Signature(type=StatementHandler.class, method="parameterize", args= {java.sql.Statement.class})
	
})
public class MySecondPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation arg0) throws Throwable {
		System.out.println("MySecondPlugin:" + arg0.getMethod());
		return arg0.proceed();
	}

	@Override
	public Object plugin(Object target) {
		System.out.println("MySecondPlugin:"+target);
		// 借助Plugin的wrap 方法来使用当前Interceptor 包装我们目标对象
		Object wrap = Plugin.wrap(target, this);
		// 返回为当前target 创建的动态代理对象
		return wrap;
	}

	@Override
	public void setProperties(Properties arg0) {
	}

}
