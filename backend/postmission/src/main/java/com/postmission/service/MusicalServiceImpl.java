package com.postmission.service;

import com.postmission.model.Musical;
import com.postmission.model.MusicalInfo;
import com.postmission.repository.MusicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicalServiceImpl implements MusicalService{

    @Autowired
    MusicalRepository repo;

    @Override
    public void saveAll(List<MusicalInfo> list) throws Exception {
        repo.saveAll(list);

    }

    @Override
    public List<MusicalInfo> findByNameContaining(String name) throws Exception {
        return repo.findByNameContaining(name);
    }

    @Override
    public MusicalInfo findById(String musicalInfoId) throws Exception {
        Optional<MusicalInfo> option = repo.findById(musicalInfoId);
        MusicalInfo musicalInfo = option.get();
        return musicalInfo;
    }
}
