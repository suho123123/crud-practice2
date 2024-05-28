package toyproject.project2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.project2.dto.CommentDTO;
import toyproject.project2.entity.Board;
import toyproject.project2.entity.Comment;
import toyproject.project2.repository.BoardRepository;
import toyproject.project2.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {

        Optional<Board> optionalBoard = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Comment comment = Comment.toSaveComment(commentDTO, board);
            return commentRepository.save(comment).getId();
        } else {
            return null;
        }
    }

    public List<CommentDTO> findAll(Long boardId) {

        Board board = boardRepository.findById(boardId).get();
        List<Comment> commentList = commentRepository.findAllByBoardOrderByIdAsc(board);

        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(comment);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }
}
