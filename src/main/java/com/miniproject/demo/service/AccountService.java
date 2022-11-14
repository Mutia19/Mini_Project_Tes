package com.miniproject.demo.service;

import com.miniproject.demo.domain.dao.AccountDao;
import com.miniproject.demo.repository.AccountRepository;
import com.miniproject.demo.repository.TransactionRepository;
import com.miniproject.demo.util.CodeGenerator;

import java.util.Optional;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountDao getAccount(String sortCode, String accountNumber) {
        Optional<AccountRepository> account = accountRepository
                .findBySortCodeAndAccountNumber(sortCode, accountNumber);

        account.ifPresent(value ->
                value.setTransactions(transactionRepository
                        .findBySourceAccountIdOrderByInitiationDate(value.getId())));

        return account.orElse(null);
    }

    public AccountDao getAccount(String accountNumber) {
        Optional<AccountRepository> account = accountRepository
                .findByAccountNumber(accountNumber);

        return account.orElse(null);
    }

    public AccountDao createAccount(String bankName, String ownerName) {
        CodeGenerator codeGenerator = new CodeGenerator();
        AccountDao newAccount = new AccountDao(bankName, ownerName, codeGenerator.generateSortCode(), codeGenerator.generateAccountNumber(), 0.00);
        return accountRepository.save(newAccount);
    }
}
