package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.AndroidRealplayTemplateMapper;
import com.bj.pojo.AndroidRealplayTemplate;

@Service
public class AndroidRealplayTemplateServiceImpl implements AndroidRealplayTemplateService {
	
    @Resource
    private AndroidRealplayTemplateMapper androidRealplayTemplateMapper;

	@Override
	public AndroidRealplayTemplate findById(int id) {
		return androidRealplayTemplateMapper.findById(id);
	}

	@Override
	public List<AndroidRealplayTemplate> findAll(int offset, int rowCount) {
		return androidRealplayTemplateMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(AndroidRealplayTemplate androidRealplayTemplate) {
		return androidRealplayTemplateMapper.insert(androidRealplayTemplate);
	}

	@Override
	public int update(AndroidRealplayTemplate androidRealplayTemplate) {
		return androidRealplayTemplateMapper.update(androidRealplayTemplate);
	}

	@Override
	public int countAll() {
		return androidRealplayTemplateMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return androidRealplayTemplateMapper.delete(id);
	}
	
}
