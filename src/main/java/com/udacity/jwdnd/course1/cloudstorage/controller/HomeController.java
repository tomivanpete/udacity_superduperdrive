package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
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
        noteService.saveNote(note);
        model.addAttribute("success", true);
        return "result";
    }

    @PostMapping("/notes/delete")
    public String deleteNote(@ModelAttribute("note") Note note, Model model) {
        noteService.deleteNote(note.getId());
        model.addAttribute("success", true);
        return "result";
    }
}
