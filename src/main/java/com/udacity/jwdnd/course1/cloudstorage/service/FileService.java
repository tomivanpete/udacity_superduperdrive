package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.DuplicateFileNameException;
import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
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

    public int saveFile(MultipartFile mpFile, int userId) throws IOException, DuplicateFileNameException {
        if (fileMapper.isDuplicateFilename(mpFile.getOriginalFilename(), userId)) {
            throw new DuplicateFileNameException("Cannot have more than 1 file with the same name.");
        }

        File fileToSave = new File();
        fileToSave.setUserId(userId);
        fileToSave.setFilename(mpFile.getOriginalFilename());
        fileToSave.setContentType(mpFile.getContentType());
        fileToSave.setFileSize(String.valueOf(mpFile.getSize()));
        fileToSave.setFileData(mpFile.getBytes());

        return fileMapper.insert(fileToSave);
    }

    public int deleteFile(File file) throws InvalidUserException {
        File existingFile = fileMapper.getFileById(file.getId());
        if (!existingFile.getUserId().equals(file.getUserId())) {
            throw new InvalidUserException("File does not belong to current user.");
        }

        return fileMapper.delete(file.getId());
    }
}
