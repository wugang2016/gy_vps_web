/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.RealplayTaskMapper;
import com.bj.pojo.FileResource;
import com.bj.pojo.RealplayTask;

/**
 * @author LQK
 *
 */
@Service
public class RealplayTaskServiceImpl implements RealplayTaskService {

    @Resource
    private RealplayTaskMapper realplayTaskMapper;

	@Override
	public RealplayTask findById(int id) {
		return realplayTaskMapper.findById(id);
	}

	@Override
	public List<RealplayTask> findAll(int offset, int rowCount) {
		return realplayTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(RealplayTask realplayTask) {
		return realplayTaskMapper.insert(realplayTask);
	}

	@Override
	public int update(RealplayTask realplayTask) {
		return realplayTaskMapper.update(realplayTask);
	}

	@Override
	public int countAll() {
		return realplayTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return realplayTaskMapper.deleteFileResourceByTaskId(id) * realplayTaskMapper.delete(id);
	}

	@Override
	public int insertFileResource(FileResource fileResource) {
		return realplayTaskMapper.insertFileResource(fileResource);
	}

	@Override
	public FileResource findFileResourceById(int id) {
		return realplayTaskMapper.findFileResourceById(id);
	}

	@Override
	public int countByTemplateId(int templateId) {
		return realplayTaskMapper.countByTemplateId(templateId);
	}
	
}
