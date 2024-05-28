package toyproject.project2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import toyproject.project2.dto.BoardForm;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String boardWriter;
    @Column(length = 10)
    private String boardPass;
    private String boardTitle;
    @Column(length = 500)
    private String boardContents;
    private int boardHits;
    private int fileAttached; // 1 or 0

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardFile> boardFiles = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public static Board toBoard(BoardForm boardForm) {

        Board board = new Board();
        board.setBoardWriter(boardForm.getBoardWriter());
        board.setBoardPass(boardForm.getBoardPass());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setBoardContents(boardForm.getBoardContents());
        board.setBoardHits(0);
        board.setFileAttached(0); // 파일이 없다.

        return board;
    }

    public static Board toUpdateBoard(BoardForm boardForm) {

        Board board = new Board();
        board.setId(boardForm.getId());
        board.setBoardWriter(boardForm.getBoardWriter());
        board.setBoardPass(boardForm.getBoardPass());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setBoardContents(boardForm.getBoardContents());
        board.setBoardHits(boardForm.getBoardHits());

        return board;
    }

    public static Board toSaveFileEntity(BoardForm boardForm) {

        Board board = new Board();
        board.setBoardWriter(boardForm.getBoardWriter());
        board.setBoardPass(boardForm.getBoardPass());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setBoardContents(boardForm.getBoardContents());
        board.setBoardHits(0);
        board.setFileAttached(1); // 파일이 있다.

        return board;
    }
}
