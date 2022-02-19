package com.postmission.component;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface Uploader {
    String upload(MultipartFile multipartFile, String dirName) throws IOException;
}