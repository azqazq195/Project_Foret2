package com.project.foret.controller;

import com.project.foret.entity.Board;
import com.project.foret.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<Object> createBoard(
            @RequestParam Long member_id,
            @RequestParam @Nullable Long foret_id,
            Board board,
            MultipartFile[] files
    ) throws Exception {
        return boardService.createBoard(member_id, foret_id, board, files);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateBoard(
            @PathVariable Long id,
            @RequestParam Long member_id,
            Board board, MultipartFile[] files
    ) throws Exception {
        return boardService.updateBoard(id, member_id, board, files);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object> deleteBoard(@PathVariable Long id) {
//        return boardService.deleteBoard(id);
//    }
//
//    @GetMapping("/details/{id}")
//    public BoardModel getBoard(@PathVariable Long id) {
//        return boardService.getBoard(id);
//    }

}
