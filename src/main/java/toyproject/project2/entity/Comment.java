package toyproject.project2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import toyproject.project2.dto.CommentDTO;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static Comment toSaveComment(CommentDTO commentDTO, Board board) {

        Comment comment = new Comment();
        comment.setCommentWriter(commentDTO.getCommentWriter());
        comment.setCommentContents(commentDTO.getCommentContents());
        comment.setBoard(board);

        return comment;
    }
}
