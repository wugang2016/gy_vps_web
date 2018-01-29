/**
 * 
 */
package com.bj.service;

import java.util.List;

import com.bj.pojo.RealtimeArea;

/**
 * @author LQK
 *
 */
public interface RealtimeAreaService {
	List<RealtimeArea> findAll(int offset,  int rowCount);
	int countAll();
	int insert(RealtimeArea realtimeArea);
	int update(RealtimeArea realtimeArea);
	int detele(int id);
	RealtimeArea findById(int id);
	int countBySysId(int sys_id);
	int countBySysIdExcept(int sys_id,int id);
}
