/**
 * 
 */
package com.bj.pojo;

/**
 * @author LQK
 *
 */
public class QueryResult {
	private String opt; //默认task_status
	private int task_id;
	private int status; //0:成功完成 1：进行中 2： 失败
	private String errmsg; //status=2 失败的时候才有
	/**
	 * @return the opt
	 */
	public String getOpt() {
		return opt;
	}
	/**
	 * @param opt the opt to set
	 */
	public void setOpt(String opt) {
		this.opt = opt;
	}
	/**
	 * @return the task_id
	 */
	public int getTask_id() {
		return task_id;
	}
	/**
	 * @param task_id the task_id to set
	 */
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the errmsg
	 */
	public String getErrmsg() {
		return errmsg;
	}
	/**
	 * @param errmsg the errmsg to set
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
