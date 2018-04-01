package com.zc.cris.mybatis.bean;

import java.io.Serializable;
import java.util.List;

public class Department implements Serializable {

	/**
	 * @Field Name：serialVersionUID
	 * @Description：TODO (用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private List<Employee> employees;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}

	public Department(Integer id) {
		super();
		this.id = id;
	}

}
