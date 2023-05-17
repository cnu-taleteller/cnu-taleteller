package com.cnu.taleteller.backend.domain.book.controller;

import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/")
    public ResponseEntity<Book> save(@RequestBody BookTempSaveDto dto) {
        Book book = bookService.saveBook(dto);
        System.out.println(book);

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Book> update(@RequestBody BookTempSaveDto dto, @PathVariable Long bookId) {
        Book book = bookService.updateBook(dto, bookId);
        System.out.println(book);

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(@RequestParam String searchType, @RequestParam String searchKeyword) {

        List<Book> searchResults;

        switch (searchType) {
/*            case "name":
                searchResults = bookService.searchByName(searchKeyword);
                break;*/
            case "title":
                searchResults = bookService.searchByTitle(searchKeyword);
                break;
            case "content":
                searchResults = bookService.searchByContent(searchKeyword);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/detail/{bookId}")
    public ResponseEntity<Book> getBookDetail(@PathVariable Long bookId) {
        Book book = bookService.getBookByBookId(bookId);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/detail/{bookId}/recommend")
    public ResponseEntity<BookDto> recommendBook(@PathVariable Long bookId, BookDto bookDto) {
        BookDto recommendBook = bookService.recommendBook(bookId, bookDto);
        if (recommendBook != null) {
            return ResponseEntity.ok(recommendBook);
        }
        return ResponseEntity.notFound().build();
    }

}