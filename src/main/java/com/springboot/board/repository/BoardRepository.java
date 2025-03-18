package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//public interface BoardRepository extends JpaRepository<Board, Long> {
//    Optional<Board> findById(long boardId);
//
//    Page<Board> findByBoardStatusNot(Board.BoardStatus boardStatus, Pageable pageable);
//}
