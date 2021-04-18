package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("note") Note note,
                           @ModelAttribute("credential") Credential credential,
                           @ModelAttribute("file") File file,
                           Model model, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        model.addAttribute("notes", noteService.getNotesByUsername(currentUser.getUsername()));
        model.addAttribute("credentials", credentialService.getCredentialsByUsername(currentUser.getUsername()));
        model.addAttribute("files", fileService.getFilesByUsername(currentUser.getUsername()));
        return "home";
    }
}
