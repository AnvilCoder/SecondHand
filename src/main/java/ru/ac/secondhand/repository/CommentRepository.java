package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByAdId(Integer adId);

    Optional<Comment> findByAdIdAndId(Integer adId, Integer commentId);


}
