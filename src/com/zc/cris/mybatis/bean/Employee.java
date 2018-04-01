package com.zc.cris.mybatis.bean;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("emp")
public class Employee implements Serializable {

	/**
	 * @Field Name：serialVersionUID
	 * @Description：TODO (用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Character gender;
	private String email;
	private Department department;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

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

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", gender=" + gender + ", email=" + email + "]";
	}

	public Employee(Integer id, String name, Character gender, String email) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.email = email;
	}

	public Employee() {
		super();

	}

	public Employee(Integer id, String name, Character gender, String email, Department department) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.department = department;
	}

}
