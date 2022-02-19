package com.postmission.repository;

import com.postmission.model.MusicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicalInfoRepository extends JpaRepository<MusicalInfo,String> {

    @Query(value = "select * from musical_info where name=:name limit 1;", nativeQuery = true)
    MusicalInfo findByName(String name);
}
