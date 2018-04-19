package com.bj.service;

import java.util.List;

import com.bj.pojo.DispatchTask;

public interface DispatchTaskService {
	DispatchTask findById(int id);
    List<DispatchTask> findAll(int offset, int rowCount);
    int insert(DispatchTask dispatchTask);
    int update(DispatchTask dispatchTask);
    int countAll();
    int delete(int id);
    int countBySplitTaskId(int splitTaskId);
}
