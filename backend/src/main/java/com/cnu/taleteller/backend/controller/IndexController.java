package com.cnu.taleteller.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "Spring test! Hello C&U!";
    }
}