package com.nikita.testproject.controller;

import com.nikita.testproject.entities.UserEntity;
import com.nikita.testproject.fileUpDown.StorageService;
import com.nikita.testproject.filter.JwtProvider;
import com.nikita.testproject.response.UserResponse;
import com.nikita.testproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private StorageService storageService;
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Parameter(in = ParameterIn.HEADER, description = "Header Authorization со строкой Bearer_/token/", name = "Authorization", required = true)
    @PostMapping("/user/getinfo")
    public ResponseEntity<?> returnUserInfo(HttpServletRequest request) {
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7)));
        return new ResponseEntity<>(new UserResponse(user.getLogin(), user.getUrl_picture()), HttpStatus.OK);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Parameter(in = ParameterIn.HEADER, description = "Header Authorization со строкой Bearer_/token/", name = "Authorization", required = true)
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7)));
        Resource resource = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
