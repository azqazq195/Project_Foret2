package com.project.foret.service;

import com.project.foret.entity.*;
import com.project.foret.model.BoardModel;
import com.project.foret.repository.BoardPhotoRepository;
import com.project.foret.repository.BoardRepository;
import com.project.foret.repository.ForetRepository;
import com.project.foret.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;
    private BoardPhotoRepository boardPhotoRepository;
    private MemberRepository memberRepository;

    public ResponseEntity<Object> createBoard(Long member_id, Long foret_id, Board board, MultipartFile[] files) throws Exception {
        Board newBoard = new Board();
        newBoard.setType(board.getType());
        newBoard.setSubject(board.getSubject());
        newBoard.setContent(board.getContent());
        if (files != null) {
            for (MultipartFile photo : files) {
                String dir = System.getProperty("user.dir") + "/src/main/resources/storage/board";
                String originname = photo.getOriginalFilename();
                String filename = photo.getOriginalFilename();
                int lastIndex = originname.lastIndexOf(".");
                String filetype = originname.substring(lastIndex + 1);
                int filesize = (int) photo.getSize();
                if (!new File(dir).exists()) {
                    new File(dir).mkdirs();
                }
                File file = new File(dir, filename);
                FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

                BoardPhoto boardPhoto = new BoardPhoto();
                boardPhoto.setDir(dir);
                boardPhoto.setOriginname(originname);
                boardPhoto.setFilename(filename);
                boardPhoto.setFiletype(filetype);
                boardPhoto.setFilesize(filesize);
                newBoard.addPhoto(boardPhoto);
            }
        }
        newBoard.setMember(new Member(member_id));
        if (foret_id != null) {
            newBoard.setForet(new Foret(foret_id));
        }
        Board savedBoard = boardRepository.save(newBoard);
        if (boardRepository.findById(savedBoard.getId()).isPresent()) {
            return ResponseEntity.ok("게시글생성 성공");
        } else return ResponseEntity.unprocessableEntity().body("게시글생성 실패");
    }

    @Transactional
    public ResponseEntity<Object> updateBoard(Long id, Long member_id, Board board, MultipartFile[] files) throws Exception {
        if (boardRepository.findById(id).isPresent()) {
            Board updateBoard = boardRepository.findById(id).get();
            if (updateBoard.getMember().getId().equals(member_id)) {
                boardPhotoRepository.deleteByBoardId(id);

                updateBoard.setType(board.getType());
                updateBoard.setHit(board.getHit());
                updateBoard.setSubject(board.getSubject());
                updateBoard.setContent(board.getContent());
                updateBoard.setEdit_date(Timestamp.valueOf(LocalDateTime.now()));
                if (files != null) {
                    for (MultipartFile photo : files) {
                        String dir = System.getProperty("user.dir") + "/src/main/resources/storage/board";
                        String originname = photo.getOriginalFilename();
                        String filename = photo.getOriginalFilename();
                        int lastIndex = originname.lastIndexOf(".");
                        String filetype = originname.substring(lastIndex + 1);
                        int filesize = (int) photo.getSize();
                        if (!new File(dir).exists()) {
                            new File(dir).mkdirs();
                        }
                        File file = new File(dir, filename);
                        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));

                        BoardPhoto boardPhoto = new BoardPhoto();
                        boardPhoto.setDir(dir);
                        boardPhoto.setOriginname(originname);
                        boardPhoto.setFilename(filename);
                        boardPhoto.setFiletype(filetype);
                        boardPhoto.setFilesize(filesize);
                        updateBoard.addPhoto(boardPhoto);
                    }
                }
                Board savedBoard = boardRepository.save(updateBoard);
                if (boardRepository.findById(savedBoard.getId()).isPresent()) {
                    return ResponseEntity.ok("게시글수정 성공");
                } else return ResponseEntity.unprocessableEntity().body("게시글수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("게시글 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("게시글을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteBoard(Long id) {
        if(boardRepository.findById(id).isPresent()) {
            boardRepository.deleteById(id);
            if(boardRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("게시글삭제 실패");
            else return ResponseEntity.ok().body("게시글삭제 성공");
        } else return ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다.");
    }
//
//    public BoardModel getBoard(@PathVariable Long id) {
//    }
}
