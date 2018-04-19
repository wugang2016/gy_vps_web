package com.bj.service;

import java.util.List;

import com.bj.pojo.DispatchSubTask;

public interface DispatchSubTaskService {
	DispatchSubTask findById(int id);
    List<DispatchSubTask> findAll(int offset, int rowCount);
    List<DispatchSubTask> findByTaskId(int taskId);
    int insert(DispatchSubTask dispatchSubTask);
    int batchInsert(List<DispatchSubTask> dispatchSubTasks);
    int countAll();
    int delete(int id);
}
