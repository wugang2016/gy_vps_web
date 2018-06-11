/**
 * 
 */
package com.bj.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.dao.mapper.SubSystemMapper;
import com.bj.job.JobException;
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
		if(subSystemInfo.getContent_width() == null) {
			subSystemInfo.setContent_width(subSystemInfo.getWidth());
		}
		if(subSystemInfo.getContent_height() == null) {
			subSystemInfo.setContent_height(subSystemInfo.getHeight());
		}
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

	@Override
	public int countByBoxIp(String ip) {
		return subSystemInfoMapper.countByBoxIp(ip);
	}

	@Override
	public int countByBoxIpExcept(String ip, int id) {
		return subSystemInfoMapper.countByBoxIpExcept(ip,id);
	}

	@Override
	@Transactional
	public void checkAndBatchInsert(List<SubSystemInfo> subs) {
		if(subs != null) {
			for(int i=0; i<subs.size(); i++) {
				SubSystemInfo subSystemInfo = subs.get(i);
				String result = checkData(subSystemInfo);
				if(result == null) {
					this.insertNoSendMsg(subSystemInfo);
				}else {
					throw new JobException(result);
				}
			}
			batchSendMsg(subs);
		}
	}
	
	private int insertNoSendMsg(SubSystemInfo subSystemInfo) {
		if(subSystemInfo.getContent_width() == null) {
			subSystemInfo.setContent_width(subSystemInfo.getWidth());
		}
		if(subSystemInfo.getContent_height() == null) {
			subSystemInfo.setContent_height(subSystemInfo.getHeight());
		}
		return subSystemInfoMapper.insert(subSystemInfo);
	}
	
	private void batchSendMsg(List<SubSystemInfo> subs) {
		if(subs != null) {
			for(int i=0; i<subs.size(); i++) {
				SubSystemInfo subSystemInfo = subs.get(i);
				sendMessageService.onlySendMessage(subSystemInfo.format("add"));
			}
		}
	}
	/**
     * 
     * @param subSystemInfo
     * @return
     */
    private String checkData(SubSystemInfo subSystem) {
    	if(!subSystem.getBoxIp().isEmpty()) {
    		if(this.countByIp(subSystem.getBoxIp()) > 0) {
    			return "导入失败，["+subSystem.getBoxIp()+"]BOX IP地址与其它系统冲突！";
        	}else if(this.countByBoxIp(subSystem.getBoxIp()) > 0){
        		return "导入失败，["+subSystem.getBoxIp()+"]BOX IP地址与其它系统BOX IP地址冲突！";
        	}
    	}
    	if(this.countByIp(subSystem.getIp()) > 0) {
    		return "导入失败，["+subSystem.getIp()+"]IP地址与其它系统冲突！";
    	}else if(this.countByBoxIp(subSystem.getIp()) > 0){
    		return "导入失败，["+subSystem.getIp()+"]IP地址与其它系统BOX IP地址冲突！";
    	}
    	return null;
    }
}
