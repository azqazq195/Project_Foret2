package com.project.foret.controller;

import com.project.foret.entity.Foret;
import com.project.foret.model.ForetModel;
import com.project.foret.model.MemberModel;
import com.project.foret.service.ForetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/foret")
public class ForetController {

    private ForetService foretService;

    @PostMapping("/create")
    public ResponseEntity<Object> createForet(
            @RequestParam Long member_id,
            Foret foret,
            MultipartFile[] files) throws Exception {
        return foretService.createForet(member_id, foret, files);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateForet(
            @PathVariable Long id,
            @RequestParam Long member_id,
            Foret foret,
            MultipartFile[] files) throws Exception {
        return foretService.updateForet(id, member_id, foret, files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteForet(@PathVariable Long id) {
        return foretService.deleteForet(id);
    }

    @GetMapping("/details/{id}")
    public ForetModel getForet(@PathVariable Long id){
        return foretService.getForet(id);
    }

    @GetMapping("/all")
    public List<ForetModel> getForets(){
        return foretService.getForets();
    }

    @GetMapping("/myForets")
    public List<ForetModel> getMyForets(@RequestParam Long member_id){
        return foretService.getMyForets(member_id);
    }
}
