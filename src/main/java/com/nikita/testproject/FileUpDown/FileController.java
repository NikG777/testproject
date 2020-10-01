package com.nikita.testproject.FileUpDown;

import com.nikita.testproject.entities.UserEntity;
import com.nikita.testproject.filter.JwtProvider;
import com.nikita.testproject.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FileController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/allfiles")
    @ResponseBody
    public String listAllFiles(Model model) {

         storageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList());

        return "listFiles";
    }

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

    @PostMapping("/upload-file")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request ) {
            UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization")));

            String name = storageService.store(file);
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(name)
                    .toUriString();
            user.setUrl_picture(uri);
            userService.update(user);
            return new FileResponse(name, uri, file.getContentType(), file.getSize());

    }

    @PostMapping("/upload-multiple-files")
    @ResponseBody
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file,request))
                .collect(Collectors.toList());
    }
    @PostMapping("/update-picture")
    @ResponseBody
    public FileResponse updatePicture(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request)
    {
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization")));
        String filename = FilenameUtils.getName(user.getUrl_picture().trim());
        storageService.delete(filename);
        String name = storageService.store(multipartFile);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();
        user.setUrl_picture(uri);
        userService.update(user);
        return new FileResponse(name, uri, multipartFile.getContentType(), multipartFile.getSize());

    }
    @PostMapping("/delete-user-picture")
    @ResponseStatus
    public boolean deletePicture(HttpServletRequest request)
    {
        UserEntity user = userService.findByLogin(jwtProvider.getLoginFromToken(request.getHeader("Authorization").substring(7)));
        String filename = FilenameUtils.getName(user.getUrl_picture().trim());
        return storageService.delete(filename);
    }
}