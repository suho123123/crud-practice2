package toyproject.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.project2.entity.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
