package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.FileArea;
import com.bj.pojo.SubSystemInfo;

/**
 * Created by LQK on 2018-1-25.
 */
@Mapper
public interface FileAreaMapper {	
	@Select("SELECT * FROM tbl_file_area where area_id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "area_id"),
            @Result(property = "x", column = "pos_x"),
            @Result(property = "y", column = "pos_y"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById"))})
    FileArea findById(@Param("id") int id);
    
    @Select("SELECT * FROM tbl_file_area " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "area_id"),
            @Result(property = "x", column = "pos_x"),
            @Result(property = "y", column = "pos_y"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById"))})
    List<FileArea> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Insert("INSERT INTO tbl_file_area " +
            "   (pos_x, pos_y, width, height,sub_sys_id) " +
            "VALUES " +
            "   (#{x}, #{y}, #{width}, #{height}, #{subSystem.id})")
    @Options(useGeneratedKeys=true,keyColumn="area_id")
    int insert(FileArea fileArea);

    @Update("UPDATE tbl_file_area t set" +
    		"   t.pos_x = #{x}, " +
    		"   t.pos_y = #{y}, " +
    		"   t.width = #{width}, " +
    		"   t.height = #{height}, " +
    		"   t.sub_sys_id = #{subSystem.id} " +
            "   where area_id=#{id}")
    int update(FileArea fileArea);

    @Select("SELECT count(1) FROM tbl_file_area")
    int countAll();
    
    @Select("SELECT count(1) FROM tbl_file_area where sub_sys_id =#{sys_id}")
    int countBySysId(int sys_id);

    @Delete("DELETE FROM tbl_file_area where area_id=#{id}")
    int delete(int id);

    @Select("SELECT count(1) FROM tbl_file_area where sub_sys_id =#{sys_id} and area_id!=#{id}")
	int countBySysIdExcept(@Param("sys_id") int sys_id , @Param("id") int id);
}
