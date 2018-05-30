/**
 * 
 */
package com.bj.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bj.job.AdminStatusTask;
import com.bj.job.ExportZipFileJob;
import com.bj.job.SendMessageJob;
import com.bj.util.BaseUtil;

/**
 * @author LQK
 *
 */
@Service
public class JobServiceImpl implements JobService {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    // 保留最近任务
    private LRUMap<String, AdminStatusTask> lastStatusJobs = new LRUMap<>(20);

    @Resource(name = "statusExecutor")
    private ExecutorService statusExecutor;
    
    @Value("${bijie.send.udp.ip}")
    private String ip;

    @Value("${bijie.send.udp.port}")
    private int port;
    
    @Value("${bijie.upload.file.path}")
    private String uploadFileDir;

    @Override
    public SendMessageJob sendMessage(String name, String message, MultipartFile file, String newFileName, boolean isQuery) throws IOException{
    	BaseUtil.doSaveFile(uploadFileDir, file, newFileName);
    	SendMessageJob job = new SendMessageJob(name, message, ip, port, isQuery, true);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public SendMessageJob sendMessage(String name, String message, MultipartFile file) throws IOException{
    	BaseUtil.doSaveFile(uploadFileDir, file, null);
    	SendMessageJob job = new SendMessageJob(name, message, ip, port, false, true);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public SendMessageJob sendMessage(String name, String message){
    	SendMessageJob job = new SendMessageJob(name, message, ip, port);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public SendMessageJob onlySendMessage(String message){
    	SendMessageJob job = new SendMessageJob("SEND", message, ip, port);
        statusExecutor.submit(job);
        return job;
    }

    @Override
    public AdminStatusTask getAdminStatusTask(String taskId) {
        return lastStatusJobs.get(taskId);
    }

	@Override
	public LRUMap<String, AdminStatusTask> getTaskMap() {
		return lastStatusJobs;
	}

	@Override
	public ExportZipFileJob exportZipFile(List<File> srcFiles, String zipPath) {
		ExportZipFileJob job = new ExportZipFileJob("zip_package", srcFiles, zipPath);
        lastStatusJobs.put(job.getTaskId(), job);
        statusExecutor.submit(job);
        return job;
	}
}
