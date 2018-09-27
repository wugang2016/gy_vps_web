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

import com.bj.pojo.FileResource;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface FileResourceMapper {    
	@Select("SELECT * FROM tbl_file_src where src_file_id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "src_file_id"),
            @Result(property = "filePath", column = "file_path"),
            @Result(property = "fileDesc", column = "file_desc")})
    FileResource findById(@Param("id") int id);
	
    @Select("SELECT * FROM tbl_file_src where type != 255 order by src_file_id desc " +
            "LIMIT #{offset}, #{rowCount}")
    @Results(value = {
            @Result(property = "id", column = "src_file_id"),
            @Result(property = "filePath", column = "file_path"),
            @Result(property = "fileDesc", column = "file_desc")})
    List<FileResource> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("SELECT count(1) FROM tbl_file_src where type != 255")
    int countAll();

    @Delete("DELETE FROM tbl_file_src where src_file_id=#{id}")
    int delete(int id);

    @Delete("DELETE FROM tbl_file_src where src_file_id in(select file_id from tbl_file_realplay_task where task_id=#{taskId})")
    int deleteByTaskId(int taskId);
    
    @Insert("INSERT INTO tbl_file_src " +
            "   (file_path, file_desc, type) " +
            "VALUES " +
            "   (#{filePath}, #{fileDesc}, #{type})")
    @Options(useGeneratedKeys=true,keyColumn="src_file_id")
    int insert(FileResource fileResource);

    @Update("UPDATE tbl_file_src t set" +
    		"   t.file_path = #{filePath}, " +
    		"   t.file_desc = #{fileDesc}, " +
    		"   t.type = #{type} where t.src_file_id=#{id}"
    		)
	int update(FileResource fileResource);
}
