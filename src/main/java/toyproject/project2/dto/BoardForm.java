package toyproject.project2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import toyproject.project2.entity.Board;
import toyproject.project2.entity.BoardFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {

    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;

    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // 파일을 담는 용도.
    private List<String> originalFileNames; // 원본 파일 이름.
    private List<String> storedFileNames; // 서버 저장용 파일 이름.
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0) 일종의 flag 값.

    public BoardForm(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardForm toBoardForm(Board board) {

        BoardForm boardForm = new BoardForm();
        boardForm.setId(board.getId());
        boardForm.setBoardWriter(board.getBoardWriter());
        boardForm.setBoardPass(board.getBoardPass());
        boardForm.setBoardTitle(board.getBoardTitle());
        boardForm.setBoardContents(board.getBoardContents());
        boardForm.setBoardHits(board.getBoardHits());
        boardForm.setBoardCreatedTime(board.getCreatedTime());
        boardForm.setBoardUpdatedTime(board.getUpdatedTime());

        if (board.getFileAttached() == 0) {
            boardForm.setFileAttached(board.getFileAttached()); // 0
        } else {
            List<String> originalFileNames = new ArrayList<>();
            List<String> storedFileNames = new ArrayList<>();
            boardForm.setFileAttached(board.getFileAttached()); // 1

            for (BoardFile boardFile : board.getBoardFiles()) {

                originalFileNames.add(boardFile.getOriginalFileName());
                storedFileNames.add(boardFile.getStoredFileName());
            }
            boardForm.setOriginalFileNames(originalFileNames);
            boardForm.setStoredFileNames(storedFileNames);
        }

        return boardForm;
    }
}
