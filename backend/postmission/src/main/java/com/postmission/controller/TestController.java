package com.postmission.controller;

import com.postmission.component.Uploader;
import com.postmission.configuration.SecurityConfig;
import com.postmission.model.User;
import com.postmission.model.dto.request.UserApiRequest;
import com.postmission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TestController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final Uploader uploader;

    @PostMapping("/join")
    public String joinTest(@RequestBody User user){
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles("ROLE_USER");
//        userRepository.save(user);
        return "done";
    }

    @PostMapping("/join/admin")
    public String joinAdmin(@RequestBody UserApiRequest userApiRequest){
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles("ROLE_ADMIN");
//        userRepository.save(user);
        return "done";
    }

    @GetMapping("/admin")
    public String adminTest(){
        return "test";
    }

    @GetMapping("/why")
    public String testV1(){
        return "hi";
    }

    @PostMapping("/files")
    public String testFile(@RequestParam("image")MultipartFile file) throws IOException {

        String images = uploader.upload(file, "images");
        System.out.println(images);

        return "a";
    }

    private String getFileExtension(String filename){
        return filename.substring(filename.lastIndexOf("."));
    }
}
