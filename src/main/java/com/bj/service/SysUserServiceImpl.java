/**
 * 
 */
package com.bj.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bj.dao.mapper.SysUserMapper;
import com.bj.pojo.SysUser;

/**
 * @author LQK
 *
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

	@Override
	public SysUser findByUsername(String username) {
		return sysUserMapper.findByUsername(username);
	}

	@Override
	public void updatePassword(String password) {
		sysUserMapper.updatePassword(password);
	}

	@Override
	public void updateDoPassword(String doPassword) {
		sysUserMapper.updateDoPassword(doPassword);
	}

}
