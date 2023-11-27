package com.example.bai_thi_module4.controller;

import com.example.bai_thi_module4.service.author.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final AuthorService authorService;


    @GetMapping
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("index");
        view.addObject("authors", authorService.findAll());
        return view;
    }
}
