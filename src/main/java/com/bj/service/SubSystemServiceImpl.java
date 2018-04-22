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
		return subSystemInfoMapper.insert(subSystemInfo);
	}

	@Override
	public SubSystemInfo findById(int id) {
		return subSystemInfoMapper.findById(id);
	}

	@Override
	public int update(SubSystemInfo subSystemInfo) {
		return subSystemInfoMapper.update(subSystemInfo);
	}

	@Override
	public int detele(int id) {
		return subSystemInfoMapper.delete(id);
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
