package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //삭제 상태가 아닌것들만 조회
    Page<Board> findByBoardStatusNot(Board.BoardStatus boardStatus, Pageable pageable);
}
