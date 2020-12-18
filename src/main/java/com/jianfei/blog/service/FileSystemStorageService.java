package com.jianfei.blog.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jianfei.blog.exception.StorageException;
import com.jianfei.blog.exception.StorageFileNotFoundException;
import com.jianfei.blog.properties.StorageProperties;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        java.util.Date date = new java.util.Date();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(String.valueOf(date.getTime())+"."+suffix),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
    
    @Override
    public void storeAndSetName(MultipartFile file,String name) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(name),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
    	
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        
    }
    @Override
    public void deleteByNameNoSuffix(String name) {
//    	List<Path> paths =
    	try {
			Files.walk(rootLocation, 1)
			.filter(path -> path.getFileName().toString().replaceAll("[.][^.]+$", "").equals(name))
			.forEach(path -> FileSystemUtils.deleteRecursively(path.toFile()));
    		
//			DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocation);
//			for(Path path : stream){
//		        if(path.getFileName().toString().replaceAll("[.][^.]+$", "").equals(name)) {
//		        	
//		        }
//		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new StorageFileNotFoundException(
                    "Could not delete file: ");
		}
//    	.collect(Collectors.toList());
//    	paths.forEach(path -> FileSystemUtils.deleteRecursively(path.toFile()));
    }
    
    @Override
    public void deleteByName(String name) {
//    	List<Path> paths =
    	try {
			Files.walk(rootLocation, 1)
			.filter(path -> path.getFileName().toString().equals(name))
			.forEach(path -> FileSystemUtils.deleteRecursively(path.toFile()));
    		
//			DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocation);
//			for(Path path : stream){
//		        if(path.getFileName().toString().replaceAll("[.][^.]+$", "").equals(name)) {
//		        	
//		        }
//		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new StorageFileNotFoundException(
                    "Could not delete file: ");
		}
//    	.collect(Collectors.toList());
//    	paths.forEach(path -> FileSystemUtils.deleteRecursively(path.toFile()));
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
