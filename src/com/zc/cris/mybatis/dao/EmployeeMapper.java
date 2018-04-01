package com.zc.cris.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.zc.cris.mybatis.bean.Employee;

public interface EmployeeMapper {
	
	public Employee getById(Integer id);
	
	public List<Employee> getEmps();
	
}
