package com.project.foret.service;

import com.project.foret.entity.Board;
import com.project.foret.entity.Comment;
import com.project.foret.entity.Foret;
import com.project.foret.entity.Member;
import com.project.foret.model.CommentModel;
import com.project.foret.model.ForetModel;
import com.project.foret.model.MemberModel;
import com.project.foret.repository.BoardRepository;
import com.project.foret.repository.CommentRepository;
import com.project.foret.repository.MemberRepository;
import com.project.foret.response.ForetResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    BoardRepository boardRepository;
    MemberRepository memberRepository;
    MemberService memberService;

    public ResponseEntity<Object> create(Long member_id, Long board_id, Comment comment) {
        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setBoard(new Board(board_id));
        newComment.setMember(new Member(member_id));
        Comment saveComment = commentRepository.save(newComment);
        if (commentRepository.findById(saveComment.getId()).isPresent()) {
            saveComment.setGroup_id(saveComment.getId());
            commentRepository.save(saveComment);
            return ResponseEntity.ok("댓글생성 성공");
        } else return ResponseEntity.unprocessableEntity().body("댓글생성 실패");
    }

    public ResponseEntity<Object> createRe(Long id, Long member_id, Long board_id, Comment comment) {
        Comment newComment = new Comment();
        newComment.setGroup_id(id);
        newComment.setContent(comment.getContent());
        newComment.setBoard(new Board(board_id));
        newComment.setMember(new Member(member_id));
        Comment saveComment = commentRepository.save(newComment);
        if (commentRepository.findById(saveComment.getId()).isPresent()) {
            return ResponseEntity.ok("대댓글생성 성공");
        } else return ResponseEntity.unprocessableEntity().body("대댓글생성 실패");
    }

    public ResponseEntity<Object> update(Long id, Long member_id, Comment comment) {
        if (commentRepository.findById(id).isPresent()) {
            Comment updateComment = commentRepository.findById(id).get();
            if (updateComment.getMember().getId().equals(member_id)) {
                updateComment.setContent(comment.getContent());
                Comment savedComment = commentRepository.save(updateComment);
                if (commentRepository.findById(savedComment.getId()).isPresent()) {
                    return ResponseEntity.ok("댓글수정 성공");
                } else return ResponseEntity.unprocessableEntity().body("댓글수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("댓글 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("댓글을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> delete(Long id) {
        if (commentRepository.findById(id).isPresent()) {
            commentRepository.deleteById(id);
            if (commentRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("댓글삭제 실패");
            else return ResponseEntity.ok().body("댓글삭제 성공");
        } else return ResponseEntity.badRequest().body("댓글을 찾을 수 없습니다.");
    }

    public CommentModel getComment(Long id) {
        if (commentRepository.findById(id).isPresent()) {
            Comment comment = commentRepository.findById(id).get();
            CommentModel commentModel = new CommentModel();
            commentModel.setId(comment.getId());
            commentModel.setContent(comment.getContent());
            commentModel.setReg_date(comment.getReg_date());
            commentModel.setGroup_id(comment.getGroup_id());
            commentModel.setMember(getMember(comment));
            return commentModel;
        } else return null;
    }

    public List<CommentModel> getComments(Long board_id) {
        List<Comment> commentList = commentRepository.findAllByBoardIdOrderByGroupIdAscIdAsc(board_id);
        if(commentList.size() > 0){
            List<CommentModel> commentModels = new ArrayList<>();
            for(Comment comment : commentList){
                CommentModel commentModel = new CommentModel();
                commentModel.setId(comment.getId());
                commentModel.setContent(comment.getContent());
                commentModel.setReg_date(comment.getReg_date());
                commentModel.setGroup_id(comment.getGroup_id());
                commentModel.setMember(getMember(comment));
                commentModels.add(commentModel);
            }
            return commentModels;
        } else return null;
    }

    private MemberModel getMember(Comment comment) {
        return memberService.getMember(comment.getMember().getId());
    }

}
