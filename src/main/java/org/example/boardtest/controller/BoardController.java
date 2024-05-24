package org.example.boardtest.controller;


import lombok.RequiredArgsConstructor;
import org.example.boardtest.domain.Board;
import org.example.boardtest.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    //게시글 목록 보기
    @GetMapping("/list")
    public String boards(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "boards/list";
    }


    //게시글 상세 조회
    @GetMapping("/view/{id}")
    public String detailFriend(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);

        return "boards/view";
    }

    //게시글 등록 폼
    @GetMapping("/write")
    public String addForm(Model model) {
        model.addAttribute("board", new Board());
        return "boards/writerForm";
    }

    //게시글 등록
    @PostMapping("/write")
    public String addBoard(@ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("message", "등록 완료!");

        return "redirect:/boards/list";
    }


    //게시글 삭제 폼
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findBoardById(id));
        return "boards/deleteForm";
    }

    //게시글 삭제
    @PostMapping("/delete/{id}")
    public String deleteBoard(@ModelAttribute Board board, @RequestParam Long id, @RequestParam String password, RedirectAttributes redirectAttributes) {
        String storedPassword = boardService.checkPassword(id);
        if(storedPassword.equals(password)) {
            boardService.deleteBoardById(id);
            return "redirect:/boards/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/boards/delete/" + board.getId();
        }
    }


    //게시글 수정 폼
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "boards/updateForm";
    }

    //게시글 수정
    @PostMapping("/update/{id}")
    public String editBoard(@ModelAttribute Board board, @RequestParam Long id, @RequestParam String password, RedirectAttributes redirectAttributes) {
        String storedPassword = boardService.checkPassword(id);
        if(storedPassword.equals(password)) {
            boardService.saveBoard(board);
            return "redirect:/boards/view/" + board.getId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/boards/update/" + board.getId();
        }
    }

}
