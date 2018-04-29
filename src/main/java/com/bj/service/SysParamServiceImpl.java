/**
 * 
 */
package com.bj.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SysParamMapper;
import com.bj.pojo.SysParam;
import com.bj.util.Contants;

/**
 * @author LQK
 *
 */
@Service
public class SysParamServiceImpl implements SysParamService{
    @Resource
    private SysParamMapper sysParamMapper;
    
    @Resource
    private JobService sendMessageService;
    
	@Override
	public String findByKey(String key) {
		SysParam sysParam = this.findSysParamByKey(key);
		if(sysParam != null) {
			return sysParam.getValue();
		}
		return "";
	}

	@Override
	public SysParam findSysParamByKey(String key) {
		return sysParamMapper.findByKey(key);
	}

	@Override
	public int updateValue(String key, String value) {
		int result = sysParamMapper.updateValue(key, value);
		if(result > 0) {
			sendMessageService.onlySendMessage("{\"opt\":\"mod\",\"tbl_name\":\"tbl_sys_param\",\"value\":{\"key\":\"" + key + "\",\"value\":\"" + value + "\"}}");
		}
		return result;
	}

	@Override
	public boolean validTaskPassword(String taskPassword){
		return taskPassword.equals(findByKey(Contants.KEY_TASK_PASSWORD));
	}

}
