package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.InvalidUserException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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

    public int saveNote(Note note) throws InvalidUserException {
        // If the note does not have an ID, then a new entry must be added to the DB.
        // Else, the existing note is updated.
        if (note.getId() == null) {
            return noteMapper.insert(note);
        } else {
            Note existingNote = noteMapper.getNote(note.getId());
            if (!existingNote.getUserId().equals(note.getUserId())) {
                throw new InvalidUserException("Note does not belong to current user.");
            }
            return noteMapper.update(note);
        }
    }

    public int deleteNote(Note note) throws InvalidUserException {
        Note existingNote = noteMapper.getNote(note.getId());

        if (!existingNote.getUserId().equals(note.getUserId())) {
            throw new InvalidUserException("Note does not belong to current user.");
        }

        return noteMapper.delete(note.getId());
    }
}
