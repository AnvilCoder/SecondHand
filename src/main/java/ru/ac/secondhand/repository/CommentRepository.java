package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
