package com.bj.pojo;

public class RealplayTask extends BaseTask{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private FileResource fileResource;
	private SplitTemplates splitTemplate;
	private Boolean repeate;
	private String startTime;
	private String endTime;
	private Integer status;
	private Integer errCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public FileResource getFileResource() {
		return fileResource;
	}
	public void setFileResource(FileResource fileResource) {
		this.fileResource = fileResource;
	}
	public SplitTemplates getSplitTemplate() {
		return splitTemplate;
	}
	public void setSplitTemplate(SplitTemplates splitTemplate) {
		this.splitTemplate = splitTemplate;
	}
	public Boolean getRepeate() {
		if(repeate == null) {
			repeate = false;
		}
		return repeate;
	}
	public void setRepeate(Boolean repeate) {
		if(repeate == null)repeate = false;
		this.repeate = repeate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getErrCode() {
		return errCode;
	}
	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getStatusText() {
		return PlayStatus.values()[status].text();
	}

	public boolean getIsFail() {
		return PlayStatus.values()[status].isFail();
	}

	public boolean getIsDelete() {
		return PlayStatus.values()[status].allowDelete();
	}

	public boolean getAllowStop() {
		return PlayStatus.values()[status].allowStop();
	}

	public boolean getAllowReplay() {
		return PlayStatus.values()[status].allowReplay();
	}
}