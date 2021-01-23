package com.project.foret.repository;

import com.project.foret.entity.Foret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForetRepository extends JpaRepository<Foret, Long> {
    List<Foret> findByMembersId(Long id);
}
