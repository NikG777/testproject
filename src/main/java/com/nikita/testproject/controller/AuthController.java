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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
            return new ResponseEntity<>( "{\"message\":\"This email or login is already exist!\"}", HttpStatus.BAD_REQUEST);
        } else {
            userService.saveUser(u);
            return new ResponseEntity<>("{\"message\":\"Thank's for registration!\"}", HttpStatus.OK);
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
        return new ResponseEntity<>("{\"message\":\"User not found\"}", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return "{\"message\":\"Activation success\"}";
        } else {
            return "{\"message\":\"Link not work!\"}";
        }
    }

    @PostMapping("/password/reset/{code}")
    public String resetPassword(@PathVariable String code, @RequestBody PasswordRequest req) {
        boolean isReset = userService.updateUserPassword(code, req.getPassword());

        if (isReset)
            return "{\"message\":\"password was reset\"}";
        else return "{\"message\":\"password not reset\"}";
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Parameter(in = ParameterIn.HEADER, description = "Header Authorization со строкой Bearer_/token/", name = "Authorization", required = true)
    @PostMapping("/password/reset")
    public String makeQueryForReset(@RequestBody EmailRequest req) {
        if (userService.resetUserPassword(req.getEmail())) {
            return "{\"message\":\"Password was reset!\"}";
        } else return "{\"message\":\"Password not reset!\"}";
    }



}