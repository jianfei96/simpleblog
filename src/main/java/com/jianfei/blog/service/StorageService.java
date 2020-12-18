package com.jianfei.blog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);
    
    void storeAndSetName(MultipartFile file,String name);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
    
    public void deleteByNameNoSuffix(String name);
    
    public void deleteByName(String name);

}
