package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //삭제 상태가 아닌것들만 조회
    Page<Board> findByBoardStatusNot(Board.BoardStatus boardStatus, Pageable pageable);

    Page<Board> findByMember(Member member, Pageable pageable);

    //해당 멤버의 카테고리별 게시글 조회
    Page<Board> findByMemberAndGroup_SubCategory_Category_CategoryName(
            Member member, String categoryName, Pageable pageable);
}
