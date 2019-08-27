package com.codegym.service;

import com.codegym.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TypeService {

    Page<Type> findAll(Pageable pageable);

    Type findById(Long id);

    void save(Type type);

    void remove(Long id);

    Page<Type> findTypeByName(String name, Pageable pageable);
}
