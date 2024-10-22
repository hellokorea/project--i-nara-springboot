package com.eureka.mindbloom.trait.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/trait")
public class TraitController {



    @GetMapping
    public void getTraitQnAList() {

    }

}
