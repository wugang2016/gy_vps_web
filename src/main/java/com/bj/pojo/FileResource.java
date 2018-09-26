package com.bj.pojo;

import java.io.File;

public class FileResource {
	
	private Integer id;
	private String filePath;
	private Integer type;  //255:deleted 0:realplay file
	private String fileDesc;
	
	private Boolean goPlay;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public Boolean getGoPlay() {
		return goPlay;
	}
	public void setGoPlay(Boolean goPlay) {
		this.goPlay = goPlay;
	}
	public String getFileName() {
		if(this.filePath != null) {
			return this.filePath.substring(this.filePath.lastIndexOf(File.separator)+1);
		}
		return "";
	}
}
