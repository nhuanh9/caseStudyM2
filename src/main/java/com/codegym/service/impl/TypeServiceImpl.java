package com.codegym.service.impl;

import com.codegym.model.Type;
import com.codegym.repository.TypeRepository;
import com.codegym.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Override
    public Page<Type> findAll(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public Type findById(Long id) {
        return typeRepository.findOne(id);
    }

    @Override
    public void save(Type type) {
        typeRepository.save(type);
    }

    @Override
    public void remove(Long id) {
        typeRepository.delete(id);
    }

    @Override
    public Page<Type> findTypeByName(String name, Pageable pageable) {
        return typeRepository.findTypeByName(name, pageable);
    }
}
