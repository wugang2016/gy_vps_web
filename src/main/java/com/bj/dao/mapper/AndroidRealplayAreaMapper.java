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

import com.bj.pojo.AndroidRealplayArea;
import com.bj.pojo.SubSystemInfo;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface AndroidRealplayAreaMapper {	
	@Select("SELECT * FROM tbl_android_realplay_area where area_id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "area_id"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "x", column = "pos_x"),
            @Result(property = "y", column = "pos_y"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById"))})
    AndroidRealplayArea findById(@Param("id") int id);

    @Select("SELECT * FROM tbl_android_realplay_area " +
            "where template_id = #{templateId} ")
    @Results(value = {
            @Result(property = "id", column = "area_id"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "x", column = "pos_x"),
            @Result(property = "y", column = "pos_y"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById"))})
	List<AndroidRealplayArea> findByTemplateId(@Param("templateId") int templateId);
	
    @Select("SELECT * FROM tbl_android_realplay_area order by area_id desc " +
            "LIMIT #{offset}, #{rowCount}")
    @Results(value = {
            @Result(property = "id", column = "area_id"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "x", column = "pos_x"),
            @Result(property = "y", column = "pos_y"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById"))})
    List<AndroidRealplayArea> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);
    
    @Insert("INSERT INTO tbl_android_realplay_area " +
            "   (template_id,pos_x, pos_y, w, h,sub_sys_id) " +
            "VALUES " +
            "   (#{templateId}, #{x}, #{y}, #{w}, #{h}, #{subSystem.id})")
    @Options(useGeneratedKeys=true,keyColumn="area_id")
    int insert(AndroidRealplayArea androidRealplayArea);
    
    @Insert("<script>" +
            "INSERT INTO tbl_android_realplay_area " +
            "   (template_id,pos_x, pos_y, w, h,sub_sys_id) " +
            "VALUES " +
            "<foreach item='androidRealplayAreas' collection='list' separator=','>" +
            "   (#{androidRealplayAreas.templateId}, #{androidRealplayAreas.x}, #{androidRealplayAreas.y}, #{androidRealplayAreas.w}, #{androidRealplayAreas.h}, #{androidRealplayAreas.subSystem.id}) " +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<AndroidRealplayArea> androidRealplayAreas);

    @Update("UPDATE tbl_android_realplay_area t set" +
    		"   t.template_id = #{templateId}, " +
    		"   t.pos_x = #{x}, " +
    		"   t.pos_y = #{y}, " +
    		"   t.w = #{w}, " +
    		"   t.h = #{h}, " +
    		"   t.sub_sys_id = #{subSystem.id} " +
            "   where area_id=#{id}")
    int update(AndroidRealplayArea androidRealplayArea);

    @Select("SELECT count(1) FROM tbl_android_realplay_area")
    int countAll();

    @Delete("DELETE FROM tbl_android_realplay_area where area_id=#{id}")
    int delete(int id);

    @Select("SELECT count(1) FROM tbl_android_realplay_area where sub_sys_id =#{sys_id} and area_id!=#{id}")
	int countBySysIdExcept(@Param("sys_id") int sys_id , @Param("id") int id);

    @Delete("DELETE FROM tbl_android_realplay_area where template_id=#{templateId}")
	int deteleByTemplateId(int templateId);
}
