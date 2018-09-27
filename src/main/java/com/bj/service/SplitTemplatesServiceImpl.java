/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SplitTemplatesMapper;
import com.bj.pojo.SplitTemplates;

/**
 * @author LQK
 *
 */
@Service
public class SplitTemplatesServiceImpl implements SplitTemplatesService {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SplitTemplatesServiceImpl.class);

    @Resource
    private SplitTemplatesMapper splitTemplatesMapper;
	
	@Override
	public List<SplitTemplates> findAll(int offset,  int rowCount) {
		return splitTemplatesMapper.findAll(offset, rowCount);
	}

	@Override
	public int countAll() {
		return splitTemplatesMapper.countAll();
	}

	@Override
	public int insert(SplitTemplates splitTemplates) {
		int result = splitTemplatesMapper.insert(splitTemplates);
		return result;
	}

	@Override
	public SplitTemplates findById(int id) {
		return splitTemplatesMapper.findById(id);
	}

	@Override
	public int update(SplitTemplates splitTemplates) {
		return splitTemplatesMapper.update(splitTemplates);
	}

	@Override
	public int delete(int id) {
		return splitTemplatesMapper.delete(id);
	}

	@Override
	public List<SplitTemplates> findDefaultTemplatesByType(int type) {
		return splitTemplatesMapper.findDefaultTemplatesByType(type);
	}

	@Override
	public List<SplitTemplates> findAllNotDefault(int offset, int rowCount) {
		// TODO Auto-generated method stub
		return splitTemplatesMapper.findAllNotDefault(offset,rowCount);
	}

	@Override
	public int countAllNotDefault() {
		// TODO Auto-generated method stub
		return splitTemplatesMapper.countAllNotDefault();
	}
}
