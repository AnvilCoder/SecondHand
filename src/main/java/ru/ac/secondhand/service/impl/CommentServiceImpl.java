package ru.ac.secondhand.service.impl;

import org.springframework.stereotype.Service;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public Comments getComments(Integer adId) {
        return null;
    }

    @Override
    public CommentDTO createComment(CreateOrUpdateComment comment) {
        return null;
    }

    @Override
    public void delete(Integer adId, Integer commentId) {

    }

    @Override
    public CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentRequest) {
        return null;
    }
}
