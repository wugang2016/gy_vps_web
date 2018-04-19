package com.bj.pojo;

import java.io.Serializable;


/**
 * The persistent class for the tbl_android_realplay_area database table.
 * 
 * 
 * (name="tbl_android_realplay_area")
 */
public class AndroidRealplayArea implements Serializable {
	private static final long serialVersionUID = 1L;

	//Column(name="area_id")
	private Integer id;

	//Column(name="pos_x")
	private Integer x;

	//Column(name="pos_y")
	private Integer y;

	//Column(name="w")
	private Integer w;
	
	//Column(name="h")
	private Integer h;

	//Column(name="sub_sys_id")
	private SubSystemInfo subSystem;

	//Column(name="template_id")
	private Integer templateId;

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

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
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

}