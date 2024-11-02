package com.eureka.mindbloom.event.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;

@Controller
public class WinnerPageController {

    @GetMapping("/winners")
    public ResponseEntity<String> getWinnerPage() throws Exception {
        LocalTime currentTime = LocalTime.now();
        LocalTime releaseTime = LocalTime.of(13, 0);

        Path errorPath = new ClassPathResource("static/html/error.html").getFile().toPath();
        String errorContent = Files.readString(errorPath, StandardCharsets.UTF_8);

        if (currentTime.isBefore(releaseTime)) {
            return ResponseEntity.ok(errorContent);
        }

        Path path = new ClassPathResource("static/html/getWinnerPage.html").getFile().toPath();
        String content = Files.readString(path, StandardCharsets.UTF_8);
        return ResponseEntity.ok(content);
    }
}