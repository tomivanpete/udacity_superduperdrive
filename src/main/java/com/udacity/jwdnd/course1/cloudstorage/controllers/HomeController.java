package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("note") Note note, Model model, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        model.addAttribute("notes", noteService.getNotesByUsername(currentUser.getUsername()));
        return "home";
    }

    @PostMapping("/notes")
    public String addNote(@ModelAttribute("note") Note note, Model model, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        note.setUserId(currentUser.getId());
        noteService.addNote(note);
        model.addAttribute("success", true);
        return "result";
    }
}
