package com.zc.cris.mybatis.bean;

// 定义用户状态的枚举类
public enum EmpStatus {
	login(100, "用户已登录"), logout(200, "用户已登出"), remove(300, "用户已经被移除");

	private Integer code;
	private String msg;

	private EmpStatus(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/*
	 * 根据从数据库查询出来的状态码返回一个对应的具体的枚举实例
	 */
	public static EmpStatus getStatus(Integer code) {
		switch (code) {
		case 100:
			return login;
		case 200:
			return logout;
		case 300:
			return remove;

		default:
			return logout;
		}

	}
}
