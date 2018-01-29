/**
 * 
 */
package com.bj.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bj.job.AdminStatusTask;
import com.bj.job.SendMessageJob;

/**
 * @author LQK
 *
 */
public interface SendMessageService {
	SendMessageJob sendMessage(String message,MultipartFile file, boolean isQuery);
	SendMessageJob sendMessage(String message,MultipartFile file);
	SendMessageJob sendMessage(String message);
	AdminStatusTask getAdminStatusTask(UUID taskId);
}
