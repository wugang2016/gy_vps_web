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
    private SendMessageService sendMessageService;
    
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
		int result = fileAreaMapper.insert(fileArea);
		if(result > 0){
			sendMessageService.onlySendMessage(fileArea.format("add"));
		}
		return result;
	}

	@Override
	public FileArea findById(int id) {
		return fileAreaMapper.findById(id);
	}

	@Override
	public int update(FileArea fileArea) {
		int result = fileAreaMapper.update(fileArea);
		if(result > 0){
			sendMessageService.onlySendMessage(fileArea.format("mod"));
		}
		return result;
	}

	@Override
	public int detele(int id) {
		int result = fileAreaMapper.delete(id);
		if(result > 0){
			sendMessageService.onlySendMessage("{\"opt\":\"rmv\",\"tbl_name\":\"tbl_file_area\",\"value\":{\"area_id\":" + id + "}}");
		}
		return result;
	}

	@Override
	public int countBySysId(int sys_id) {
		return fileAreaMapper.countBySysId(sys_id);
	}

	@Override
	public int countBySysIdExcept(int sys_id, int id) {
		return fileAreaMapper.countBySysIdExcept(sys_id,id);
	}

}
