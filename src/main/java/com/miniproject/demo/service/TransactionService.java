package com.miniproject.demo.service;

import com.miniproject.demo.constant.Action;
import com.miniproject.demo.constant.ConstantApp;
import com.miniproject.demo.domain.dao.AccountDao;
import com.miniproject.demo.domain.dao.TransactionDao;
import com.miniproject.demo.domain.dao.UserDao;
import com.miniproject.demo.domain.dto.request.TransactionDto;
import com.miniproject.demo.domain.dto.request.UserDto;
import com.miniproject.demo.repository.AccountRepository;
import com.miniproject.demo.repository.TransactionRepository;
import com.miniproject.demo.repository.UserRepository;
import com.miniproject.demo.security.JwtTokenProvider;
import com.miniproject.demo.util.ResponseUtil;
import com.miniproject.demo.util.TransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<Object> getAllTransaction(){
        try {
            log.info("Getting all product's information");
            List<TransactionDao> transactionDaoList = transactionRepository.findAll();
            List<TransactionDto> transactionDtoList = new ArrayList<>();

            for (TransactionDao transactionDao: transactionDaoList){
                Optional<UserDao> userDaoOptional = userRepository.findById(transactionDao.getUser().getId());

                transactionDtoList.add(TransactionDto.builder()
                        .id(transactionDao.getId())
                        .created(transactionDao.getCreatedAt())
                                .SourceAccountId(transactionDao.getSourceAccountId())
                                .TargetAccountId(transactionDao.getTargetAccountId())
                                .amount(transactionDao.getAmount())
                        .user(UserDto.builder()
                                .username(userDaoOptional.get().getUsername())
                                .build())
                        .build());
            }

            return ResponseUtil.build(ConstantApp.SUCCESS, transactionDtoList, HttpStatus.OK);
        }catch (Exception e) {
            log.error("Got an error when getting all transaction's information, error : {}", e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<Object> getUserTransaction(HttpServletRequest request){
        try {
            log.info("Getting user's transactions");

            String bearerToken = request.getHeader("Authorization");
            String token = bearerToken.substring(7);

            Optional<UserDao> userToken = userRepository.findById(jwtTokenProvider.getId(token));

            List<TransactionDao> transactionDaoList = transactionRepository.findAll();
            List<TransactionDto> transactionDtoList = new ArrayList<>();

            for (TransactionDao transactionDao: transactionDaoList){
                if(transactionDao.getUser().getId().equals(userToken.get().getId())){
                    Optional<UserDao> userDaoOptional = userRepository.findById(transactionDao.getUser().getId());

                    transactionDtoList.add(TransactionDto.builder()
                        .id(transactionDao.getId())
                        .created(transactionDao.getCreatedAt())
                                    .SourceAccountId(transactionDao.getSourceAccountId())
                                    .TargetAccountId(transactionDao.getTargetAccountId())
                                    .amount(transactionDao.getAmount())
                        .user(UserDto.builder()
                                .username(userDaoOptional.get().getUsername())
                                .build())
                        .build());
                }
            }
            return ResponseUtil.build(ConstantApp.SUCCESS, transactionDtoList, HttpStatus.OK);
        }catch (Exception e) {
            log.error("Got an error when getting all transaction's information, error : {}", e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<Object> getTransactionById(Long id){
        try{
            log.info("Getting transaction by id, id : {}", id);
            Optional<TransactionDao> transactionDaoOptional = transactionRepository.findById(id);

            if (transactionDaoOptional.isEmpty()){
                return ResponseUtil.build(ConstantApp.DATA_NOT_FOUND,null ,HttpStatus.BAD_REQUEST);
            }
            TransactionDao transactionDao = transactionDaoOptional.get();

            return ResponseUtil.build(ConstantApp.SUCCESS, TransactionDto.builder()
                    .id(transactionDao.getId())
                    .created(transactionDao.getCreatedAt())
                            .SourceAccountId(transactionDao.getSourceAccountId())
                            .TargetAccountId(transactionDao.getTargetAccountId())
                            .amount(transactionDao.getAmount())
                    .user(UserDto.builder()
                            .username(transactionDao.getUser().getUsername())
                            .build())
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            log.error("Got an error when getting transaction by id, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean CreateTransfer(TransactionUtil transactionUtil) {
        String sourceSortCode = transactionUtil.getSourceAccount().getSortCode();
        String sourceAccountNumber = transactionUtil.getSourceAccount().getAccountNumber();
        Optional<AccountRepository> sourceAccount = accountRepository
                .findBySortCodeAndAccountNumber(sourceSortCode, sourceAccountNumber);

        String targetSortCode = transactionUtil.getTargetAccount().getSortCode();
        String targetAccountNumber = transactionUtil.getTargetAccount().getAccountNumber();
        Optional<AccountRepository> targetAccount = accountRepository
                .findBySortCodeAndAccountNumber(targetSortCode, targetAccountNumber);

        if (sourceAccount.isPresent() && targetAccount.isPresent()) {
            if (isAmountAvailable(transactionUtil.getAmount(), sourceAccount.get().getCurrentBalance())) {
                var transaction = new TransactionDao();

                transaction.setAmount(transactionUtil.getAmount());
                transaction.setSourceAccountId(sourceAccount.get().getId());
                transaction.setTargetAccountId(targetAccount.get().getId());
                transaction.settargetOwnerName\(targetAccount.get().getOwnerName());
                transaction.setReference(transactionUtil.getReference());
                transaction.setLatitude(transactionUtil.getLatitude());
                transaction.setLongitude(transactionUtil.getLongitude());

                updateAccountBalance(sourceAccount.get(), transactionUtil.getAmount(), Action.WITHDRAW);
                transactionRepository.save(transaction);

                return true;
            }
        }
        return false;
    }

    public void updateAccountBalance(AccountDao account, double amount, ACTION action) {
        if (action == Action.WITHDRAW) {
            account.setCurrentBalance((account.getCurrentBalance() - amount));
        } else if (action == Action.DEPOSIT) {
            account.setCurrentBalance((account.getCurrentBalance() + amount));
        }
        accountRepository.save(account);
    }

    // TODO support overdrafts or credit account
    public boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
    }

    public ResponseEntity<Object> deleteTransaction(Long id){
        try {
            log.info("Deleting transaction with id : {}",id);
            Optional<TransactionDao> transactionDaoOptional = transactionRepository.findById(id);
            if (transactionDaoOptional.isEmpty()){
                log.info("Transaction not found");
                return ResponseUtil.build(ConstantApp.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            transactionRepository.delete(transactionDaoOptional.get());
            return ResponseUtil.build(ConstantApp.SUCCESS,null,HttpStatus.OK);

        }catch (Exception e){
            log.error("Got an error when deleting transaction, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
