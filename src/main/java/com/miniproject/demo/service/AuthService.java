package com.miniproject.demo.service;

import com.miniproject.demo.constant.ConstantApp;
import com.miniproject.demo.domain.dao.UserDao;
import com.miniproject.demo.domain.dto.request.UserDto;
import com.miniproject.demo.domain.dto.response.TokenResponse;
import com.miniproject.demo.repository.UserRepository;
import com.miniproject.demo.security.JwtTokenProvider;
import com.miniproject.demo.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> register(UserDto req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseUtil.build(ConstantApp.USERNAME_REGISTERED, null, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseUtil.build(ConstantApp.EMAIL_REGISTERED, null, HttpStatus.BAD_REQUEST);
        }

        if (req.getRole() == null || req.getRole().equals("User") || !req.getRole().equals("User")) {
            UserDao userDao = new UserDao();
            userDao.setEmail(req.getEmail());
            userDao.setUsername(req.getUsername());
            userDao.setPassword(passwordEncoder.encode(req.getPassword()));
            userDao.setName(req.getName());
            userDao.setAddress(req.getAddress());
            userDao.setPhone(req.getPhone());
            userDao.setNik(0);
            userDao.setRole("User");

            userRepository.save(userDao);
            return ResponseUtil.build("User berhasil terdaftar!", UserDto.builder()
                    .id(userDao.getId())
                    .email(userDao.getEmail())
                    .username(userDao.getUsername())
                    .name(userDao.getName())
                    .address(userDao.getAddress())
                    .phone(userDao.getPhone())
                    .nik(userDao.getNik())
                    .role(userDao.getRole())
                    .build(), HttpStatus.CREATED);
        }

        return ResponseUtil.build(ConstantApp.ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<?> authenticateAndGenerateToken(UserDto req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generationToken(authentication);
            return ResponseUtil.build("Login berhasil!", TokenResponse.builder().token(jwt).build(),
                    HttpStatus.OK);
        } catch (BadCredentialsException e){
            log.error("Bad Credential", e.getMessage());
            return ResponseUtil.build("Username atau password salah!",null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return ResponseUtil.build(ConstantApp.ERROR,null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> generateData(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.substring(7);

        Optional<UserDao> userDaoOptional = userRepository.findById(jwtTokenProvider.getId(token));

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
    }
}
