package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE id = #{fileId}")
    File getFileById(int fileId);

    @Select("SELECT CASE WHEN COUNT(filename) > 0 THEN true ELSE false END FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    boolean isDuplicateFilename(String filename, int userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUserId(int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE id = #{fileId}")
    int delete(int fileId);
}
