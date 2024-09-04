package com.example.commentservice.repository;


import com.example.commentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value ="SELECT * from comment c where c.post_id =:id ", nativeQuery=true)
    List<Comment> findByPostId(@Param("id") int id);

}
