package io.github.delokoseni.rag_search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatPageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}