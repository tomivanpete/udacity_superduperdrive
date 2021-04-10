package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public List<Note> getNotesByUsername(String username) {
        return noteMapper.getNotesByUserId(userMapper.getUser(username).getId());
    }

    public Note getNoteById(int noteId) {
        return noteMapper.getNote(noteId);
    }

    public void addNote(Note note) {
        noteMapper.insert(note);
    }

    public void updateNote(Note note) {
        noteMapper.update(note);
    }

    public void deleteNote(int noteId) {
        noteMapper.delete(noteId);
    }
}
