package com.eureka.mindbloom.commoncode.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.service.CommonCodeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeCacheService commonCodeCacheService;

    @GetMapping("/admin/common-code")
    public ResponseEntity<?> getCommonCode() {
        List<CommonCode> commonCodes = commonCodeCacheService.getAllCommonCodes();
        return ResponseEntity.ok().body(ApiResponse.success("OK", commonCodes));
    }
}