package repository;

import domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findByNameContaining(String name, Pageable pageable);
    Page<User> searchByCondition(UserSearchCondition condition, Pageable pageable);
} 