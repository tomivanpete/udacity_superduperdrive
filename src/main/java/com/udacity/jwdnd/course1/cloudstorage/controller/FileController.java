package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String uploadFile(@RequestParam("fileUpload")MultipartFile file, Authentication auth, Model model) throws IOException {
        User currentUser = userService.getUser(auth.getName());
        fileService.saveFile(file, currentUser.getId());
        model.addAttribute("success", true);

        return "result";
    }

    @GetMapping("/files/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable int fileId) {
        File file = fileService.getFileById(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));

        return new ResponseEntity<>(file.getFileData(), headers, HttpStatus.OK);
    }

    @PostMapping("/files/delete")
    public String deleteFile(@ModelAttribute("file")File file, Model model) {
        fileService.deleteFile(file.getId());
        model.addAttribute("success", true);
        return "result";
    }
}
