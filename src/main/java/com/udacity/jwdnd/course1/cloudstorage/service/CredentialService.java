package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
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

    public int saveCredential(Credential credential) throws InvalidUserException {
        if (credential.getId() == null) {
            return credentialMapper.insert(credential);
        } else {
            Credential existingCredential = credentialMapper.getCredential(credential.getId());
            if (!existingCredential.getUserId().equals(credential.getUserId())) {
                throw new InvalidUserException("Credential does not belong to current user.");
            }

            return credentialMapper.update(credential);
        }
    }

    public int deleteCredential(Credential credential) throws InvalidUserException {
        Credential existingCredential = credentialMapper.getCredential(credential.getId());
        if (!existingCredential.getUserId().equals(credential.getUserId())) {
            throw new InvalidUserException("Credential does not belong to current user.");
        }

        return credentialMapper.delete(credential.getId());
    }
}
