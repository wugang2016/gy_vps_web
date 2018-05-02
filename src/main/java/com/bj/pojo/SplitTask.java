package com.bj.pojo;

import java.io.File;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.bj.util.ErrorMessage;

/**
 * The persistent class for the tbl_file_split_task database table.
 * 
 */
public class SplitTask extends BaseTask{
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "任务名称不能为空")	
	@Size(min = 1, max = 128, message = "任务名称长度最大为128字节")
	private String taskName;

	private String endTime;

	private String srcFilePath;

	private String startTime;

	private Integer status;

	private String taskDesc;

	private Integer errCode;

	private SplitTemplates splitTemplate;

	private String defaultFileName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSrcFilePath() {
		return this.srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
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

	public SplitTemplates getSplitTemplate() {
		return splitTemplate;
	}

	public void setSplitTemplate(SplitTemplates splitTemplate) {
		this.splitTemplate = splitTemplate;
	}
	
	public String getDefaultFileName() {
		return defaultFileName;
	}

	public void setDefaultFileName(String defaultFileName) {
		this.defaultFileName = defaultFileName;
	}

	/****** 自定义 *********/
	public String getStatusText() {
		return TaskStatus.values()[status].text();
	}
	
	public String getFileName() {
		if(this.srcFilePath != null) {
			return this.srcFilePath.substring(this.srcFilePath.lastIndexOf(File.separator)+1);
		}
		return "";
	}
	
	public String getErrMsg() {
		String msg = ErrorMessage.getProperty(this.errCode+"");
		if(msg == null && this.errCode != null) {
			msg = this.errCode+"";
		}
		return msg;
	}
	
	public boolean getIsFail() {
		return TaskStatus.values()[status].isFail();
	}
	
	public boolean getIsDelete() {
		return TaskStatus.values()[status].allowDelete();
	}
	
	public boolean getIsDispatch() {
		return TaskStatus.values()[status].allowDispatch();
	}
	
	public String format() {
		return "{\"opt\":\"start_file_task\", \"task_id\":" + id + "}";
	}

}