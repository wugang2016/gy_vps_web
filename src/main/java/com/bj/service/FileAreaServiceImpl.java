/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.FileAreaMapper;
import com.bj.pojo.FileArea;

/**
 * @author LQK
 *
 */
@Service
public class FileAreaServiceImpl implements FileAreaService {
    
    @Resource
    private FileAreaMapper fileAreaMapper;
	
	@Override
	public List<FileArea> findAll(int offset,  int rowCount) {
		return fileAreaMapper.findAll(offset, rowCount);
	}

	@Override
	public int countAll() {
		return fileAreaMapper.countAll();
	}

	@Override
	public int insert(FileArea fileArea) {
		return fileAreaMapper.insert(fileArea);
	}

	@Override
	public FileArea findById(int id) {
		return fileAreaMapper.findById(id);
	}

	@Override
	public int update(FileArea fileArea) {
		return fileAreaMapper.update(fileArea);
	}

	@Override
	public int detele(int id) {
		return fileAreaMapper.delete(id);
	}

	@Override
	public int countBySysId(int sys_id) {
		return fileAreaMapper.countBySysId(sys_id);
	}

	@Override
	public int countBySysIdExcept(int sys_id, int id) {
		return fileAreaMapper.countBySysIdExcept(sys_id,id);
	}

	@Override
	public int deteleByTemplateId(int templateId) {
		return fileAreaMapper.deteleByTemplateId(templateId);
	}

	@Override
	public int batchInsert(List<FileArea> fileAreas) {
		return fileAreaMapper.batchInsert(fileAreas);
	}

	@Override
	public List<FileArea> findByTemplateId(int templateId) {
		return fileAreaMapper.findByTemplateId(templateId);
	}

}
