package com.codegym.repository;

import com.codegym.model.Note;
import com.codegym.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {

    Page<Note> findAllByAuthorStartsWith(String author, Pageable pageable);

    Page<Note> findAllByType(Type author, Pageable pageable);

    Page<Note> findNoteByTitleContains(String title, Pageable pageable);

}
