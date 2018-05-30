package com.bj.pojo;

public class Template {
	@Override
	public String toString() {
		return "Template [template_id=" + template_id + ", template_name=" + template_name + ", mini_pic_path="
				+ mini_pic_path + ", pic_path=" + pic_path + ", backgroud_video=" + backgroud_video
				+ ", sig_pic_boder_path=" + sig_pic_boder_path + ", longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}
	public Template() {
		super();
	}
	public Template(int template_id, String template_name, String mini_pic_path, String pic_path,
			String backgroud_video, String sig_pic_boder_path, String longitude, String latitude) {
		super();
		this.template_id = template_id;
		this.template_name = template_name;
		this.mini_pic_path = mini_pic_path;
		this.pic_path = pic_path;
		this.backgroud_video = backgroud_video;
		this.sig_pic_boder_path = sig_pic_boder_path;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public int getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(int template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public String getMini_pic_path() {
		return mini_pic_path;
	}
	public void setMini_pic_path(String mini_pic_path) {
		this.mini_pic_path = mini_pic_path;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getBackgroud_video() {
		return backgroud_video;
	}
	public void setBackgroud_video(String backgroud_video) {
		this.backgroud_video = backgroud_video;
	}
	public String getSig_pic_boder_path() {
		return sig_pic_boder_path;
	}
	public void setSig_pic_boder_path(String sig_pic_boder_path) {
		this.sig_pic_boder_path = sig_pic_boder_path;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	private int template_id;
	private String template_name;
	private String mini_pic_path;
	private String pic_path;
	private String backgroud_video;
	private String sig_pic_boder_path;
	private String longitude;
	private String latitude;
}
