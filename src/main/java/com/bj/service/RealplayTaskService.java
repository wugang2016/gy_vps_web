package com.bj.service;

import java.util.List;

import com.bj.pojo.FileResource;
import com.bj.pojo.RealplayTask;

public interface RealplayTaskService {
	RealplayTask findById(int id);
    List<RealplayTask> findAll(int offset, int rowCount);
    int insert(RealplayTask realplayTask);
    int update(RealplayTask realplayTask);
    int countAll();
    int delete(int id);
    int insertFileResource(FileResource fileResource);
    FileResource findFileResourceById(int id);
    int countByTemplateId(int templateId);
}
