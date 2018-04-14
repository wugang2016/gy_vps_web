/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SplitSubTaskMapper;
import com.bj.pojo.SplitSubTask;

/**
 * @author LQK
 *
 */
@Service
public class SplitSubTaskServiceImpl implements SplitSubTaskService {

    @Resource
    private SplitSubTaskMapper splitSubTaskMapper;

	@Override
	public SplitSubTask findById(int id) {
		return splitSubTaskMapper.findById(id);
	}

	@Override
	public List<SplitSubTask> findAll(int offset, int rowCount) {
		return splitSubTaskMapper.findAll(offset,rowCount);
	}

	@Override
	public int insert(SplitSubTask SplitSubTask) {
		return splitSubTaskMapper.insert(SplitSubTask);
	}

	@Override
	public int countAll() {
		return splitSubTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return splitSubTaskMapper.delete(id);
	}

	@Override
	public int batchInsert(List<SplitSubTask> splitSubTasks) {
		return splitSubTaskMapper.batchInsert(splitSubTasks);
	}

	@Override
	public List<SplitSubTask> findByTaskId(int taskId) {
		return splitSubTaskMapper.findByTaskId(taskId);
	}

}
