/**
 * 
 */
package com.bj.service;

import java.util.List;

import com.bj.pojo.SubSystemInfo;

/**
 * @author LQK
 *
 */
public interface SubSystemService {
	List<SubSystemInfo> findAll(int offset,  int rowCount);
	int countAll();
	int countByIp(String ip);
	int countByBoxIp(String ip);
	int countByIpExcept(String ip,int id);
	int countByBoxIpExcept(String ip,int id);
	int insert(SubSystemInfo subSystemInfo);
	int update(SubSystemInfo subSystemInfo);
	int detele(int id);
	void checkAndBatchInsert(List<SubSystemInfo> subs);
	SubSystemInfo findById(int id);
}
