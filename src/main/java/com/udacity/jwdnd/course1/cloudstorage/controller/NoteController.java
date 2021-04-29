package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/notes")
    public String saveNote(@ModelAttribute("note") Note note, Model model, Authentication auth) throws InvalidUserException {
        User currentUser = userService.getUser(auth.getName());
        note.setUserId(currentUser.getId());
        int rowsUpdated = noteService.saveNote(note);

        if (rowsUpdated < 1) {
            throw new RuntimeException("Could not save Note to DB");
        }

        model.addAttribute("success", true);

        return "result";
    }

    @PostMapping("/notes/delete")
    public String deleteNote(@ModelAttribute("note") Note note, Model model, Authentication auth) throws InvalidUserException {
        User currentUser = userService.getUser(auth.getName());
        note.setUserId(currentUser.getId());
        int rowsUpdated = noteService.deleteNote(note);

        if (rowsUpdated < 1) {
            throw new RuntimeException("Could not delete Note from DB");
        }

        model.addAttribute("success", true);

        return "result";
    }
}
