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

import com.bj.pojo.FileArea;
import com.bj.pojo.SplitSubTask;

/**
 * Created by LQK on 2018-1-25.
 */
@Mapper
public interface SplitSubTaskMapper {	
	@Select("SELECT * FROM tbl_file_split_subtask where subtask_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "fileName", column = "file_name"),
            @Result(property = "filePath", column = "file_path"),
            @Result(property = "errCode", column = "err_code"),
            @Result(property = "fileArea", javaType = FileArea.class, column = "area_id", one = @One(select = "com.bj.dao.mapper.FileAreaMapper.findById"))})
	SplitSubTask findById(@Param("id") int id);
	
    @Select("SELECT * FROM tbl_file_split_subtask order by subtask_id desc " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "fileName", column = "file_name"),
            @Result(property = "filePath", column = "file_path"),
            @Result(property = "errCode", column = "err_code"),
            @Result(property = "fileArea", javaType = FileArea.class, column = "area_id", one = @One(select = "com.bj.dao.mapper.FileAreaMapper.findById"))})
    List<SplitSubTask> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT * FROM tbl_file_split_subtask " +
            "WHERE task_id = #{taskId} ")
    @Results(value = {
            @Result(property = "id", column = "subtask_id"),
            @Result(property = "fileName", column = "file_name"),
            @Result(property = "filePath", column = "file_path"),
            @Result(property = "errCode", column = "err_code"),
            @Result(property = "fileArea", javaType = FileArea.class, column = "area_id", one = @One(select = "com.bj.dao.mapper.FileAreaMapper.findById"))})
    List<SplitSubTask> findByTaskId(@Param("taskId") int taskId);
    
    @Insert("INSERT INTO tbl_file_split_subtask " +
            "   (task_id, file_name, area_id, status) " +
            "VALUES " +
            "   (#{taskId}, #{fileName}, #{fileArea.id}, #{status})")
    @Options(useGeneratedKeys=true,keyColumn="subtask_id")
    int insert(SplitSubTask splitSubTask);

    @Select("SELECT count(1) FROM tbl_file_split_subtask")
    int countAll();
    
    @Delete("DELETE FROM tbl_file_split_subtask where subtask_id=#{id}")
    int delete(int id);
    
    @Delete("DELETE FROM tbl_file_split_subtask where task_id=#{taskId}")
    int deleteByTaskId(int taskId);
}
