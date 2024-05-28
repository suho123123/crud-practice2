package toyproject.project2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.project2.dto.BoardForm;
import toyproject.project2.entity.Board;
import toyproject.project2.entity.BoardFile;
import toyproject.project2.repository.BoardFileRepository;
import toyproject.project2.repository.BoardRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardForm boardForm) throws IOException {

        // 파일 첨부 여부에 따라 로직을 분리해야함.
        if (boardForm.getBoardFile().isEmpty()) {
            log.info("empty");
            // 첨부 파일이 없을 때
            Board board = Board.toBoard(boardForm);
            boardRepository.save(board);
        } else {
            // 첨부 파일이 있을 때
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름을 가져옴
                3. 서버 저장용 이름을 만듦(내사진.jpg -> 12412535323.jpg)
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            log.info("image?");

            Board board =  Board.toSaveFileEntity(boardForm);
            Long savedId = boardRepository.save(board).getId();
            Board findBoard = boardRepository.findById(savedId).get(); // id 값이 있는 board

            List<MultipartFile> boardFiles = boardForm.getBoardFile(); // 1.
            for (MultipartFile boardFile : boardFiles) {
                String originalFilename = boardFile.getOriginalFilename(); // 2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
                String savePath = "/Users/yousuho/toyproject/imgFile/" + storedFileName; // 4.
                boardFile.transferTo(new File(savePath)); // 5.

                BoardFile boardFileEntity = BoardFile.toBoardFileEntity(findBoard, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }

        }

    }

    public List<BoardForm> findAll() {

        List<Board> boards = boardRepository.findAll();
        List<BoardForm> boardForms = new ArrayList<>();

        for (Board board : boards) {
            boardForms.add(BoardForm.toBoardForm(board));
        }

        return boardForms;
    }

    @Transactional
    public void updateHits(Long boardId) {

        boardRepository.updateHits(boardId);
    }

    public BoardForm findById(Long boardId) {

        Optional<Board> result = boardRepository.findById(boardId);

        if (result.isPresent()) {
            Board board = result.get();
            return BoardForm.toBoardForm(board);
        } else {
            return null;
        }
    }

    public BoardForm update(BoardForm boardForm) {

        Board board = Board.toUpdateBoard(boardForm);
        boardRepository.save(board);

        return findById(boardForm.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardForm> paging(Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        Page<Board> boards =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardForm> boardForms =
                boards.map(board ->
                        new BoardForm(board.getId(), board.getBoardWriter(), board.getBoardTitle(),
                                board.getBoardHits(), board.getCreatedTime()));

        return boardForms;
    }
}
