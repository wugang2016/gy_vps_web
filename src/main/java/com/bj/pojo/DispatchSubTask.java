package com.bj.pojo;

import com.bj.util.ErrorMessage;

/**
 * The persistent class for the tbl_file_split_subtask database table.
 * 
 */
public class DispatchSubTask extends BaseTask  {
	private static final long serialVersionUID = 1L;

	private int id;

	private int taskId;

	private Integer errCode;

	private int status;
	
	private SubSystemInfo subSystem;
	
	private SplitSubTask splitSubTask;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public SubSystemInfo getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(SubSystemInfo subSystem) {
		this.subSystem = subSystem;
	}

	public SplitSubTask getSplitSubTask() {
		return splitSubTask;
	}

	public void setSplitSubTask(SplitSubTask splitSubTask) {
		this.splitSubTask = splitSubTask;
	}

	public String getStatusText() {
		return SubTaskStatus.values()[status].text();
	}

	public boolean getIsFail() {
		return status == 3;
	}
	
	public String getErrMsg() {
		if(SubTaskStatus.values()[status].isOver()) {
			String msg = ErrorMessage.getProperty(this.errCode+"");
			if(msg == null && this.errCode != null) {
				msg = this.errCode+"";
			}
			return msg;
		}
		return "";
	}
}