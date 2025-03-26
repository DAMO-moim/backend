package com.springboot.comment.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus commentStatus = CommentStatus.COMMENT_POST;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // 영속성 전이, 동기화
    public void setBoard(Board board) {
        this.board = board;
        if (!board.getComments().contains(this)) {
            board.setComment(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getComments().contains(this)) {
            member.setComment(this);
        }
    }

    public enum CommentStatus {
        COMMENT_POST("댓글 등록 상태"),
        COMMENT_DELETE("댓글 삭제 상태");

        @Getter
        private String status;

        CommentStatus(String status) {
            this.status = status;
        }
    }
}
