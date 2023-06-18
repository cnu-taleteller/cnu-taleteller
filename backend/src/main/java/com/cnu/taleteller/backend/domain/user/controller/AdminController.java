package com.cnu.taleteller.backend.domain.user.controller;

import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.service.BookService;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import com.cnu.taleteller.backend.domain.user.dto.BookManagementDto;
import com.cnu.taleteller.backend.domain.user.dto.MemberManagementDto;
import com.cnu.taleteller.backend.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final BookService bookService;

    @GetMapping("/member")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @PostMapping("/member/suspend/{memberId}")
    public ResponseEntity<Member> suspendMember(@PathVariable Long memberId, @RequestBody MemberManagementDto memberManagementDto) {
        Member member = memberService.suspend(memberId, memberManagementDto);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping("/book")
    public List<Book> getBooks() {
        return bookService.getAllNonTempBooks();
    }

    @PostMapping("/book/public/{bookId}")
    public ResponseEntity<Book> setPublicBook(@PathVariable Long bookId, @RequestBody BookManagementDto bookManagementDto) {
        Book book = bookService.setPublic(bookId, bookManagementDto);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

}