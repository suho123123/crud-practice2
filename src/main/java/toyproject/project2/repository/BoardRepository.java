package toyproject.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toyproject.project2.dto.BoardForm;
import toyproject.project2.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query(value = "update Board b set b.boardHits = b.boardHits + 1 where b.id = :id")
    void updateHits(@Param("id") Long id);
}
