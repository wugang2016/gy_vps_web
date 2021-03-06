package com.bj.job;

import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj.pojo.QueryResult;
import com.bj.util.BaseUtil;

import net.sf.json.JSONObject;

public class SendMessageJob implements AdminStatusTask, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageJob.class);
    private final String ip;
    private final int port;
    private final boolean hasFile;
	private final UUID taskId;
    private final String name;
    private final String message;
    private final boolean isQuery;
    private String status;
    private Date startTime;
    private Date endTime;
    private String backURL;

    public SendMessageJob(String taskName, String message, String ip, int port) {
    	this.ip = ip;
    	this.port = port;
    	this.hasFile = false;
    	this.isQuery = false;
    	this.message = message;
        this.name = taskName;
        this.taskId = UUID.randomUUID();
        this.status = "未开始";
    }

    public SendMessageJob(String taskName, String message, String ip, int port, boolean isQuery, boolean hasFile) {
    	this.ip = ip;
    	this.port = port;
    	this.hasFile = hasFile;
    	this.isQuery = isQuery;
    	this.message = message;
        this.name = taskName;
        this.taskId = UUID.randomUUID();
        this.status = "未开始";
    }

    @Override
    public void run() {
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        this.status = "初始化";
        this.startTime = new Date();
        LOGGER.info("Task:{} 任务开始执行 -{}", this.taskId, this.name);
        
        String uploadStatus = "";
        String sendStatus = "";
        try {
        	if(this.hasFile){
	            uploadStatus = "上传文件完成！<p/><p/>";
	            this.status = uploadStatus;
                LOGGER.info("Task:{} {}", this.taskId, uploadStatus);
        	}
        	
        	if(this.message != null){
        		sendStatus = "正在发送消息...";
                this.status = uploadStatus + sendStatus;
	            udpSend(this.message);
	            sendStatus = "发送消息完成！<p/><p/>";
	            this.status = uploadStatus + sendStatus;
                LOGGER.info("Task:{} {}", this.taskId, this.status);
        	}
            
            //query 查询状态
            if(this.isQuery){
	            this.status = uploadStatus + sendStatus + "查询状态中...";
            	String _message = "{\"opt\":\"query_task_status\",\"task_id\":0}";
            	for(int i=0; i<300; i++){
            		Thread.sleep(1000);
            		String json = udpSend(_message);
            		if(json != null){
            			JSONObject obj = JSONObject.fromObject(json);
            			QueryResult result = (QueryResult)JSONObject.toBean(obj, QueryResult.class);
            			if(result.getStatus() == 0){
            				this.status = uploadStatus + sendStatus + "查询状态完成...[成功]";
                            break;
            			}else if(result.getStatus() == 1){
                            this.status = uploadStatus + sendStatus + "查询状态中...[进行中]";
            			}else if(result.getStatus() == 2){
            				this.status = uploadStatus + sendStatus + "查询状态完成...[失败:" + result.getErrmsg() + "]";
                            break;
            			}
            		}
            	}
                LOGGER.info("Task:{} {}", this.taskId, this.status);
            }
        } catch (Exception e) {
            LOGGER.error("Task:{} 发送失败{}", this.taskId, e);
            this.status = uploadStatus + sendStatus + " 发送失败，" + e.getMessage();
        } finally {
            this.endTime = new Date();
        }
    }
    
    /**
     * 发送消息到设备
     */
    private String udpSend(String _message) throws SocketException,IOException{
        return BaseUtil.udpSend(ip, port, _message);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTaskId() {
        return taskId.toString();
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String getStatus() {
        return status;
    }
    
    @Override
    public String getBackURL() {
        return backURL;
    }

	@Override
	public String getZipPath() {
		return null;
	}

	@Override
	public Integer getState() {
		return null;
	}
}
