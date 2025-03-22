package com.springboot.comment.repository;

import com.springboot.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //삭제 상태가 아닌것들만 조회
    Page<Comment> findByCommentStatusNot(Comment.CommentStatus commentStatus, Pageable pageable);
}
