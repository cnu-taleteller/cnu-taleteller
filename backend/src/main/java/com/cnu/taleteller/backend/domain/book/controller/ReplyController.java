package com.cnu.taleteller.backend.domain.book.controller;


import com.cnu.taleteller.backend.domain.book.entity.Reply;
import com.cnu.taleteller.backend.domain.book.dto.ReplyDto;
import com.cnu.taleteller.backend.domain.book.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/book")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping("/detail/reply")
    public ResponseEntity<Reply> saveReply(@RequestBody ReplyDto replyDto) {
        Reply reply = replyService.saveReply(replyDto);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

    @PutMapping("/detail/reply/{replyId}")
    public ResponseEntity<Reply> editReply(@PathVariable Long replyId, @RequestBody ReplyDto replyDto) {
        Reply reply = replyService.editReply(replyId, replyDto);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }

    @DeleteMapping("/detail/reply/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        boolean reply = replyService.deleteReply(replyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}