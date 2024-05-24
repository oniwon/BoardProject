package org.example.boardtest.service;

import lombok.RequiredArgsConstructor;
import org.example.boardtest.domain.Board;
import org.example.boardtest.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //게시글 목록 찾기
    public Page<Board> findAllBoards(Pageable pageable) {
        Pageable sortedByDesId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findAll(sortedByDesId);
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    //게시글 등록
    @Transactional
    public Board saveBoard(Board board) {
        if (board.getId() == null) {
            board.setCreatedAt(LocalDateTime.now());
        }
        board.setUpdatedAt(LocalDateTime.now());
        board.setCreatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    //특정 게시글 삭제
    @Transactional
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String checkPassword(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        return board.getPassword();
    }


}
