package com.project.foret.service;

import com.project.foret.entity.*;
import com.project.foret.model.*;
import com.project.foret.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ForetService {

    private ForetRepository foretRepository;
    private ForetPhotoRepository foretPhotoRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;
    private MemberRepository memberRepository;

    private MemberService memberService;

    public ResponseEntity<Object> createForet(Long member_id, Foret model, MultipartFile[] files) throws Exception {
        if (memberRepository.findById(member_id).isPresent()) {
            Foret newForet = new Foret();
            newForet.setName(model.getName());
            newForet.setIntroduce(model.getIntroduce());
            newForet.setMax_member(model.getMax_member());
            newForet.setLeader_id(member_id);
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    newForet.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    newForet.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            if (files != null) {
                for (MultipartFile photo : files) {
                    String dir = System.getProperty("user.dir") + "/src/main/resources/storage/foret";
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

                    ForetPhoto foretPhoto = new ForetPhoto();
                    foretPhoto.setDir(dir);
                    foretPhoto.setOriginname(originname);
                    foretPhoto.setFilename(filename);
                    foretPhoto.setFiletype(filetype);
                    foretPhoto.setFilesize(filesize);
                    newForet.addPhoto(foretPhoto);
                }
            }
            newForet.addMember(memberRepository.findById(member_id).get());
            Foret savedForet = foretRepository.save(newForet);
            if (foretRepository.findById(savedForet.getId()).isPresent()) {
                return ResponseEntity.ok("포레생성 성공");
            } else return ResponseEntity.unprocessableEntity().body("포레생성 실패");
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 회원입니다.");
        }
    }

    @Transactional
    public ResponseEntity<Object> updateForet(Long id, Long member_id, Foret model, MultipartFile[] files) throws Exception {
        if (foretRepository.findById(id).isPresent()) {
            Foret updateForet = foretRepository.findById(id).get();
            foretPhotoRepository.deleteByForetId(id);
            if (updateForet.getLeader_id().equals(member_id)){
                updateForet.setName(model.getName());
                updateForet.setIntroduce(model.getIntroduce());
                updateForet.setMax_member(model.getMax_member());
                updateForet.setTags(null);
                updateForet.setRegions(null);
                updateForet.setPhotos(null);
                if (model.getTags() != null) {
                    for (Tag tag : model.getTags()) {
                        updateForet.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                    }
                }
                if (model.getRegions() != null) {
                    for (Region region : model.getRegions()) {
                        updateForet.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                    }
                }
                if (files != null) {
                    for (MultipartFile photo : files) {
                        String dir = System.getProperty("user.dir") + "/src/main/resources/storage/foret";
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

                        ForetPhoto foretPhoto = new ForetPhoto();
                        foretPhoto.setDir(dir);
                        foretPhoto.setOriginname(originname);
                        foretPhoto.setFilename(filename);
                        foretPhoto.setFiletype(filetype);
                        foretPhoto.setFilesize(filesize);
                        updateForet.addPhoto(foretPhoto);
                    }
                }
                Foret savedForet = foretRepository.save(updateForet);
                if (foretRepository.findById(savedForet.getId()).isPresent())
                    return ResponseEntity.ok("포레수정 성공");
                else return ResponseEntity.unprocessableEntity().body("포레수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("포레 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("포레를 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteForet(Long id) {
        if (foretRepository.findById(id).isPresent()) {
            foretRepository.deleteById(id);
            if (foretRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("포레삭제 실패");
            else return ResponseEntity.ok().body("포레삭제 성공");
        } else return ResponseEntity.badRequest().body("포레를 찾을 수 없습니다.");
    }

    public ForetModel getForet(Long id) {
        if (foretRepository.findById(id).isPresent()) {
            Foret foret = foretRepository.findById(id).get();
            ForetModel foretModel = new ForetModel();
            foretModel.setName(foret.getName());
            foretModel.setIntroduce(foret.getIntroduce());
            foretModel.setMax_member(foret.getMax_member());
            foretModel.setReg_date(foret.getReg_date());
            foretModel.setTags(getTagList(foret));
            foretModel.setRegions(getRegionList(foret));
            foretModel.setPhotos(getPhotoList(foret));
            foretModel.setMembers(getMemberList(foret));
            return foretModel;
        } else return null;
    }

    public List<ForetModel> getForets() {
        List<Foret> foretList = foretRepository.findAll();
        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return foretModels;
        } else return new ArrayList<>();
    }

    public List<ForetModel> getMyForets(Long member_id) {
        List<Foret> foretList = foretRepository.findByMembersId(member_id);
        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return foretModels;
        } else return new ArrayList<>();
    }

    private List<TagModel> getTagList(Foret foret) {
        List<TagModel> tagList = new ArrayList<>();
        if (foret.getTags() != null && foret.getTags().size() != 0) {
            for (Tag tag : foret.getTags()) {
                TagModel tagModel = new TagModel();
                tagModel.setTagName(tag.getTagName());
                tagList.add(tagModel);
            }
            return tagList;
        } else return null;
    }

    private List<RegionModel> getRegionList(Foret foret) {
        List<RegionModel> regionList = new ArrayList<>();
        if (foret.getRegions() != null && foret.getRegions().size() != 0) {
            for (Region region : foret.getRegions()) {
                RegionModel regionModel = new RegionModel();
                regionModel.setRegionSi(region.getRegionSi());
                regionModel.setRegionGu(region.getRegionGu());
                regionList.add(regionModel);
            }
            return regionList;
        } else return null;
    }

    private List<PhotoModel> getPhotoList(Foret foret) {
        List<PhotoModel> photoList = new ArrayList<>();
        if (foret.getPhotos() != null && foret.getPhotos().size() != 0) {
            for (ForetPhoto foretPhoto : foret.getPhotos()) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setDir(foretPhoto.getDir());
                photoModel.setFilename(foretPhoto.getFilename());
                photoModel.setOriginname(foretPhoto.getOriginname());
                photoModel.setFilesize(foretPhoto.getFilesize());
                photoModel.setFiletype(foretPhoto.getFiletype());
                photoModel.setReg_date(foretPhoto.getReg_date());
                photoList.add(photoModel);
            }
            return photoList;
        } else return null;
    }

    private List<MemberModel> getMemberList(Foret foret) {
        List<MemberModel> memberList = new ArrayList<>();
        if (foret.getMembers() != null && foret.getMembers().size() != 0) {
            for (Member member : foret.getMembers()) {
                memberList.add(memberService.getMember(member.getId()));
            }
            return memberList;
        } else return null;
    }
}
