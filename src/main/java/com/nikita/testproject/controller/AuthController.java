package com.nikita.testproject.controller;


import com.nikita.testproject.filter.JwtFilter;
import com.nikita.testproject.filter.JwtProvider;
import com.nikita.testproject.request.EmailRequest;
import com.nikita.testproject.request.PasswordRequest;
import com.nikita.testproject.request.UserAccRequest;
import com.nikita.testproject.response.AuthResponse;
import com.nikita.testproject.response.UserResponse;
import com.nikita.testproject.validator.UserAccValidator;
import com.nikita.testproject.entities.UserEntity;
import com.nikita.testproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
    public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccValidator userAccValidator;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtFilter jwtFilter;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserAccRequest registrationRequest, BindingResult result) {
        userAccValidator.validate(registrationRequest, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError(), HttpStatus.NOT_ACCEPTABLE);
        }
        UserEntity u = new UserEntity();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        u.setEmail(registrationRequest.getEmail());
        // Необходимо продумать возврат ошибки
        if (userService.findByLogin(u.getLogin()) != null || userService.findByEmail(u.getEmail()) != null) {
            return new ResponseEntity<>("Bad", HttpStatus.BAD_REQUEST);
        } else {
            userService.saveUser(u);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/authenticate")
    public ResponseEntity<?> auth(@RequestBody @Valid UserAccRequest request, BindingResult result)  {
        userAccValidator.validate(request, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError(), HttpStatus.NOT_ACCEPTABLE);
        }
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (userEntity != null) {
            String token = jwtProvider.generateToken(userEntity.getLogin(),userEntity.getRoleEntity().getName());
            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
        }
        return new ResponseEntity<>("Такого пользователя не существует", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return "Activation success";
        } else {
            return "Link not work";
        }
    }

    @PostMapping("/password/reset/{code}")
    public String resetPassword(@PathVariable String code, @RequestBody PasswordRequest req) {
        boolean isReset = userService.updateUserPassword(code, req.getPassword());

        if (isReset)
            return "password was reset";
        else return "password not reset";
    }

    @PostMapping("/password/reset")
    public String makeQueryForReset(@RequestBody EmailRequest req) {
        if (userService.resetUserPassword(req.getEmail())) {
            return "good";
        } else return "false";
    }

    @PostMapping("/user/getinfo")
    public ResponseEntity<?> returnUserInfo(HttpServletRequest request) {
       UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7)));
        return new ResponseEntity<>(new UserResponse(user.getLogin(), user.getUrl_picture()), HttpStatus.OK);

    }

}