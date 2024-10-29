package com.eureka.mindbloom.commoncode.service;

import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.domain.CommonCodeGroup;
import com.eureka.mindbloom.commoncode.exception.CommonCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeConvertService {

    private final CommonCodeCacheService commonCodeCacheService;

    public CommonCode codeNameToCommonCode(String codeName) {
        List<CommonCode> caches = commonCodeCacheService.getAllCommonCodes();

        return caches.stream()
                .filter(cache -> codeName.equals(cache.getName()))
                .findFirst()
                .orElseThrow(()-> new CommonCodeNotFoundException(codeName));
    }

    public CommonCode codeToCommonCode(String code) {
        List<CommonCode> caches = commonCodeCacheService.getAllCommonCodes();

        return caches.stream()
                .filter(cache -> code.equals(cache.getName()))
                .findFirst()
                .orElseThrow(()-> new CommonCodeNotFoundException(code));
    }

    public List<CommonCode> codeGroupNameToCommonCodes(String groupName) {
        List<CommonCode> caches = commonCodeCacheService.getAllCommonCodes();

        return caches.stream()
                .filter(cache -> groupName.equals(cache.getCodeGroup().getGroupName())).toList();
    }

    public List<CommonCode> codeGroupToCommonCodes(String codeGroup) {
        List<CommonCode> caches = commonCodeCacheService.getAllCommonCodes();

        return caches.stream()
                .filter(cache -> codeGroup.equals(cache.getCodeGroup().getCodeGroup())).toList();
    }

    public String codeToCommonCodeName(String code) {
        List<CommonCode> caches = commonCodeCacheService.getAllCommonCodes();

        return caches.stream()
                .filter(cache -> code.equals(cache.getCode()))
                .map(CommonCode::getName)
                .findFirst()
                .orElseThrow(() -> new CommonCodeNotFoundException(code));
    }
    public List<CommonCodeGroup> parentCodeGroupToCodeGroups(String parentCodeGroup) {
        List<CommonCodeGroup> caches = commonCodeCacheService.getAllCommonCodeGroups();
        return caches.stream()
                .filter(cache -> cache.getParent()!=null && parentCodeGroup.equals(cache.getParent().getCodeGroup())).toList();
    }
}