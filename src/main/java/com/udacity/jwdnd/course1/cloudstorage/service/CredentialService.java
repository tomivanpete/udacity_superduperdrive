package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
    }

    public List<Credential> getCredentialsByUsername(String username) {
        return credentialMapper.getCredentialsByUserId(userMapper.getUser(username).getId());
    }

    public Credential getCredentialById(int credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void addCredential(Credential credential) {
        credentialMapper.insert(credential);
    }

    public void updateCredential(Credential credential) {
        credentialMapper.update(credential);
    }

    public void deleteCredential(int credentialId) {
        credentialMapper.delete(credentialId);
    }
}