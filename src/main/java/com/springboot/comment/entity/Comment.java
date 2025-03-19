package com.springboot.comment.entity;

import com.springboot.audit.BaseEntity;
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

    @Column(nullable = false)
    private CommentStatus commentStatus = CommentStatus.COMMENT_POST;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

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
