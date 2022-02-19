package com.postmission.service;

import com.postmission.model.UserHasTrophy;
import com.postmission.repository.UserHasTrophyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHasTrophyServiceImpl implements UserHasTrophyService {

    @Autowired
    UserService userService;
    @Autowired
    TicketService ticketService;
    @Autowired
    TrophyService trophyService;
    @Autowired
    UserHasTrophyRepository userHasTrophyRepository;

    @Override
    public void checkFollowTrophy(Long userId, Long targetId) throws Exception {
        // 첫 팔로우 트로피 체크
        if(userHasTrophyRepository.findByUserIdAndTrophyId(userId,1L)==null){ // 본인이 1번 트로피(첫 팔로우) 미보유시
            System.out.println("1번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(1L))
                    .build());
        }
        // 첫 팔로워 트로피 체크
        if(userHasTrophyRepository.findByUserIdAndTrophyId(targetId,2L)==null){ // 상대방이 2번 트로피(첫 팔로워) 미보유시
            System.out.println("2번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(targetId))
                    .trophy(trophyService.getTrophy(2L))
                    .build());
        }
    }

    @Override
    public void checkNumOfTicketTrophy(Long userId) throws Exception {
        // 후기 트로피 체크
        if(userHasTrophyRepository.findByUserIdAndTrophyId(userId,3L)==null){ // 본인이 3번 트로피(첫 후기) 미보유시
            System.out.println("3번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(3L))
                    .build());
        }else if(ticketService.getAllMyTicketsWatched(userId).size()>=10 && userHasTrophyRepository.findByUserIdAndTrophyId(userId,4L)==null){ // 본인이 후기10개 이상 작성 및 4번 트로피(후기 10개) 미보유시
            System.out.println("4번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(4L))
                    .build());
        }else if(ticketService.getAllMyTicketsWatched(userId).size()>=50 && userHasTrophyRepository.findByUserIdAndTrophyId(userId,5L)==null){ // 본인이 후기50개 이상 작성 및 5번 트로피(후기 50개) 미보유시
            System.out.println("5번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(5L))
                    .build());
        }
        // 한 작품 N회 이상 트로피 체크
        if(ticketService.getMostTicketCount(userId)>=2 && userHasTrophyRepository.findByUserIdAndTrophyId(userId,6L)==null){ // 본인이 6번 트로피(한 작품 2회 이상) 미보유시
            System.out.println("6번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(6L))
                    .build());
        }else if(ticketService.getMostTicketCount(userId)>=5 && userHasTrophyRepository.findByUserIdAndTrophyId(userId,7L)==null){ // 본인이 7번 트로피(한 작품 5회 이상) 미보유시
            System.out.println("7번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(7L))
                    .build());
        }
    }

    @Override
    public void check5RankTicketTrophy(Long userId) throws Exception {
        // 별점 5개 트로피 체크
        if(userHasTrophyRepository.findByUserIdAndTrophyId(userId,8L)==null){ // 본인이 별점 5개를 주고 8번 트로피(별점 5개 첫 등록) 미보유시
            System.out.println("8번 트로피 획득");
            userHasTrophyRepository.save(UserHasTrophy.builder()
                    .user(userService.findUserById(userId))
                    .trophy(trophyService.getTrophy(8L))
                    .build());
        }
    }
}
