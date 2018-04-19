/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.DispatchSubTaskMapper;
import com.bj.pojo.DispatchSubTask;

/**
 * @author LQK
 *
 */
@Service
public class DispatchSubTaskServiceImpl implements DispatchSubTaskService {

    @Resource
    private DispatchSubTaskMapper dispatchSubTaskMapper;

	@Override
	public DispatchSubTask findById(int id) {
		return dispatchSubTaskMapper.findById(id);
	}

	@Override
	public List<DispatchSubTask> findAll(int offset, int rowCount) {
		return dispatchSubTaskMapper.findAll(offset, rowCount);
	}

	@Override
	public List<DispatchSubTask> findByTaskId(int taskId) {
		return dispatchSubTaskMapper.findByTaskId(taskId);
	}

	@Override
	public int insert(DispatchSubTask dispatchSubTask) {
		return dispatchSubTaskMapper.insert(dispatchSubTask);
	}

	@Override
	public int batchInsert(List<DispatchSubTask> dispatchSubTasks) {
		return dispatchSubTaskMapper.batchInsert(dispatchSubTasks);
	}

	@Override
	public int countAll() {
		return dispatchSubTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return dispatchSubTaskMapper.delete(id);
	}

}
