package com.eureka.mindbloom.event.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/event")
public class EventPageController {

    @GetMapping("/winners")
    public ResponseEntity<Resource> getEventPage() {
        LocalTime currentTime = LocalTime.now();
        LocalTime allowedTime = LocalTime.of(23, 33);


        try {
            Path filePath = Paths.get("src/main/resources/static/html/getWinnerPage.html");
            Path errorPath = Paths.get("src/main/resources/static/html/error.html");
            Resource resource = new FileSystemResource(filePath);
            Resource errorResource = new FileSystemResource(errorPath);

            CacheControl cacheControl = CacheControl.maxAge(10, TimeUnit.MINUTES)
                    .mustRevalidate();

            if (currentTime.isBefore(allowedTime)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .cacheControl(cacheControl)
                        .body(errorResource);
            }

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .cacheControl(cacheControl)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .cacheControl(cacheControl)
                        .body(errorResource);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
