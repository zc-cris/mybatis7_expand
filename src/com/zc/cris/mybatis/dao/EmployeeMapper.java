package com.zc.cris.mybatis.dao;

import java.util.List;

import com.zc.cris.mybatis.bean.Employee;
import com.zc.cris.mybatis.bean.Page;

public interface EmployeeMapper {
	
	public Employee getById(Integer id);
	
	public List<Employee> getEmps();
	
	public void addEmp(Employee employee);
	
	public void getPageByProcedure(Page page);
	
}
