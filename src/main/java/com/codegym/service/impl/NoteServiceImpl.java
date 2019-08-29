package com.codegym.service.impl;

import com.codegym.model.Note;
import com.codegym.model.Type;
import com.codegym.repository.NoteRepository;
import com.codegym.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Page<Note> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    @Override
    public Note findById(Long id) {
        return noteRepository.findOne(id);
    }

    @Override
    public void save(Note note) {
        noteRepository.save(note);
    }

    @Override
    public void remove(Long id) {
        noteRepository.delete(id);
    }

    @Override
    public Page<Note> findAllByAuthorStartsWith(String author, Pageable pageable) {
        return noteRepository.findAllByAuthorStartsWith(author, pageable);
    }

    @Override
    public Page<Note> findNoteByTitleContains(String title, Pageable pageable) {
        return noteRepository.findNoteByTitleContains(title, pageable);
    }

    @Override
    public Page<Note> findAllByType(Type type, Pageable pageable) {
        return noteRepository.findAllByType(type, pageable);
    }
}
