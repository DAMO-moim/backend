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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @Column(nullable = false)
    private BoardStatus boardStatus = BoardStatus.BOARD_POST;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

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
