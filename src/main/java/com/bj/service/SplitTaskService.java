package com.bj.service;

import java.util.List;

import com.bj.pojo.SplitTask;

public interface SplitTaskService {
	SplitTask findById( int id);
    List<SplitTask> findAll(int offset, int rowCount);
    int insert(SplitTask splitTask);
    int update(SplitTask splitTask);
    int countAll();
    int delete(int id);
}
