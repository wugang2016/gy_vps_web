package com.bj.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.SysParam;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface SysParamMapper {	
	@Select("SELECT * FROM tbl_sys_param where `key` = #{key}")
	SysParam findByKey(@Param("key") String key);

    @Update("UPDATE tbl_sys_param set" +
    		"   value = #{value} " +
            "   where `key` = #{key}")
	int updateValue(@Param("key") String key, @Param("value") String value);
    
    @Insert("INSERT INTO tbl_sys_param " +
            "   (key,value) " +
            "VALUES " +
            "   (#{key}, #{value})")
    int insert(@Param("key") String key, @Param("value") String value);
}
