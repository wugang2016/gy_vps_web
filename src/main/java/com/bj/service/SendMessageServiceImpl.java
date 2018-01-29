/**
 * 
 */
package com.bj.service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bj.job.AdminStatusTask;
import com.bj.job.SendMessageJob;

/**
 * @author LQK
 *
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    // 保留最近任务
    private LRUMap<UUID, AdminStatusTask> lastStatusJobs = new LRUMap<>(20);

    @Resource(name = "statusExecutor")
    private ExecutorService statusExecutor;
    
    @Value("${bijie.send.udp.ip}")
    private String ip;

    @Value("${bijie.send.udp.port}")
    private int port;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @Override
    public SendMessageJob sendMessage(String message, MultipartFile file, boolean isQuery){
    	SendMessageJob job = new SendMessageJob("上传文件"+UUID.randomUUID(), message, uploadFileDir, file, ip, port, isQuery);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public SendMessageJob sendMessage(String message, MultipartFile file){
    	SendMessageJob job = new SendMessageJob("上传文件"+UUID.randomUUID(), message, uploadFileDir, file, ip, port, false);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public SendMessageJob sendMessage(String message){
    	SendMessageJob job = new SendMessageJob("发送消息"+UUID.randomUUID(), message, ip, port);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public AdminStatusTask getAdminStatusTask(UUID taskId) {
        return lastStatusJobs.get(taskId);
    }
}