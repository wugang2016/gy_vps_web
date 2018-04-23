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

import com.bj.pojo.DispatchTask;
import com.bj.pojo.SplitTask;

/**
 * Created by LQK on 2018-1-25.
 */
@Mapper
public interface DispatchTaskMapper {	
	@Select("SELECT * FROM tbl_file_dispatch_task where task_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "taskName", column = "task_name"),
            @Result(property = "taskDesc", column = "task_desc"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "splitTask", javaType = SplitTask.class, column = "split_task_id", one = @One(select = "com.bj.dao.mapper.SplitTaskMapper.findById"))})
	DispatchTask findById(@Param("id") int id);
	
    @Select("SELECT * FROM tbl_file_dispatch_task order by task_id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "taskName", column = "task_name"),
            @Result(property = "taskDesc", column = "task_desc"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "splitTask", javaType = SplitTask.class, column = "split_task_id", one = @One(select = "com.bj.dao.mapper.SplitTaskMapper.findById"))})
    List<DispatchTask> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

	@Select("SELECT count(1) FROM tbl_file_dispatch_task where split_task_id = #{splitTaskId}")
    int countBySplitTaskId(int splitTaskId);
    
    @Insert("INSERT INTO tbl_file_dispatch_task " +
            "   (task_name, task_desc, status, start_time, split_task_id) " +
            "VALUES " +
            "   (#{taskName}, #{taskDesc}, #{status}, #{startTime}, #{splitTask.id})")
    @Options(useGeneratedKeys=true,keyColumn="task_id")
    int insert(DispatchTask dispatchTask);

    @Update("UPDATE tbl_file_dispatch_task t set" +
    		"   t.task_name = #{taskName}, " +
    		"   t.task_desc = #{taskDesc}, " +
    		"   t.template_id = #{splitTask.id}, " +
            "   where task_id= #{id}")
    int update(DispatchTask dispatchTask);

    @Select("SELECT count(1) FROM tbl_file_dispatch_task")
    int countAll();
    
    @Delete("DELETE FROM tbl_file_dispatch_task where task_id=#{id}")
    int delete(int id);
}
