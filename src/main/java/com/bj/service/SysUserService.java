/**
 * 
 */
package com.bj.service;

import com.bj.pojo.SysUser;

/**
 * @author LQK
 *
 */
public interface SysUserService {
	SysUser findByUsername(String username);
	void updatePassword(String password);
}
