package com.cnu.taleteller.backend.domain.book.controller;

import com.cnu.taleteller.backend.domain.book.dto.BookmarkDto;
import com.cnu.taleteller.backend.domain.book.dto.RecommendDto;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.dto.BookDto;
import com.cnu.taleteller.backend.domain.book.dto.BookTempSaveDto;
import com.cnu.taleteller.backend.domain.book.entity.Bookmark;
import com.cnu.taleteller.backend.domain.book.entity.Recommend;
import com.cnu.taleteller.backend.domain.book.entity.Reply;
import com.cnu.taleteller.backend.domain.book.repository.BookRepository;
import com.cnu.taleteller.backend.domain.book.service.BookService;
import com.cnu.taleteller.backend.domain.book.service.ReplyService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    private final ToolService toolService;

    private final ReplyService replyService;

    private final BookRepository bookRepository;

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

    @PostMapping("/thumbnailScenario")
    public ResponseEntity<?> saveThumbnailScenario(@RequestBody BookDto dto) {
        bookService.saveThumbnailScenario(dto);
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
            case "name":
                searchResults = bookService.searchByName(searchKeyword);
                break;
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
    public ResponseEntity<Map<String, Object>> getBookAndReplies(@PathVariable Long bookId) {
        Book book = bookService.getBookByBookId(bookId);
        List<Reply> replies = replyService.getRepliesByBook(book);

        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("book", book);
        response.put("replies", replies);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/detail/{bookId}/recommend")
    public ResponseEntity<Recommend> recommendBook(@PathVariable Long bookId, @RequestBody RecommendDto recommendDto) {
        Recommend recommend = bookService.recommendBook(bookId, recommendDto);

        return new ResponseEntity<>(recommend, HttpStatus.OK);
    }

    @DeleteMapping("/detail/{bookId}/recommend")
    public ResponseEntity<Recommend> unrecommendBook(@PathVariable Long bookId, @RequestBody RecommendDto recommendDto) {
        Recommend recommend = bookService.unrecommendBook(bookId, recommendDto);

        return new ResponseEntity<>(recommend, HttpStatus.OK);
    }

    @PostMapping("/detail/{bookId}/bookmark")
    public ResponseEntity<Bookmark> bookmarkBook(@PathVariable Long bookId, @RequestBody BookmarkDto bookmarkDto) {
        Bookmark bookmark = bookService.bookmarkBook(bookId, bookmarkDto);

        return new ResponseEntity<>(bookmark, HttpStatus.OK);
    }

    @DeleteMapping("/detail/{bookId}/bookmark")
    public ResponseEntity<Bookmark> unbookmarkBook(@PathVariable Long bookId, @RequestBody BookmarkDto bookmarkDto) {
        Bookmark bookmark = bookService.unbookmarkBook(bookId, bookmarkDto);

        return new ResponseEntity<>(bookmark, HttpStatus.OK);
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

    @GetMapping("/list")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

}