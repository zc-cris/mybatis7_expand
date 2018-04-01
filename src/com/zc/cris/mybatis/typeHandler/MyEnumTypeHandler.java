package com.zc.cris.mybatis.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.SynchronousQueue;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.zc.cris.mybatis.bean.EmpStatus;

// 自定义的类型处理器必须实现 TypeHandler 或者继承 BaseTypeHandler
public class MyEnumTypeHandler implements TypeHandler<EmpStatus> {

	/*
	 * 定义当前数据如何保存到数据库中
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, EmpStatus status, JdbcType type) throws SQLException {
		System.out.println("要保存的状态码为："+status.getCode());
		ps.setString(i, status.getCode().toString());
	}

	/*
	 * 根据数据库的列名返回对应的查询结果（状态码），然后根据状态码返回枚举实例
	 */
	@Override
	public EmpStatus getResult(ResultSet rs, String columnName) throws SQLException {
		int code = rs.getInt(columnName);
		System.out.println("从数据库获取的状态码：" + code);
		return EmpStatus.getStatus(code);
	}

	@Override
	public EmpStatus getResult(ResultSet rs, int i) throws SQLException {
		int code = rs.getInt(i);
		return EmpStatus.getStatus(code);
	}

	@Override
	public EmpStatus getResult(CallableStatement rs, int i) throws SQLException {
		int code = rs.getInt(i);
		return EmpStatus.getStatus(code);
	}

}
