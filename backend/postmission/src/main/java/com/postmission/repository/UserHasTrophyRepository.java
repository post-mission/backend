package com.postmission.repository;

import com.postmission.model.UserHasTrophy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHasTrophyRepository extends JpaRepository<UserHasTrophy,Long> {
    List<UserHasTrophy> findByUserId(Long userId);
    UserHasTrophy findByUserIdAndTrophyId(Long userId, Long TrophyId);

}
