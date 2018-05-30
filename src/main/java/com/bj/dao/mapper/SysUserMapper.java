package com.bj.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.SysUser;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface SysUserMapper {	
	@Select("SELECT * FROM tbl_user where username=#{username}")
    SysUser findByUsername(@Param("username") String username);

    @Update("UPDATE tbl_user t set" +
    		"   t.password = #{password} " +
            "   where username='admin'")
	int updatePassword(@Param("password") String password);
}
