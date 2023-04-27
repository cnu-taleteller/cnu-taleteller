package com.cnu.taleteller.backend.domain.book.controller;


import com.cnu.taleteller.backend.domain.book.domain.Book;
import com.cnu.taleteller.backend.domain.book.domain.Reply;
import com.cnu.taleteller.backend.domain.book.dto.ReplyDto;
import com.cnu.taleteller.backend.domain.book.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/book")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping("/detail/{bookId}/reply")
    public ResponseEntity<ReplyDto> saveReply(@RequestBody ReplyDto replyDto) {
        Reply reply = replyDto.toEntity();
        replyService.saveReply(reply);
        return new ResponseEntity<>(replyDto, HttpStatus.CREATED);
    }

}