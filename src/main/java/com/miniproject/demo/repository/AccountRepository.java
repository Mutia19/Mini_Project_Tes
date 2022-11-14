package com.miniproject.demo.repository;

import com.miniproject.demo.domain.dao.AccountDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountDao, Long> {
    Optional<AccountRepository> findBySortCodeAndAccountNumber(String SortCode, String AccountNumber);
    Optional<AccountRepository> findByAccountNumber(String AccountNumber);
}
