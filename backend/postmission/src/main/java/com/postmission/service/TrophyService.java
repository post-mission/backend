package com.postmission.service;

import com.postmission.model.Trophy;
import com.postmission.model.UserHasTrophy;
import com.postmission.repository.TrophyRepository;
import com.postmission.repository.UserHasTrophyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrophyService {
    private final UserHasTrophyRepository userHasTrophyRepository;
    private final TrophyRepository trophyRepository;

    public List<Trophy> getSelectedTrophies(Long userId){
        List<UserHasTrophy> userHasTrophies = userHasTrophyRepository.findByUserId(userId);
        List<Trophy> trophies = new ArrayList<>();
        userHasTrophies.forEach((userHasTrophy -> {
            if(userHasTrophy.isSelected()){
                trophies.add(userHasTrophy.getTrophy());
            }
        }));
        return trophies;
    }

    public List<Trophy> getAllTrophies(Long userId) {
        List<UserHasTrophy> userHasTrophies = userHasTrophyRepository.findByUserId(userId);
        List<Trophy> trophies = new ArrayList<>();
        userHasTrophies.forEach((userHasTrophy -> {
            trophies.add(userHasTrophy.getTrophy());
        }));
        return trophies;
    }

    public Trophy getTrophy(Long trophyId){
        return trophyRepository.findById(trophyId).get();
    }
}
