package com.cnu.taleteller.backend.domain.book.controller;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.service.BookService;
import com.cnu.taleteller.backend.domain.tool.entity.mongo.Page;
import com.cnu.taleteller.backend.domain.tool.service.ToolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    private final ToolService toolService;

    @PostMapping
    public ResponseEntity<?> saveSubmit(@RequestBody BookDto dto) {
        bookService.submitBook(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tmp")
    public Long saveTmp(@RequestBody BookTempSaveDto dto) {
        Page[] pageList = dto.getPageList();
        ObjectId objectId = toolService.saveBook(pageList);
        return bookService.saveBook(dto, objectId.toString());
    }

    @PostMapping("/thumbnail")
    public ResponseEntity<?> saveThumbnail(@RequestBody BookDto dto) {
        bookService.saveThumbnail(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{bookId}")
    public Long update(@RequestBody BookTempSaveDto dto, @PathVariable Long bookId) throws ExecutionException, InterruptedException {
        return bookService.updateBook(dto, bookId);
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

    @GetMapping("/mywork/{email}")
    public ResponseEntity<List<Book>> myworkSearch(@PathVariable String email) {
        System.out.println("작품관리 : "+email);
        List<Book> myworkSearch = bookService.findAllMyWork(email);
        return ResponseEntity.ok(myworkSearch);
    }

    @GetMapping("/mybookmark/{email}")
    public ResponseEntity<List<Book>> mybookmarkSearch(@PathVariable String email) {
        System.out.println("즐겨찾기 : "+email);
        List<Book> mybookmarkSearch = bookService.findAllMyBookmark(email);
        return ResponseEntity.ok(mybookmarkSearch);
    }
        
    @PostMapping("/userBookList")
    public List<Book> userBookList(@RequestBody String userEmail) throws UnsupportedEncodingException {
        String decodedEmail = URLDecoder.decode(userEmail, "UTF-8").replace("=", "");
        log.info("------" + decodedEmail);
        return bookService.findUserBookList(decodedEmail);
    }

    @DeleteMapping("/deleteBookList")
    public ResponseEntity<Void> deleteSelectedList(@RequestBody List<Long> selectedBookList) {
        log.info( "---" + selectedBookList);
        boolean successOrFail = bookService.deleteBookList(selectedBookList);

        if(successOrFail) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}