package com.springboot.comment.repository;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //삭제 상태가 아닌것들만 조회
    Page<Comment> findByBoardAndCommentStatusNot(Board board, Comment.CommentStatus commentStatus, Pageable pageable);
    //해당 멤버의 댓글삭제가 아닌것들을 조회
    Page<Comment> findByMemberAndCommentStatusNot(Member member, Comment.CommentStatus status, Pageable pageable);
    Page<Comment> findByMember(Member member, Pageable pageable);
}
