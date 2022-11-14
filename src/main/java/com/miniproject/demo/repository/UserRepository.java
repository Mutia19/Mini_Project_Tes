package com.miniproject.demo.repository;

import com.miniproject.demo.domain.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDao,Long> {
    UserDao findByUsername(String username);
    UserDao getDistinctTopByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
