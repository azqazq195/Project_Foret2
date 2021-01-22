package com.project.foret.service;

import com.project.foret.entity.Member;
import com.project.foret.entity.Region;
import com.project.foret.entity.Tag;
import com.project.foret.model.MemberModel;
import com.project.foret.model.RegionModel;
import com.project.foret.model.TagModel;
import com.project.foret.repository.MemberRepository;
import com.project.foret.repository.RegionRepository;
import com.project.foret.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;

    public ResponseEntity<Object> createMember(Member model) {
        Member newMember = new Member();
        if (memberRepository.findByEmail(model.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 사용중인 이메일 입니다. 회원가입 실패");
        } else {
            newMember.setName(model.getName());
            newMember.setEmail(model.getEmail());
            newMember.setPassword(model.getPassword());
            newMember.setNickname(model.getNickname());
            newMember.setBirth(model.getBirth());
            newMember.setDeviceToken(model.getDeviceToken());
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    newMember.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    newMember.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            // 사진, 포레
            Member savedMember = memberRepository.save(newMember);
            if (memberRepository.findById(savedMember.getId()).isPresent())
                return ResponseEntity.ok("회원가입 성공");
            else return ResponseEntity.unprocessableEntity().body("회원가입 실패");
        }
    }

    @Transactional
    public ResponseEntity<Object> updateMember(Member model, Long id) {
        if (memberRepository.findById(id).isPresent()) {
            Member updateMember = memberRepository.findById(id).get();
            updateMember.setName(model.getName());
            updateMember.setEmail(model.getEmail());
            updateMember.setPassword(model.getPassword());
            updateMember.setNickname(model.getNickname());
            updateMember.setBirth(model.getBirth());
            updateMember.setDeviceToken(model.getDeviceToken());
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    updateMember.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    updateMember.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            // 사진, 포레
            Member savedMember = memberRepository.save(updateMember);
            if (memberRepository.findById(savedMember.getId()).isPresent())
                return ResponseEntity.ok("회원수정 성공");
            else return ResponseEntity.unprocessableEntity().body("회원수정 실패");
        } else return ResponseEntity.unprocessableEntity().body("회원을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteMember(Long id) {
        if (memberRepository.findById(id).isPresent()) {
            memberRepository.deleteById(id);
            if (memberRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("회원삭제 실패");
            else return ResponseEntity.ok().body("회원삭제 성공");
        } else return ResponseEntity.badRequest().body("회원을 찾을 수 없습니다.");
    }

    public MemberModel getMember(Long id) {
        if (memberRepository.findById(id).isPresent()) {
            Member member = memberRepository.findById(id).get();
            MemberModel memberModel = new MemberModel();
            memberModel.setName(member.getName());
            memberModel.setEmail(member.getEmail());
            memberModel.setPassword(member.getPassword());
            memberModel.setNickname(member.getNickname());
            memberModel.setBirth(member.getBirth());
            memberModel.setDeviceToken(member.getDeviceToken());
            memberModel.setReg_date(member.getReg_date());
            memberModel.setTags(getTagList(member));
            memberModel.setRegions(getRegionList(member));
            return memberModel;
        } else return null;
    }

    public List<MemberModel> getMembers() {
        List<Member> memberList = memberRepository.findAll();
        if (memberList.size() > 0) {
            List<MemberModel> memberModels = new ArrayList<>();
            for (Member member : memberList) {
                MemberModel memberModel = new MemberModel();
                memberModel.setName(member.getName());
                memberModel.setEmail(member.getEmail());
                memberModel.setPassword(member.getPassword());
                memberModel.setNickname(member.getNickname());
                memberModel.setBirth(member.getBirth());
                memberModel.setDeviceToken(member.getDeviceToken());
                memberModel.setReg_date(member.getReg_date());
                memberModel.setTags(getTagList(member));
                memberModel.setRegions(getRegionList(member));
                memberModels.add(memberModel);
            }
            return memberModels;
        } else return new ArrayList<>();
    }

    // 이걸로 안하면 태그안의 멤버타고 재귀 순회
    private List<TagModel> getTagList(Member member) {
        List<TagModel> tagList = new ArrayList<>();
        for (Tag tag : member.getTags()) {
            TagModel tagModel = new TagModel();
            tagModel.setTagName(tag.getTagName());
            tagList.add(tagModel);
        }
        return tagList;
    }

    private List<RegionModel> getRegionList(Member member) {
        List<RegionModel> regionList = new ArrayList<>();
        for (Region region : member.getRegions()) {
            RegionModel regionModel = new RegionModel();
            regionModel.setRegionSi(region.getRegionSi());
            regionModel.setRegionGu(region.getRegionGu());
            regionList.add(regionModel);
        }
        return regionList;
    }
}
