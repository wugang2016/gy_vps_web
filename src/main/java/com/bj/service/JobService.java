/**
 * 
 */
package com.bj.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.web.multipart.MultipartFile;

import com.bj.job.AdminStatusTask;
import com.bj.job.ExportZipFileJob;
import com.bj.job.SendMessageJob;

/**
 * @author LQK
 *
 */
public interface JobService {
	SendMessageJob sendMessage(String name,String message,MultipartFile file,String newFileName,boolean isQuery) throws IOException;
	SendMessageJob sendMessage(String name,String message,MultipartFile file) throws IOException;
	SendMessageJob sendMessage(String name,String message);
	SendMessageJob onlySendMessage(String message);
	ExportZipFileJob exportZipFile(List<File> srcFiles, String zipPath);
	AdminStatusTask getAdminStatusTask(String taskId);
	LRUMap<String, AdminStatusTask> getTaskMap();
}
