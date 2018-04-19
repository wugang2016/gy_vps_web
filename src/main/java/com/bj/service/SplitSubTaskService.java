package com.bj.service;

import java.util.List;

import com.bj.pojo.SplitSubTask;

public interface SplitSubTaskService {
	SplitSubTask findById(int id);
    List<SplitSubTask> findAll(int offset,int rowCount);
    int insert(SplitSubTask SplitSubTask);
    int countAll();
    int delete(int id);
    List<SplitSubTask> findByTaskId(int taskId);
}
