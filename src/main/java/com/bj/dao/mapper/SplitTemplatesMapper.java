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

import com.bj.pojo.SplitTemplates;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface SplitTemplatesMapper {	
	@Select("SELECT * FROM tbl_file_split_template where template_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "template_id")})
    SplitTemplates findById(@Param("id") int id);
    
    @Select("SELECT * FROM tbl_file_split_template where type =  #{type} ")
    @Results(value = {
            @Result(property = "id", column = "template_id")})
    List<SplitTemplates> findDefaultTemplatesByType(@Param("type") int type);
    
    @Select("SELECT * FROM tbl_file_split_template where type = 0 order by template_id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "template_id")})
    List<SplitTemplates> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM tbl_file_split_template where type = 0")
    int countAll();

    @Insert("INSERT INTO tbl_file_split_template " +
            "   (name, `desc`, type) " +
            "VALUES " +
            "   (#{name}, #{desc}, #{type})")
    @Options(useGeneratedKeys=true,keyColumn="template_id",keyProperty="id")
    int insert(SplitTemplates splitTemplates);

    @Update("UPDATE tbl_file_split_template t set" +
    		"   t.name = #{name}, " +
    		"   t.desc = #{desc}, " +
    		"   t.type = #{type}  " +
    		"   where t.template_id = #{id}")
    int update(SplitTemplates splitTemplates);
    
    @Delete("DELETE FROM tbl_file_split_template where template_id=#{id}")
    int delete(int id);

    @Select("SELECT count(1) FROM tbl_file_split_template where sub_sys_id =#{sys_id} and template_id!=#{id}")
	int countBySysIdExcept(@Param("sys_id") int sys_id , @Param("id") int id);
}
