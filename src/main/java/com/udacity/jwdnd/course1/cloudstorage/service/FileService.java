package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public List<File> getFilesByUsername(String username) {
        return fileMapper.getFilesByUserId(userMapper.getUser(username).getId());
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void saveFile(MultipartFile mpFile, int userId) throws IOException {
        File fileToSave = new File();
        fileToSave.setUserId(userId);
        fileToSave.setFilename(mpFile.getOriginalFilename());
        fileToSave.setContentType(mpFile.getContentType());
        fileToSave.setFileSize(String.valueOf(mpFile.getSize()));
        fileToSave.setFileData(mpFile.getBytes());
        fileMapper.insert(fileToSave);
    }

    public void deleteFile(int fileId) {
        fileMapper.delete(fileId);
    }
}
