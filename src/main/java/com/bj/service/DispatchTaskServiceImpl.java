/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.DispatchSubTaskMapper;
import com.bj.dao.mapper.DispatchTaskMapper;
import com.bj.pojo.DispatchTask;

/**
 * @author LQK
 *
 */
@Service
public class DispatchTaskServiceImpl implements DispatchTaskService {

    @Resource
    private DispatchTaskMapper dispatchTaskMapper;
    
    @Resource
    private DispatchSubTaskMapper dispatchSubTaskMapper;
    
    @Resource
    private JobService sendMessageService;

	@Override
	public DispatchTask findById(int id) {
		return dispatchTaskMapper.findById(id);
	}

	@Override
	public List<DispatchTask> findAll(int offset, int rowCount) {
		return dispatchTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(DispatchTask dispatchTask) {
		int result = dispatchTaskMapper.insert(dispatchTask);
		if(result > 0){
			sendMessageService.onlySendMessage(dispatchTask.format());
		}
		return result;
	}

	@Override
	public int update(DispatchTask dispatchTask) {
		return dispatchTaskMapper.update(dispatchTask);
	}

	@Override
	public int countAll() {
		return dispatchTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return dispatchSubTaskMapper.deleteByTaskId(id) * dispatchTaskMapper.delete(id);
	}

	@Override
	public int countBySplitTaskId(int splitTaskId) {
		return dispatchTaskMapper.countBySplitTaskId(splitTaskId);
	}
	
}
