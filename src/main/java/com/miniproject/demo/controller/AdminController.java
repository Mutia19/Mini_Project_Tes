package com.miniproject.demo.controller;

import com.miniproject.demo.domain.dao.UserDao;
import com.miniproject.demo.util.ResponseUtil;
import com.miniproject.demo.constant.ConstantApp;
import com.miniproject.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/data")
    public ResponseEntity<Object> get(Principal principal){
        UserDao user = (UserDao) userService.loadUserByUsername(principal.getName());
        if (user.getRole().equals("Admin")) {
            return adminService.getAllData();
        }
        return ResponseUtil.build(ConstantApp.NOT_AUTHORIZED, null, HttpStatus.FORBIDDEN);
    }

}
