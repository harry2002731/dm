package com.example.dm_test.controller;

import com.example.dm_test.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class ImageController {
    private final ResourceLoader resourceLoader;

    @Autowired
    private ClassificationService classificationService;

    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/images/tree")
    public ResponseEntity<Resource> getImage(@RequestParam() int height, @RequestParam() int leaves) throws IOException {
        String path = classificationService.getTreeVisualization(height,leaves);
        String res = "file:" + path;
        Resource resource = resourceLoader.getResource(res);
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}