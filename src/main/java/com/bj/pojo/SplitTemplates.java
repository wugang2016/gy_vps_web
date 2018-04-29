/**
 * 
 */
package com.bj.pojo;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author LQK
 *
 */
public class SplitTemplates {
	private Integer id;
	@NotBlank(message = "模板名称不能为空")	
	@Size(min = 1, max = 128, message = "模板名称长度最大为128字节")
	private String name;
	@NotBlank(message = "描述不能为空")	
	@Size(min = 1, max = 255, message = "描述长度最大为255字节")
	private String desc;
	private Integer type;
	
	private List<FileArea> fileAreas;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the fileAreas
	 */
	public List<FileArea> getFileAreas() {
		return fileAreas;
	}
	/**
	 * @param fileAreas the fileAreas to set
	 */
	public void setFileAreas(List<FileArea> fileAreas) {
		this.fileAreas = fileAreas;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public boolean getIsDefault() {
		return (this.type != null && this.type == 1);
	}
	
}
