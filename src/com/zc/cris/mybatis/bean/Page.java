package com.zc.cris.mybatis.bean;

import java.util.List;

// 模拟oracle 的分页查询，将其定义为一个存储过程 procedure
public class Page {

	private int start;
	private int end;
	private int totalCount;
	private List<Employee> emps;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Employee> getEmps() {
		return emps;
	}

	public void setEmps(List<Employee> emps) {
		this.emps = emps;
	}

}
