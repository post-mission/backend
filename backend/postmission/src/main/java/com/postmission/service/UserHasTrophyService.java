package com.postmission.service;

public interface UserHasTrophyService {

    public void checkFollowTrophy(Long userId, Long targetId) throws Exception;
    public void checkNumOfTicketTrophy(Long userId) throws Exception;
    public void check5RankTicketTrophy(Long userId) throws Exception;
}
