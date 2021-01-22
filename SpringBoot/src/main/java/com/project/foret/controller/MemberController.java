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
    private MemberRepository memberRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> createMember(@RequestBody Member member, MultipartFile[] files) {
        return memberService.createMember(member);
    }

    @GetMapping("/details/{id}")
    public MemberModel getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    @GetMapping("/all")
    public List<MemberModel> getMembers() {
        return memberService.getMembers();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return memberService.updateMember(member, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        return memberService.deleteMember(id);
    }
}
