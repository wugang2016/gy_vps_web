package com.bj.pojo;

import java.util.ArrayList;
import java.util.Arrays;

import com.bj.util.ErrorMessage;

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
	
	private Integer[] subSystemIds;
	private String subSystemIdsStr;
	
	public String getSubSystemIdsStr() {
		return subSystemIdsStr;
	}
	public void setSubSystemIdsStr(String subSystemIdsStr) {
		this.subSystemIdsStr = subSystemIdsStr;
		if(this.subSystemIdsStr != null)
		{
			String[] ecues = this.subSystemIdsStr.trim().split(",");
			ArrayList<Integer> subSystemIdsList = new ArrayList<Integer>();
			//subSystemIds = new Integer[ecues.length];
			for (int i = 0; i < ecues.length; i++) {
				String string = ecues[i];
				try {
					Integer v = Integer.parseInt(string);
					subSystemIdsList.add(v);
				}
				catch(NumberFormatException e) {
					
				}				
			}
			subSystemIds = new Integer[subSystemIdsList.size()];
			subSystemIdsList.toArray(subSystemIds);
		}
		else {
			this.subSystemIdsStr = "";
		}
	}
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
	
	public Integer[] getSubSystemIds() {
		return subSystemIds;
	}
	public void setSubSystemIds(Integer[] subSystemIds) {
		this.subSystemIds = subSystemIds;
		subSystemIdsStr="";
		if(subSystemIds != null)
		{
			for(int i =0;i<this.subSystemIds.length;++i)
			{
				subSystemIdsStr += subSystemIds[i];
				if(i != this.subSystemIds.length -1)
				{
					subSystemIdsStr += ',';
				}
			}
		}
	}
	
	public String getErrMsg() {
		String msg = ErrorMessage.getProperty(this.errCode+"");
		if(msg == null && this.errCode != null) {
			msg = this.errCode+"";
		}
		return msg;
	}
	public String format(String opt) {
		String ecue_list = "";
		if(subSystemIds !=null && subSystemIds.length > 0) {
			ecue_list = ",\"ecue_list\":" + Arrays.toString(subSystemIds);
		}
		return "{\"opt\":\"" + opt + "\", \"task_id\":" + id + ecue_list + "}";
	}
}
