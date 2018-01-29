package com.bj.job;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        this.status = "初始化";
        this.startTime = new Date();
        LOGGER.info("Task:{} 任务开始执行 -", this.taskId, this.name);
        
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
            		String result = udpSend(_message);
            		if(result != null){
            			//2:成功完成 1：进行中 3： 失败
            			if(result.indexOf("\"status\" : 2") != -1){
            				this.status = uploadStatus + sendStatus + "查询状态完成...[成功]";
                            break;
            			}else if(result.indexOf("\"status\" : 1") != -1){
                            this.status = uploadStatus + sendStatus + "查询状态中...[进行中]";
            			}else if(result.indexOf("\"status\" : 3") != -1){
            				this.status = uploadStatus + sendStatus + "查询状态完成...[失败]";
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
        LOGGER.info("发送内容：{}", _message);
    	DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			//1.发送
			byte[] buf = _message.getBytes();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
			ds.send(dp);
			//2.接收
            byte[] getBuf = new byte[1024];
            DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);  
            ds.setSoTimeout(2000);
            ds.receive(getPacket);
            String backMes = new String(getBuf, 0, getPacket.getLength());  
            LOGGER.info("接受方返回的消息：{}", backMes);
            return backMes;
		} finally{
			ds.close();
		}
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getTaskId() {
        return taskId;
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
}
