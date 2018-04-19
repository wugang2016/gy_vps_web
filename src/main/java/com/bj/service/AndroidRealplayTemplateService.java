package com.bj.service;

import java.util.List;

import com.bj.pojo.AndroidRealplayTemplate;

public interface AndroidRealplayTemplateService {
	AndroidRealplayTemplate findById(int id);
    List<AndroidRealplayTemplate> findAll(int offset, int rowCount);
    int insert(AndroidRealplayTemplate androidRealplayTemplate);
    int update(AndroidRealplayTemplate androidRealplayTemplate);
    int countAll();
    int delete(int id);
}
