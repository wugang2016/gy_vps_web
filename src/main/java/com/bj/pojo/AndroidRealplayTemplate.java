package com.bj.pojo;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the tbl_android_realplay_template database table.
 * 
 * Table(name="tbl_android_realplay_template")
 */
public class AndroidRealplayTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	//Column(name="template_id")
	private Integer id;

	private String name;

	private String desc;

	//Column(name="backgroud_video")
	private String backgroudVideo;

	private String latitude;

	private String longitude;

	//Column(name="mini_pic_path")
	private String miniPicPath;

	//Column(name="pic_path")
	private String picPath;

	//Column(name="sig_pic_boder_path")
	private String sigPicBoderPath;
	
	private List<AndroidRealplayArea> realplayAreas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBackgroudVideo() {
		return this.backgroudVideo;
	}

	public void setBackgroudVideo(String backgroudVideo) {
		this.backgroudVideo = backgroudVideo;
	}
	
	public String getVideoName() {
		if(this.backgroudVideo != null) {
			return this.backgroudVideo.substring(this.backgroudVideo.lastIndexOf(File.separator)+1);
		}
		return "";
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMiniPicPath() {
		return this.miniPicPath;
	}

	public void setMiniPicPath(String miniPicPath) {
		this.miniPicPath = miniPicPath;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getSigPicBoderPath() {
		return this.sigPicBoderPath;
	}

	public void setSigPicBoderPath(String sigPicBoderPath) {
		this.sigPicBoderPath = sigPicBoderPath;
	}

	public List<AndroidRealplayArea> getRealplayAreas() {
		return realplayAreas;
	}

	public void setRealplayAreas(List<AndroidRealplayArea> realplayAreas) {
		this.realplayAreas = realplayAreas;
	}

}