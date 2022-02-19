package com.postmission.repository;

import com.postmission.model.Post;
import com.postmission.model.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentsRepository extends JpaRepository<PostComments,Long> {
    List<PostComments> findByPost(Post post);
}
