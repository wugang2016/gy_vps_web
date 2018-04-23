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

import com.bj.pojo.SplitTask;
import com.bj.pojo.SubSystemInfo;

/**
 * Created by LQK on 2018-1-25.
 */
@Mapper
public interface SplitTaskMapper {	
	@Select("SELECT * FROM tbl_file_split_task where task_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "taskName", column = "task_name"),
            @Result(property = "taskDesc", column = "task_desc"),
            @Result(property = "srcFilePath", column = "src_file_path"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "splitTemplate", javaType = SubSystemInfo.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
	SplitTask findById(@Param("id") int id);
	
    @Select("SELECT * FROM tbl_file_split_task order by task_id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "taskName", column = "task_name"),
            @Result(property = "taskDesc", column = "task_desc"),
            @Result(property = "srcFilePath", column = "src_file_path"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "splitTemplate", javaType = SubSystemInfo.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
    List<SplitTask> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);
    
    @Insert("INSERT INTO tbl_file_split_task " +
            "   (task_name, task_desc, src_file_path, status, start_time, template_id) " +
            "VALUES " +
            "   (#{taskName}, #{taskDesc}, #{srcFilePath}, #{status}, #{startTime}, #{splitTemplate.id})")
    @Options(useGeneratedKeys=true,keyColumn="task_id")
    int insert(SplitTask splitTask);

    @Select("SELECT count(1) FROM tbl_file_split_task")
    int countAll();
    
    @Select("SELECT count(1) FROM tbl_file_split_task where template_id = #{templateId}")
    int countByTemplateId(int templateId);
    
    @Delete("DELETE FROM tbl_file_split_task where task_id=#{id}")
    int delete(int id);
}
