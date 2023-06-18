package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class IndexController {

    private final BookRepository bookRepository;

    @GetMapping("/")
    public String index() {
        return "Spring test! Hello C&U!";
    }

    @GetMapping("/api/")
    @CrossOrigin(origins = "http://localhost:8200/")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}