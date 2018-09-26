/**
 * 
 */
package com.bj.service;

import java.util.List;

import com.bj.pojo.FileResource;
import com.bj.pojo.SubSystemInfo;

/**
 * @author LQK
 *
 */
public interface FileResourceService {
	FileResource findById(int id);
    List<FileResource> findAll(int offset, int rowCount);
    int countAll();
    int delete(int id);
    int deleteByTaskId(int taskId);
    int insert(FileResource fileResource);
    int update(FileResource fileResource);
}
