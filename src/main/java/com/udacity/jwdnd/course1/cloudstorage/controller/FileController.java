package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.DuplicateFileNameException;
import com.udacity.jwdnd.course1.cloudstorage.exception.EmptyFileException;
import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public String uploadFile(@RequestParam("fileUpload")MultipartFile file, Authentication auth, Model model) throws IOException, DuplicateFileNameException, EmptyFileException {
        User currentUser = userService.getUser(auth.getName());
        int rowsUpdated = fileService.saveFile(file, currentUser.getId());

        if (rowsUpdated < 1) {
            throw new RuntimeException("Could not save File to DB.");
        }

        model.addAttribute("success", true);

        return "result";
    }

    @GetMapping("/files/{fileId}")
    @ResponseBody
    public HttpEntity<byte[]> getFile(@PathVariable int fileId, Authentication auth, HttpServletResponse response) throws InvalidUserException, FileNotFoundException {
        User currentUser = userService.getUser(auth.getName());
        File file = fileService.getFileById(fileId);

        if (file == null) {
            throw new FileNotFoundException("File with ID " + fileId + " not found.");
        }

        if (!file.getUserId().equals(currentUser.getId())) {
            throw new InvalidUserException("File does not belong to current user.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());

        return new HttpEntity<>(file.getFileData(), headers);
    }

    @PostMapping("/files/delete")
    public String deleteFile(@ModelAttribute("file")File file, Model model, Authentication auth) throws InvalidUserException {
        User currentUser = userService.getUser(auth.getName());
        file.setUserId(currentUser.getId());
        int rowsUpdated = fileService.deleteFile(file);

        if (rowsUpdated < 1) {
            throw new RuntimeException("Could not delete File from DB.");
        }

        model.addAttribute("success", true);

        return "result";
    }
}
