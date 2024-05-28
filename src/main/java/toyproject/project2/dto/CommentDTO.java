package toyproject.project2.dto;

import lombok.Data;
import toyproject.project2.entity.Comment;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(Comment comment) {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setCommentWriter(comment.getCommentWriter());
        commentDTO.setCommentContents(comment.getCommentContents());
        commentDTO.setCommentCreatedTime(comment.getCreatedTime());
        commentDTO.setBoardId(comment.getBoard().getId());

        return commentDTO;
    }
}
