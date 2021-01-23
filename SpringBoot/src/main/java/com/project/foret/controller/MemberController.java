package com.project.foret.controller;

import com.project.foret.entity.Member;
import com.project.foret.model.MemberModel;
import com.project.foret.repository.MemberRepository;
import com.project.foret.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @PostMapping("/create")
    public ResponseEntity<Object> createMember(Member member, MultipartFile[] files) throws Exception {
        return memberService.createMember(member, files);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateMember(@PathVariable Long id, Member member, MultipartFile[] files) throws Exception {
        return memberService.updateMember(id, member, files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        return memberService.deleteMember(id);
    }

    @GetMapping("/details/{id}")
    public MemberModel getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    @GetMapping("/all")
    public List<MemberModel> getMembers() {
        return memberService.getMembers();
    }
}
