/**
 * 
 */
package com.bj.service;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.web.multipart.MultipartFile;

import com.bj.job.AdminStatusTask;
import com.bj.job.SendMessageJob;

/**
 * @author LQK
 *
 */
public interface SendMessageService {
	SendMessageJob sendMessage(String name,String message,MultipartFile file,String newFileName,boolean isQuery) throws IOException;
	SendMessageJob sendMessage(String name,String message,MultipartFile file) throws IOException;
	SendMessageJob sendMessage(String name,String message);
	SendMessageJob onlySendMessage(String message);
	AdminStatusTask getAdminStatusTask(UUID taskId);
	LRUMap<UUID, AdminStatusTask> getTaskMap();
}
