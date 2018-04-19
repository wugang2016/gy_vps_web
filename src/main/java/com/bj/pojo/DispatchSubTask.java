package com.bj.pojo;

/**
 * The persistent class for the tbl_file_split_subtask database table.
 * 
 */
public class DispatchSubTask extends BaseTask  {
	private static final long serialVersionUID = 1L;

	private int id;

	private int taskId;

	private int errCode;

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
	
	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
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
}