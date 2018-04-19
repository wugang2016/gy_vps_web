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
	private int id;

	//Column(name="pos_x")
	private int x;

	//Column(name="pos_y")
	private int y;

	//Column(name="w")
	private int w;
	
	//Column(name="h")
	private int h;

	//Column(name="sub_sys_id")
	private SubSystemInfo subSystem;

	//Column(name="template_id")
	private AndroidRealplayTemplate template;

	public int getH() {
		return this.h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public SubSystemInfo getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(SubSystemInfo subSystem) {
		this.subSystem = subSystem;
	}

	public void setTemplate(AndroidRealplayTemplate template) {
		this.template = template;
	}

	public AndroidRealplayTemplate getTemplate() {
		return template;
	}

	public void setTemplateId(AndroidRealplayTemplate template) {
		this.template = template;
	}
}