package com.bj.service;

import java.util.List;

import com.bj.pojo.AndroidRealplayArea;

public interface AndroidRealplayAreaService {
	AndroidRealplayArea findById(int id);
	List<AndroidRealplayArea> findByTemplateId(int templateId);
    List<AndroidRealplayArea> findAll(int offset,int rowCount);
    int insert(AndroidRealplayArea androidRealplayArea);
    int batchInsert(List<AndroidRealplayArea> androidRealplayAreas);
    int update(AndroidRealplayArea androidRealplayArea);
    int countAll();
    int delete(int id);
	int countBySysIdExcept(int sys_id, int id);
	int deteleByTemplateId(int templateId);
}
