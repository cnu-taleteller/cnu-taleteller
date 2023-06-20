package com.cnu.taleteller.backend.domain.book.service;

import com.cnu.taleteller.backend.domain.book.dto.ReplyDto;
import com.cnu.taleteller.backend.domain.book.entity.Book;
import com.cnu.taleteller.backend.domain.book.entity.Reply;
import com.cnu.taleteller.backend.domain.book.repository.ReplyRepository;
import com.cnu.taleteller.backend.domain.user.Repository.MemberRepository;
import com.cnu.taleteller.backend.domain.user.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReplyRepository replyRepository;

    public Reply saveReply(ReplyDto replyDto) {
        Member member = memberRepository.findByMemberEmail(replyDto.getMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));
        replyDto.setMember(member);
        Reply reply = replyRepository.save(replyDto.toEntity());
        return reply;
    }

    public List<Reply> getRepliesByBook(Book bookId) {
        List<Reply> replies = replyRepository.findByBookId(bookId);
        return replies;
    }

    public Reply editReply(Long replyId, ReplyDto replyDto) {
        Reply updatedReply = replyRepository.findById(replyId).orElse(null);
        Reply reply = updatedReply;
        reply.updateReply(replyId, replyDto.getReplyContent());
        reply = replyRepository.save(reply);
        return reply;
    }

    public boolean deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if (reply != null) {
            replyRepository.delete(reply);
            return true;
        }
        return false;
    }
}