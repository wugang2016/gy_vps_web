package com.bj.pojo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class FileArea {
	private Integer id;
	@Range(min=0, message = "横坐标必须大于等于0")
	@NotNull(message = "横坐标不能为空")	
	private Integer x;
	@Min(value=0, message = "纵坐标必须大于等于0")
	@NotNull(message = "纵坐标不能为空")	
	private Integer y;
	@Range(min=1, max=3940, message = "宽度范围1-3940")
	@NotNull(message = "宽度不能为空")
	private Integer width;
	@Range(min=1, max=2160, message = "高度范围1-2160")
	@NotNull(message = "高度不能为空")
	private Integer height;
	private SubSystemInfo subSystem;
	private Integer templateId;
	
	private String customFileName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public SubSystemInfo getSubSystem() {
		return subSystem;
	}
	public void setSubSystem(SubSystemInfo subSystem) {
		this.subSystem = subSystem;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getCustomFileName() {
		return customFileName;
	}
	public void setCustomFileName(String customFileName) {
		this.customFileName = customFileName;
	}
	public String format(String opt){
		String str = "{\"opt\":\""+opt+"\",\"tbl_name\":\"tbl_file_area\",\"value\":{" 
				+ "\"area_id\":" + id
				+ ",\"pos_x\":" + x 
				+ ",\"pos_y\":" + y 
				+ ",\"width\":" + width
				+ ",\"height\":" + height
				+ ",\"sub_sys_id\":" + subSystem.getId()
				+ "}}";
		return str;
	}
}
