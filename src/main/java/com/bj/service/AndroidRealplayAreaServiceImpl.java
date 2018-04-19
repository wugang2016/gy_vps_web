package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.AndroidRealplayAreaMapper;
import com.bj.pojo.AndroidRealplayArea;

@Service
public class AndroidRealplayAreaServiceImpl implements AndroidRealplayAreaService {
    @Resource
    private AndroidRealplayAreaMapper androidRealplayAreaMapper;

	@Override
	public AndroidRealplayArea findById(int id) {
		return androidRealplayAreaMapper.findById(id);
	}

	@Override
	public List<AndroidRealplayArea> findByTemplateId(int templateId) {
		return androidRealplayAreaMapper.findByTemplateId(templateId);
	}

	@Override
	public List<AndroidRealplayArea> findAll(int offset, int rowCount) {
		return androidRealplayAreaMapper.findAll(offset, rowCount);
	}

	@Override
	public int insert(AndroidRealplayArea androidRealplayArea) {
		return androidRealplayAreaMapper.insert(androidRealplayArea);
	}

	@Override
	public int batchInsert(List<AndroidRealplayArea> androidRealplayAreas) {
		return androidRealplayAreaMapper.batchInsert(androidRealplayAreas);
	}

	@Override
	public int update(AndroidRealplayArea androidRealplayArea) {
		return androidRealplayAreaMapper.update(androidRealplayArea);
	}

	@Override
	public int countAll() {
		return androidRealplayAreaMapper.countAll();
	}

	@Override
	public int delete(int id) {
		return androidRealplayAreaMapper.delete(id);
	}

	@Override
	public int countBySysIdExcept(int sys_id, int id) {
		return androidRealplayAreaMapper.countBySysIdExcept(sys_id, id);
	}

	@Override
	public int deteleByTemplateId(int templateId) {
		return androidRealplayAreaMapper.deteleByTemplateId(templateId);
	}
	
}
