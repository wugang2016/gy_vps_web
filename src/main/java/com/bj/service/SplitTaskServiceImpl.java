/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SplitSubTaskMapper;
import com.bj.dao.mapper.SplitTaskMapper;
import com.bj.pojo.SplitTask;

/**
 * @author LQK
 *
 */
@Service
public class SplitTaskServiceImpl implements SplitTaskService {

    @Resource
    private SplitTaskMapper splitTaskMapper;
    
    @Resource
    private SplitSubTaskMapper splitSubTaskMapper;

	@Override
	public SplitTask findById(int id) {
		return splitTaskMapper.findById(id);
	}

	@Override
	public List<SplitTask> findAll(int offset, int rowCount) {
		return splitTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(SplitTask splitTask) {
		return splitTaskMapper.insert(splitTask);
	}

	@Override
	public int countAll() {
		return splitTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return splitSubTaskMapper.deleteByTaskId(id) * splitTaskMapper.delete(id);
	}

	@Override
	public int countByTemplateId(int templateId) {
		return splitTaskMapper.countByTemplateId(templateId);
	}
    

}
