package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.DuplicateFileNameException;
import com.udacity.jwdnd.course1.cloudstorage.exception.EmptyFileException;
import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.commons.io.FileUtils;
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
        List<File> userFiles = fileMapper.getFilesByUserId(userMapper.getUser(username).getId());

        for (File file: userFiles) {
            long fileBytes = Long.parseLong(file.getFileSize());
            String fileDisplaySize = FileUtils.byteCountToDisplaySize(fileBytes);
            file.setFileSize(fileDisplaySize);
        }

        return userFiles;
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int saveFile(MultipartFile mpFile, int userId) throws IOException, DuplicateFileNameException, EmptyFileException {
        if (fileMapper.isDuplicateFilename(mpFile.getOriginalFilename(), userId)) {
            throw new DuplicateFileNameException("Cannot have more than 1 file with the same name.");
        }
        if (mpFile.isEmpty()) {
            throw new EmptyFileException("File cannot be empty.");
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
