/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.RealtimeAreaMapper;
import com.bj.pojo.RealtimeArea;

/**
 * @author LQK
 *
 */
@Service
public class RealtimeAreaServiceImpl implements RealtimeAreaService {

    @Resource
    private SendMessageService sendMessageService;
    
    @Resource
    private RealtimeAreaMapper realtimeAreaMapper;
	
	@Override
	public List<RealtimeArea> findAll(int offset,  int rowCount) {
		return realtimeAreaMapper.findAll(offset, rowCount);
	}

	@Override
	public int countAll() {
		return realtimeAreaMapper.countAll();
	}

	@Override
	public int insert(RealtimeArea realtimeArea) {
		int result = realtimeAreaMapper.insert(realtimeArea);
		if(result > 0){
			sendMessageService.onlySendMessage(realtimeArea.format("add"));
		}
		return result;
	}

	@Override
	public RealtimeArea findById(int id) {
		return realtimeAreaMapper.findById(id);
	}

	@Override
	public int update(RealtimeArea realtimeArea) {
		int result = realtimeAreaMapper.update(realtimeArea);
		if(result > 0){
			sendMessageService.onlySendMessage(realtimeArea.format("mod"));
		}
		return result;
	}

	@Override
	public int detele(int id) {
		int result = realtimeAreaMapper.delete(id);
		if(result > 0){
			sendMessageService.onlySendMessage("{\"opt\":\"rmv\",\"tbl_name\":\"tbl_realtime_area\",\"value\":{\"area_id\":" + id + "}}");
		}
		return result;
	}

	@Override
	public int countBySysId(int sys_id) {
		return realtimeAreaMapper.countBySysId(sys_id);
	}

	@Override
	public int countBySysIdExcept(int sys_id, int id) {
		return realtimeAreaMapper.countBySysIdExcept(sys_id,id);
	}

}
