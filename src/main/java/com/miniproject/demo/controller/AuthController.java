package com.miniproject.demo.controller;

import com.miniproject.demo.domain.dto.request.UserDto;
import com.miniproject.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Log4j2
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody UserDto req){
        return authService.register(req);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserDto req){
        return authService.authenticateAndGenerateToken(req);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<?> generateData(HttpServletRequest request){
        return authService.generateData(request);
    }
}
