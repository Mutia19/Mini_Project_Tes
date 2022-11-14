package com.miniproject.demo.repository;

import com.miniproject.demo.domain.dao.TransactionDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDao, Long> {

    List<TransactionDao> findBysourceAccountIdOrderByInitiationDate(long id);
}
