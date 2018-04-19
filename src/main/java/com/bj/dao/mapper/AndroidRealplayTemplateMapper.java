package com.bj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bj.pojo.AndroidRealplayTemplate;

/**
 * Created by LQK on 2018-3-25.
 */
@Mapper
public interface AndroidRealplayTemplateMapper {	
	@Select("SELECT * FROM tbl_android_realplay_template where template_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "template_id"),
            @Result(property = "miniPicPath", column = "mini_pic_path"),
            @Result(property = "picPath", column = "pic_path"),
            @Result(property = "backgroudVideo", column = "backgroud_video"),
            @Result(property = "sigPicBoderPath", column = "sig_pic_boder_path"),
            @Result(property = "realplayAreas", javaType = List.class, column = "template_id", many = @Many(select = "com.bj.dao.mapper.AndroidRealplayAreaMapper.findByTemplateId"))})
    AndroidRealplayTemplate findById(@Param("id") int id);
    
    @Select("SELECT * FROM tbl_android_realplay_template " +
            "LIMIT #{offset}, #{rowCount} ")
    @Results(value = {
            @Result(property = "id", column = "template_id"),
            @Result(property = "miniPicPath", column = "mini_pic_path"),
            @Result(property = "picPath", column = "pic_path"),
            @Result(property = "backgroudVideo", column = "backgroud_video"),
            @Result(property = "sigPicBoderPath", column = "sig_pic_boder_path"),
            @Result(property = "realplayAreas", javaType = List.class, column = "template_id", many = @Many(select = "com.bj.dao.mapper.AndroidRealplayAreaMapper.findByTemplateId"))})
    List<AndroidRealplayTemplate> findAll(@Param("offset") int offset, @Param("rowCount") int rowCount);

    @Insert("INSERT INTO tbl_android_realplay_template " +
            "   (name, `desc`, longitude, latitude, mini_pic_path, pic_path, backgroud_video, sig_pic_boder_path) " +
            "VALUES " +
            "   (#{name}, #{desc}, #{longitude}, #{latitude}, #{miniPicPath}, #{picPath}, #{backgroudVideo}, #{sigPicBoderPath})")
    @Options(useGeneratedKeys=true,keyColumn="template_id",keyProperty="id")
    int insert(AndroidRealplayTemplate androidRealplayTemplate);

    @Update("<script>" + 
    		"UPDATE tbl_android_realplay_template t " +
    		"<trim prefix=\"set\" suffixOverrides=\",\">" +
    		"   <if test=\"name != null\"> t.name = #{name}, </if>" +
    		"   <if test=\"desc != null\"> t.desc = #{desc}, </if>" +
    		"   <if test=\"miniPicPath != null\"> t.mini_pic_path = #{miniPicPath}, </if>" +
    		"   <if test=\"picPath != null\"> t.pic_path = #{picPath}, </if>" +
    		"   <if test=\"backgroudVideo != null\"> t.backgroud_video = #{backgroudVideo}, </if>" +
    		"   <if test=\"sigPicBoderPath != null\"> t.sig_pic_boder_path = #{sigPicBoderPath}, </if>" +
    		"</trim>" +
    		"   where t.template_id = #{id}" +
    		"</script>")
    int update(AndroidRealplayTemplate androidRealplayTemplate);

    @Select("SELECT count(1) FROM tbl_android_realplay_template")
    int countAll();
    
    @Delete("DELETE FROM tbl_android_realplay_template where template_id=#{id}")
    int delete(int id);
}
