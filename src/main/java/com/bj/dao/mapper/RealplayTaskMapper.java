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

import com.bj.pojo.FileResource;
import com.bj.pojo.RealplayTask;
import com.bj.pojo.SplitTemplates;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface RealplayTaskMapper {
	@Select("SELECT * FROM tbl_file_realplay_task where task_id=#{id}")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystemIdsStr", column = "ecue_list"),
            @Result(property = "maxPlayTime", column = "max_play_time"),
            @Result(property = "fileResource", javaType = FileResource.class, column = "file_id", one = @One(select = "com.bj.dao.mapper.FileResourceMapper.findById")),
            @Result(property = "splitTemplate", javaType = SplitTemplates.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
    RealplayTask findById(@Param("id") int id);

	@Select("SELECT * FROM tbl_file_realplay_task where file_id=#{fileId}")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystemIdsStr", column = "ecue_list"),
            @Result(property = "maxPlayTime", column = "max_play_time"),
            @Result(property = "fileResource", javaType = FileResource.class, column = "file_id", one = @One(select = "com.bj.dao.mapper.FileResourceMapper.findById")),
            @Result(property = "splitTemplate", javaType = SplitTemplates.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
	List<RealplayTask> findByFileId(int fileId);
    
    @Select("SELECT * FROM tbl_file_realplay_task order by ${property} ${sort} " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystemIdsStr", column = "ecue_list"),
            @Result(property = "maxPlayTime", column = "max_play_time"),
            @Result(property = "fileResource", javaType = FileResource.class, column = "file_id", one = @One(select = "com.bj.dao.mapper.FileResourceMapper.findById")),
            @Result(property = "splitTemplate", javaType = SplitTemplates.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
    List<RealplayTask> findAllByOrder(@Param("property") String property, @Param("sort") String sort, @Param("offset") int offset, @Param("rowCount") int rowCount);

    @Select("select a.* from tbl_file_realplay_task a, tbl_file_src b, tbl_file_split_template c " +
    		"where a.file_id=b.src_file_id and a.template_id=c.template_id order by ${property} ${sort} " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "task_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "errCode", column = "error_code"),
            @Result(property = "subSystemIdsStr", column = "ecue_list"),
            @Result(property = "maxPlayTime", column = "max_play_time"),
            @Result(property = "fileResource", javaType = FileResource.class, column = "file_id", one = @One(select = "com.bj.dao.mapper.FileResourceMapper.findById")),
            @Result(property = "splitTemplate", javaType = SplitTemplates.class, column = "template_id", one = @One(select = "com.bj.dao.mapper.SplitTemplatesMapper.findById"))})
    List<RealplayTask> findAllByOrderGLB(@Param("property") String property, @Param("sort") String sort, @Param("offset") int offset, @Param("rowCount") int rowCount);

    @Insert("INSERT INTO tbl_file_realplay_task " +
            "   (file_id, template_id, repeate, start_time, status, ecue_list, max_play_time) " +
            "VALUES " +
            "   (#{fileResource.id}, #{splitTemplate.id}, #{repeate}, #{startTime}, #{status}, #{subSystemIdsStr}, #{maxPlayTime})")
    @Options(useGeneratedKeys=true,keyColumn="task_id")
    int insert(RealplayTask realplayTask);

    @Update("UPDATE tbl_file_realplay_task t set" +
    		"   t.file_id = #{fileResource.id}, " +
    		"   t.template_id = #{splitTemplate.id}, " +
    		"   t.repeate = #{repeate}, " +
    		"   t.start_time = #{startTime}, " +
    		"   t.status = #{status} " +
    		"   t.max_play_time = #{maxPlayTime} " +
            "   where task_id=#{id}")
    int update(RealplayTask realplayTask);

    @Select("SELECT count(1) FROM tbl_file_realplay_task")
    int countAll();
    
    @Select("SELECT count(1) FROM tbl_file_realplay_task where template_id = #{templateId}")
    int countByTemplateId(int templateId);

    @Delete("DELETE FROM tbl_file_realplay_task where task_id=#{id}")
    int delete(int id);
}
