package com.eureka.mindbloom.commoncode.service;

import com.eureka.mindbloom.commoncode.domain.CommonCode;
import com.eureka.mindbloom.commoncode.domain.CommonCodeGroup;
import com.eureka.mindbloom.commoncode.repository.CommonCodeGroupRepository;
import com.eureka.mindbloom.commoncode.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeCacheService {

    private final CommonCodeRepository codeRepository;
    private final CommonCodeGroupRepository groupRepository;

    @Cacheable(cacheNames = "commonCode", cacheManager = "commonCodeCacheManager")
    public List<CommonCode> getAllCommonCodes() {
        return codeRepository.findAll();
    }

    @Cacheable(cacheNames = "codeGroup", cacheManager = "commonCodeCacheManager")
    public List<CommonCodeGroup> getAllCommonCodeGroups() {
        return groupRepository.findAll();
    }
}