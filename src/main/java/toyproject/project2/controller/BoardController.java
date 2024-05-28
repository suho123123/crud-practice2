package toyproject.project2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toyproject.project2.dto.BoardForm;
import toyproject.project2.dto.CommentDTO;
import toyproject.project2.service.BoardService;
import toyproject.project2.service.CommentService;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm() {

        return "saveForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("boardForm") BoardForm boardForm) throws IOException {

        log.info("BoardController");
        boardService.save(boardForm);
        return "index";
    }

    @GetMapping("/list")
    public String list(Model model) {

        List<BoardForm> boardForms = boardService.findAll();
        model.addAttribute("boardForms", boardForms);

        return "boardList";
    }

    @GetMapping("/{boardId}")
    public String board(@PathVariable("boardId") Long boardId, Model model,
                        @PageableDefault(page = 1) Pageable pageable) {

        boardService.updateHits(boardId);
        BoardForm boardForm = boardService.findById(boardId);

        // 댓글 목록 가져오기
        List<CommentDTO> commentDTOList = commentService.findAll(boardId);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardForm);
        model.addAttribute("page", pageable.getPageNumber());

        return "board";
    }

    @GetMapping("/update/{boardId}")
    public String boardUpdateForm(@PathVariable("boardId") Long boardId, Model model) {

        BoardForm boardForm = boardService.findById(boardId);
        model.addAttribute("boardUpdate", boardForm);

        return "boardUpdateForm";
    }

    @PostMapping("/update/{boardId}")
    public String updateBoard(@ModelAttribute("boardForm") BoardForm boardForm, Model model) {

        BoardForm board = boardService.update(boardForm);
        model.addAttribute("board", board);

        return "board";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.delete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {

        Page<BoardForm> boardForms = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), boardForms.getTotalPages());

        model.addAttribute("boardList", boardForms);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }
}
