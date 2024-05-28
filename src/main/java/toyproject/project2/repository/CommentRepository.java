package toyproject.project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.project2.entity.Board;
import toyproject.project2.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoardOrderByIdAsc(Board board);
}
