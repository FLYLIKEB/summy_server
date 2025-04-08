package com.jwp.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jwp.core.domain.User;

public interface UserRepositoryCustom {
    Page<User> findByNameContaining(String name, Pageable pageable);
    Page<User> searchByCondition(UserSearchCondition condition, Pageable pageable);
} 