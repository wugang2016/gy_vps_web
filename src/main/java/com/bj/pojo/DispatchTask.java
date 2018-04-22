package com.bj.pojo;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The persistent class for the tbl_file_split_task database table.
 * 
 */
public class DispatchTask extends BaseTask{
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "任务名称不能为空")	
	@Size(min = 1, max = 128, message = "任务名称长度最大为128字节")
	private String taskName;

	private String endTime;

	private String startTime;

	private Integer status;

	private String taskDesc;

	private SplitTask splitTask;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTaskDesc() {
		return this.taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public SplitTask getSplitTask() {
		return splitTask;
	}

	public void setSplitTask(SplitTask splitTask) {
		this.splitTask = splitTask;
	}

	/****** 自定义 *********/
	public String getStatusText() {
		return TaskStatus.values()[status].text();
	}

	public boolean getIsFail() {
		return TaskStatus.values()[status].isFail();
	}
	
	public boolean getIsDelete() {
		return TaskStatus.values()[status].allowDelete();
	}
	
	public boolean getIsDispatch() {
		return TaskStatus.values()[status].allowDispatchForDispatchTask();
	}
	
	public String format() {
		return "{\"opt\":\"start_dispatch_task\", \"task_id\":" + id + "}";
	}
}