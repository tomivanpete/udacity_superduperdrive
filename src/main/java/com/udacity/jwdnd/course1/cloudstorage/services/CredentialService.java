package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
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
