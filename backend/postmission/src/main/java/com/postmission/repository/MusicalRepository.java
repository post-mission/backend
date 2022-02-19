package com.postmission.repository;

import com.postmission.model.MusicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MusicalRepository extends JpaRepository<MusicalInfo, String> {

    List<MusicalInfo> findByNameContaining(String name);

}
