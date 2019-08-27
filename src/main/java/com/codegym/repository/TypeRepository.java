package com.codegym.repository;

import com.codegym.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TypeRepository extends PagingAndSortingRepository<Type, Long> {
    Page<Type> findTypeByName(String name, Pageable pageable);
}
