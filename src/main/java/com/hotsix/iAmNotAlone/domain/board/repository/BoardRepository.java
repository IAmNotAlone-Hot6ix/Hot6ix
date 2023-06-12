package com.hotsix.iAmNotAlone.domain.board.repository;

import com.hotsix.iAmNotAlone.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
