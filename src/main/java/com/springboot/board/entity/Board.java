package com.springboot.board.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.comment.entity.Comment;
import com.springboot.group.entity.Group;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    // 게시글 제목
    @Column(nullable = false)
    private String title;

    // 게시글 내용
    @Column(nullable = false)
    private String content;

    // 게시글 이미지
    @Column
    private String image;

    // 게시글 상태
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus boardStatus = BoardStatus.BOARD_POST;

    // 회원 과 게시글 1 : N -> 단방향, 게시글이 회원을 객체로 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 모임 과 게시글 1 : N -> 양방향, 게시글 쪽은 모임을 객체로 가짐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // 게시글과 댓글 1 : N -> 단방향, 게시글이 댓글을 리스트로 가짐
    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    // 영속성 전이, 동기화
    public void setGroup(Group group) {
        this.group = group;
        if (!group.getBoards().contains(this)) {
            group.setBoard(this);
        }
    }

    // 영속성 전이, 동기화
    public void setComment(Comment comment) {
        comments.add(comment);
        if (comment.getBoard() != this) {
            comment.setBoard(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getBoards().contains(this)) {
            member.setBoard(this);
        }
    }


    public enum BoardStatus {
        BOARD_POST("게시글 등록 상태"),
        BOARD_DELETE("게시글 삭제 상태");

        @Getter
        private String status;

        BoardStatus(String status) {
            this.status = status;
        }
    }
}
