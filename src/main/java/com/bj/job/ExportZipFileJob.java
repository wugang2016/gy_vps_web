package com.bj.job;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj.util.ZipUtil;

public class ExportZipFileJob implements AdminStatusTask, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportZipFileJob.class);
	private final UUID taskId;
	private final List<File> srcFiles; //可以是文件，也可以是文件夹
    private final String zipPath;
    private final String name;
    private String status;
    private Date startTime;
    private Date endTime;
    private String backURL;
    private Integer state = 0; //-1：失败，0：进行中，1：成功

    public ExportZipFileJob(String taskName, List<File> srcFiles, String zipPath) {
        this.name = taskName;
        this.taskId = UUID.randomUUID();
        this.status = "未开始";
        this.srcFiles = srcFiles;
        this.zipPath = zipPath;
    }

    @Override
    public void run() {
        this.status = "初始化";
        this.startTime = new Date();
        try {
            this.status = "正在打包中，请稍等...";
    		FileOutputStream out = new FileOutputStream(new File(zipPath));
    		ZipUtil.toZip(srcFiles, out, true);
            this.status = "打包完成!";
        } catch (Exception e) {
            LOGGER.error("Task:{} 打包失败{}", this.taskId, e);
            this.status = "任务失败，" + e.getMessage();
            state = -1;
        } finally {
            this.endTime = new Date();
        }
        state = 1;
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
        return zipPath;
    }

	@Override
	public Integer getState() {
		return state;
	}
}
