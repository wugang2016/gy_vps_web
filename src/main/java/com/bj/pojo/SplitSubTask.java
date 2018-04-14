package com.bj.pojo;

/**
 * The persistent class for the tbl_file_split_subtask database table.
 * 
 */
public class SplitSubTask extends BaseTask  {
	private static final long serialVersionUID = 1L;

	private int id;

	private int taskId;

	private int errCode;

	private String fileName;

	private String filePath;

	private int status;
	
	private FileArea fileArea;
	
	public String getStatusText() {
		return SubTaskStatus.values()[status].text();
	}

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

	public int getErrCode() {
		return this.errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public FileArea getFileArea() {
		return fileArea;
	}

	public void setFileArea(FileArea fileArea) {
		this.fileArea = fileArea;
	}

}