package com.postmission.repository;

import com.postmission.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String query, Pageable pageable); // 제목 검색
    @Query(
            value = "SELECT p FROM Post p WHERE p.title LIKE %:query% OR p.description LIKE %:query%"
//            , countQuery = "SELECT COUNT(p.id) FROM Post p WHERE p.title LIKE %:title% OR p.content LIKE %:content%"
    )
    List<Post> findByBothSearch(String query, Pageable pageable); // 제목+내용 검색
    @Query(value = "SELECT p FROM Post p JOIN MusicalInfo m ON p.musicalInfo.id = m.id WHERE m.name like %:query%")
    List<Post> findByHeaderSearch(String query, Pageable pageable); // 말머리 검색
}
