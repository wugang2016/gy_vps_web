/**
 * 
 */
package com.bj.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SysParamMapper;
import com.bj.pojo.SysParam;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;

/**
 * @author LQK
 *
 */
@Service
public class SysParamServiceImpl implements SysParamService{
    @Resource
    private SysParamMapper sysParamMapper;

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
		return sysParamMapper.updateValue(key, value);
	}

	@Override
	public boolean validTaskPassword(String taskPassword){
		return BaseUtil.md5(taskPassword).equals(findByKey(Contants.KEY_TASK_PASSWORD));
	}

}
