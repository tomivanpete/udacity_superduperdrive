package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String homeView(@ModelAttribute("note") Note note,
                           @ModelAttribute("credential") Credential credential,
                           Model model, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        model.addAttribute("notes", noteService.getNotesByUsername(currentUser.getUsername()));
        model.addAttribute("credentials", credentialService.getCredentialsByUsername(currentUser.getUsername()));
        return "home";
    }

    @PostMapping("/notes")
    public String saveNote(@ModelAttribute("note") Note note, Model model, Authentication auth) {
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

    @PostMapping("/credentials")
    public String saveCredential(@ModelAttribute("credential")Credential credential, Model model, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        credential.setUserId(currentUser.getId());

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);

        credentialService.saveCredential(credential);
        model.addAttribute("success", true);
        return "result";
    }

    @PostMapping("/credentials/delete")
    public String deleteCredential(@ModelAttribute("credential") Credential credential, Model model) {
        credentialService.deleteCredential(credential.getId());
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/credentials/decrypt/{id}")
    @ResponseBody
    public String getDecryptedPassword(@PathVariable int id, Authentication auth) {
        User currentUser = userService.getUser(auth.getName());
        Credential credential = credentialService.getCredentialById(id);

        if (credential.getUserId() != currentUser.getId()) {
            return null;
        }

        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}
