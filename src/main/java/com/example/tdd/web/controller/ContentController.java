package com.example.tdd.web.controller;

import ch.qos.logback.core.model.Model;
import com.example.tdd.web.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

}
