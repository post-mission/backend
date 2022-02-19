package com.postmission.service;


import com.postmission.model.Musical;
import com.postmission.model.MusicalInfo;

import java.util.List;

public interface MusicalService {
    public void saveAll(List<MusicalInfo> list) throws Exception;
    List<MusicalInfo> findByNameContaining(String name) throws Exception;
    MusicalInfo findById(String musicalInfoId) throws Exception;
}
