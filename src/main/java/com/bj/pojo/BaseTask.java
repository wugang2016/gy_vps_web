package com.bj.pojo;

import java.io.Serializable;

public class BaseTask implements Serializable {
	private static final long serialVersionUID = 1L;
	private String taskPassword;
	
	public String getTaskPassword() {
		return taskPassword;
	}
	public void setTaskPassword(String taskPassword) {
		this.taskPassword = taskPassword;
	}
}
