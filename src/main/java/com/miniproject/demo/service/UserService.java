package com.miniproject.demo.service;

import com.miniproject.demo.domain.dao.UserDao;
import com.miniproject.demo.domain.dto.request.UserDto;
import com.miniproject.demo.repository.UserRepository;
import com.miniproject.demo.util.ResponseUtil;
import com.miniproject.demo.constant.ConstantApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userRepository.getDistinctTopByUsername(username);

        if (user == null){
            throw new UsernameNotFoundException("Username Not Found");
        }
        return user;

    }

    public ResponseEntity<Object> getAllUser(){
        try{
            log.info("Getting all user's information");
            List<UserDao> userDaoList = userRepository.findAll();
            List<UserDto> userDtoList = new ArrayList<>();

            for(UserDao userDao: userDaoList){
                if(userDao.getRole().equals("User")){
                    userDtoList.add(UserDto.builder()
                            .id(userDao.getId())
                            .email(userDao.getEmail())
                            .username(userDao.getUsername())
                            .name(userDao.getName())
                            .address(userDao.getAddress())
                            .phone(userDao.getPhone())
                                    .nik(userDao.getNik())
                            .build());
                }
            }

            return ResponseUtil.build(ConstantApp.SUCCESS, userDtoList, HttpStatus.OK);
        }catch (Exception e){
            log.error("Got an error when getting all user's information, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getUserById(Long id){
        try{
            log.info("Getting user by id,id : {}",id);
            Optional<UserDao> userDaoOptional = userRepository.findById(id);

            if (userDaoOptional.isEmpty()){
                return ResponseUtil.build(ConstantApp.DATA_NOT_FOUND,null ,HttpStatus.BAD_REQUEST);
            }
            UserDao userDao = userDaoOptional.get();

            return ResponseUtil.build(ConstantApp.SUCCESS, UserDto.builder()
                    .id(userDao.getId())
                    .email(userDao.getEmail())
                    .username(userDao.getUsername())
                    .name(userDao.getName())
                    .address(userDao.getAddress())
                    .phone(userDao.getPhone())
                    .nik(userDao.getNik())
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            log.error("Got an error when getting user by id, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateUser(Long id, UserDto request){
        try {
            log.info("Updating user with id : {}",id);
            Optional<UserDao> userDaoOptional = userRepository.findById(id);

            if (userDaoOptional.isEmpty()){
                log.info("User not found");
                return ResponseUtil.build(ConstantApp.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            if (!Objects.equals(request.getEmail(), "") && request.getEmail() != null) {
                if (userRepository.existsByEmail(request.getEmail())) {
                    return ResponseUtil.build(ConstantApp.EMAIL_REGISTERED, null, HttpStatus.BAD_REQUEST);
                }
                userDaoOptional.get().setEmail(request.getEmail());
            }

            if (!Objects.equals(request.getUsername(), "") && request.getUsername() != null) {
                if (userRepository.existsByUsername(request.getUsername())) {
                    return ResponseUtil.build(ConstantApp.USERNAME_REGISTERED, null, HttpStatus.BAD_REQUEST);
                }
                userDaoOptional.get().setUsername(request.getUsername());
            }

            if (!Objects.equals(request.getPassword(), "") && request.getPassword() != null) {
                userDaoOptional.get().setPassword(passwordEncoder.encode(request.getPassword()));
            }

            if (!Objects.equals(request.getName(), "") && request.getName() != null) {
                userDaoOptional.get().setName(request.getName());
            }

            if (!Objects.equals(request.getAddress(), "") && request.getAddress() != null) {
                userDaoOptional.get().setAddress(request.getAddress());
            }

            if (!Objects.equals(request.getPhone(), "") && request.getPhone() != null) {
                userDaoOptional.get().setPhone(request.getPhone());
            }

            userRepository.save(userDaoOptional.get());

            return ResponseUtil.build(ConstantApp.DATA_UPDATED, UserDto.builder()
                    .id(userDaoOptional.get().getId())
                    .email(userDaoOptional.get().getEmail())
                    .username(userDaoOptional.get().getUsername())
                    .name(userDaoOptional.get().getName())
                    .address(userDaoOptional.get().getAddress())
                    .phone(userDaoOptional.get().getPhone())
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            log.error("Got an error when updating user's information, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteUser(Long id){
        try {
            log.info("Deleting user with id : {}",id);
            Optional<UserDao> userDaoOptional = userRepository.findById(id);
            if (userDaoOptional.isEmpty()){
                log.info("User not found");
                return ResponseUtil.build(ConstantApp.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            userRepository.delete(userDaoOptional.get());
            return ResponseUtil.build(ConstantApp.DATA_DELETED,null,HttpStatus.OK);

        }catch (Exception e){
            log.error("Got an error when deleting user, error : {}",e.getMessage());
            return ResponseUtil.build(ConstantApp.ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
