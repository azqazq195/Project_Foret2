package com.project.foret.repository;

import com.project.foret.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value =
            "(SELECT * " +
            "FROM board WHERE foret_id = ?1 AND TYPE = 1 " +
            "ORDER BY id DESC " +
            "LIMIT 5) " +
            "UNION ALL " +
            "(SELECT * " +
            "FROM board WHERE foret_id = ?1 AND TYPE = 3 " +
            "ORDER BY id DESC " +
            "LIMIT 5 )",
    nativeQuery = true)
    List<Board> findTop5ByForetIdOrderByIdDesc(Long foret_id);
}
