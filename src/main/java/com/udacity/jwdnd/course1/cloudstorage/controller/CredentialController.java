package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class CredentialController {

    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credentials")
    public String saveCredential(@ModelAttribute("credential") Credential credential, Model model, Authentication auth) {
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
    public String deleteCredential(@ModelAttribute("credential") Credential credential, Model model, Authentication auth) throws InvalidUserException {
        User currentUser = userService.getUser(auth.getName());
        Credential credentialToDelete = credentialService.getCredentialById(credential.getId());

        if (!credentialToDelete.getUserId().equals(currentUser.getId())) {
            throw new InvalidUserException("Credential does not belong to current user.");
        }

        credentialService.deleteCredential(credential.getId());
        model.addAttribute("success", true);

        return "result";
    }

    @GetMapping("/credentials/decrypt/{credentialId}")
    @ResponseBody
    public String getDecryptedPassword(@PathVariable int credentialId, Authentication auth) throws InvalidUserException {
        User currentUser = userService.getUser(auth.getName());
        Credential credential = credentialService.getCredentialById(credentialId);

        if (!credential.getUserId().equals(currentUser.getId())) {
            throw new InvalidUserException("Credential does not belong to current user.");
        }

        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}
