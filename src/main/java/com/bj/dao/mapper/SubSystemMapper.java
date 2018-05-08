package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.SubSystemInfo;

/**
 * Created by liqingkun on 2018-1-25.
 */
@Mapper
public interface SubSystemMapper {
    @Select("SELECT * FROM tbl_sub_sys_info where sub_sys_id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "sub_sys_id"),
            @Result(property = "name", column = "sub_sys_name"),
            @Result(property = "boxIp", column = "box_ip"),
            @Result(property = "picPath", column = "pic_path")})
    SubSystemInfo findById(@Param("id") int id);
    
    @Select("SELECT * FROM tbl_sub_sys_info order by sub_sys_id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "sub_sys_id"),
            @Result(property = "name", column = "sub_sys_name"),
            @Result(property = "boxIp", column = "box_ip"),
            @Result(property = "picPath", column = "pic_path")})
    List<SubSystemInfo> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);
    
    @Select("SELECT * FROM tbl_sub_sys_info where ip=#{ip}" )
    @Results(value = {
            @Result(property = "id", column = "sub_sys_id"),
            @Result(property = "name", column = "sub_sys_name"),
            @Result(property = "boxIp", column = "box_ip")})
    List<SubSystemInfo> findByIp(@Param("ip") String ip);
    
    @Insert("INSERT INTO tbl_sub_sys_info " +
            "   (sub_sys_name, ip, port, box_ip, width, height, longitude, latitude, pic_path, content_width, content_height) " +
            "VALUES " +
            "   (#{name}, #{ip}, #{port}, #{boxIp}, #{width}, #{height}, #{longitude}, #{latitude}, #{picPath}, #{content_width}, #{content_height})")
    @Options(useGeneratedKeys=true,keyColumn="sub_sys_id")
    int insert(SubSystemInfo subSystemInfo);

    @Update("UPDATE tbl_sub_sys_info t set" +
    		"   t.sub_sys_name = #{name}, " +
    		"   t.ip = #{ip}, " +
    		"   t.port = #{port}, " +
    		"   t.box_ip = #{boxIp}, " +
    		"   t.width = #{width}, " +
    		"   t.height = #{height}," +
    		"   t.content_width = #{content_width}, " +
    		"   t.content_height = #{content_height}," +
    		"   t.longitude = #{longitude}," +
    		"   t.latitude = #{latitude}," +
    		"   t.pic_path = #{picPath}" +
            "   where sub_sys_id=#{id}")
    int update(SubSystemInfo subSystemInfo);

    @Select("SELECT count(1) FROM tbl_sub_sys_info where ip=#{ip}")
    int countByIp(@Param("ip") String ip);
    
    @Select("SELECT count(1) FROM tbl_sub_sys_info where box_ip=#{ip}")
    int countByBoxIp(@Param("ip") String ip);
    
    @Select("SELECT count(1) FROM tbl_sub_sys_info where ip=#{ip} and sub_sys_id!=#{id}")
    int countByIpExcept(@Param("ip") String ip , @Param("id") int id);
    
    @Select("SELECT count(1) FROM tbl_sub_sys_info")
    int countAll();
    
    @Delete("DELETE FROM tbl_sub_sys_info where sub_sys_id=#{id}")
    int delete(int id);

    @Select("SELECT count(1) FROM tbl_sub_sys_info where box_ip=#{ip} and sub_sys_id!=#{id}")
	int countByBoxIpExcept(@Param("ip") String ip, @Param("id") int id);
}
