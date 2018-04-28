/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SubSystemMapper;
import com.bj.pojo.SubSystemInfo;

/**
 * @author LQK
 *
 */
@Service
public class SubSystemServiceImpl implements SubSystemService {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SubSystemServiceImpl.class);
    
    @Resource
    private SubSystemMapper subSystemInfoMapper;
    
    @Resource
    private JobService sendMessageService;
	
	@Override
	public List<SubSystemInfo> findAll(int offset,  int rowCount) {
		return subSystemInfoMapper.findAll(offset, rowCount);
	}

	@Override
	public int countAll() {
		return subSystemInfoMapper.countAll();
	}

	@Override
	public int insert(SubSystemInfo subSystemInfo) {
		int result = subSystemInfoMapper.insert(subSystemInfo);
		if(result > 0) {
			sendMessageService.onlySendMessage(subSystemInfo.format("add"));
		}
		return result;
	}

	@Override
	public SubSystemInfo findById(int id) {
		return subSystemInfoMapper.findById(id);
	}

	@Override
	public int update(SubSystemInfo subSystemInfo) {
		int result = subSystemInfoMapper.update(subSystemInfo);
		if(result > 0){
			sendMessageService.onlySendMessage(subSystemInfo.format("mod"));
		}
		return result;
	}

	@Override
	public int detele(int id) {
		int result = subSystemInfoMapper.delete(id);
		if(result > 0){
			sendMessageService.onlySendMessage("{\"opt\":\"rmv\",\"tbl_name\":\"tbl_sub_sys_info\",\"value\":{\"sub_sys_id\":" + id + "}}");
		}
		return result;
	}

	@Override
	public int countByIp(String ip) {
		return subSystemInfoMapper.countByIp(ip);
	}

	@Override
	public int countByIpExcept(String ip, int id) {
		return subSystemInfoMapper.countByIpExcept(ip,id);
	}
}
