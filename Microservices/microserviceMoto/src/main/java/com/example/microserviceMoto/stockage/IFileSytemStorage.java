package com.example.microserviceMoto.stockage;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface IFileSytemStorage {
    void init();
    String saveFile(MultipartFile file);
    Resource loadFile(String fileName) throws FileNotFoundException;
    public void deleteFile(String fileName);
}