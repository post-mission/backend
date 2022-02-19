package com.postmission.repository;

import com.postmission.model.Follow;
import com.postmission.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollowingUser(User followingUser);
    List<Follow> findByFollowedUser(User followedUser);
    Optional<Follow> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
}
