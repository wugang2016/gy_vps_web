/**
 * 
 */
package com.bj.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import net.sf.json.JSONObject;

/**
 * @author LQK
 *
 */
public class SubSystemInfo {
	private Integer id;
	@NotBlank(message = "用户名不能为空")	
	@Size(min = 1, max = 128, message = "用户名长度最大为128字节")
	private String name;
	@NotEmpty(message = "IP地址不能为空")
	@Pattern(regexp="^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$", message = "IP地址非法") 
	private String ip;
	@NotNull(message = "端口不能为空")
	@Range(min=1, max=65656, message = "端口范围是1-65536")
	private Integer port;
	//@NotBlank(message = "挂载地址不能为空")
	private String mntPath;
	@NotEmpty(message = "盒子IP不能为空")
	@Pattern(regexp="^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$", message = "IP地址非法") 
	private String boxIp;
	@Range(min=1, max=3980, message = "宽度范围1-3980")
	@NotNull(message = "宽度不能为空")
	private Integer width;
	@Range(min=1, max=2160, message = "高度范围1-2160")
	@NotNull(message = "高度不能为空")
	private Integer height;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
		this.mntPath = "/mnt/"+ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getMntPath() {
		return mntPath;
	}
	public void setMntPath(String mntPath) {
		this.mntPath = mntPath;
	}
	public String getBoxIp() {
		return boxIp;
	}
	public void setBoxIp(String boxIp) {
		this.boxIp = boxIp;
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
	public String format(String opt){
		String str = "{ \"opt\":\""+opt+"\",\"tbl_name\":\"tbl_sub_sys_info\",\"value\":";
		JSONObject obj = JSONObject.fromObject(this);
		String json = obj.toString();
		json = json.replace("\"id\"", "\"sub_sys_id\"");
		json = json.replace("\"name\"", "\"sub_sys_name\"");
		json = json.replace("\"mntPath\"", "\"mnt_path\"");
		json = json.replace("\"boxIp\"", "\"box_ip\"");
		return str + json;
	}
}
