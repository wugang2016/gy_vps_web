/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
	public int update(SplitTask splitTask) {
		return splitTaskMapper.update(splitTask);
	}

	@Override
	public int countAll() {
		return splitTaskMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return splitTaskMapper.delete(id);
	}
    

}
