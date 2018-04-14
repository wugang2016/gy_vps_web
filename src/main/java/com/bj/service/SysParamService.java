/**
 * 
 */
package com.bj.service;

import com.bj.pojo.SysParam;

/**
 * @author LQK
 *
 */
public interface SysParamService {
	String findByKey(String key);
	SysParam findSysParamByKey(String key);
	int updateValue(String key, String value);
	boolean validTaskPassword(String taskPassword);
}
