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

import com.bj.pojo.DispatchSubTask;
import com.bj.pojo.SplitSubTask;
import com.bj.pojo.SubSystemInfo;

/**
 * Created by LQK on 2018-1-25.
 */
@Mapper
public interface DispatchSubTaskMapper {	
	@Select("SELECT * FROM tbl_file_dispatch_subtask where subtask_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById")),
			@Result(property = "splitSubTask", javaType = SplitSubTask.class, column = "sub_filesplit_task_id", one = @One(select = "com.bj.dao.mapper.SplitSubTaskMapper.findById"))})
	DispatchSubTask findById(@Param("id") int id);
	
    @Select("SELECT * FROM tbl_file_dispatch_subtask " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById")),
			@Result(property = "splitSubTask", javaType = SplitSubTask.class, column = "sub_filesplit_task_id", one = @One(select = "com.bj.dao.mapper.SplitSubTaskMapper.findById"))})
    List<DispatchSubTask> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT * FROM tbl_file_dispatch_subtask " +
            "WHERE dispatch_task_id = #{taskId} ")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystem", javaType = SubSystemInfo.class, column = "sub_sys_id", one = @One(select = "com.bj.dao.mapper.SubSystemMapper.findById")),
			@Result(property = "splitSubTask", javaType = SplitSubTask.class, column = "sub_filesplit_task_id", one = @One(select = "com.bj.dao.mapper.SplitSubTaskMapper.findById"))})
    List<DispatchSubTask> findByTaskId(@Param("taskId") int taskId);
    
    @Insert("INSERT INTO tbl_file_dispatch_subtask " +
            "   (dispatch_task_id, sub_sys_id, sub_filesplit_task_id, status) " +
            "VALUES " +
            "   (#{taskId}, #{subSystem.id}, #{splitSubTask.id}, #{status})")
    @Options(useGeneratedKeys=true,keyColumn="subtask_id")
    int insert(DispatchSubTask dispatchSubTask);
    
    @Insert("<script>" +
            "INSERT INTO tbl_file_dispatch_subtask " +
            "   (dispatch_task_id, sub_sys_id, sub_filesplit_task_id, status) " +
            "VALUES " +
            "<foreach item='subTask' collection='list' separator=','>" +
            "   (#{subTask.taskId}, #{subTask.subSystem.id}, #{subTask.splitSubTask.id}, #{subTask.status}) " +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<DispatchSubTask> dispatchSubTasks);

    @Select("SELECT count(1) FROM tbl_file_dispatch_subtask")
    int countAll();
    
    @Delete("DELETE FROM tbl_file_dispatch_subtask where subtask_id=#{id}")
    int delete(int id);
    
    @Delete("DELETE FROM tbl_file_dispatch_subtask where dispatch_task_id=#{taskId}")
    int deleteByTaskId(int taskId);
}
