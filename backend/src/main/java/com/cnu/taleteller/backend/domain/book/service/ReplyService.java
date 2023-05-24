package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.entity.Reply;
import com.cnu.taleteller.backend.domain.book.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    public void saveReply(Reply reply) {
        replyRepository.save(reply);
    }

}