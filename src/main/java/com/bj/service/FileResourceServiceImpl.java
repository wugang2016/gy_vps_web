/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.FileResourceMapper;
import com.bj.pojo.FileResource;

/**
 * @author LQK
 *
 */
@Service
public class FileResourceServiceImpl implements FileResourceService {
    
    @Resource
    private FileResourceMapper fileResourceMapper;

	@Override
	public FileResource findById(int id) {
		return fileResourceMapper.findById(id);
	}

	@Override
	public List<FileResource> findAll(int offset, int rowCount) {
		return fileResourceMapper.findAll(offset, rowCount);
	}

	@Override
	public int countAll() {
		return fileResourceMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return fileResourceMapper.delete(id);
	}

	@Override
	public int deleteByTaskId(int taskId) {
		return fileResourceMapper.deleteByTaskId(taskId) ;
	}

	@Override
	public int insert(FileResource fileResource) {
		return fileResourceMapper.insert(fileResource);
	}
	
}
