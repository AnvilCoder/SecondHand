package ru.ac.secondhand.service;

import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;

public interface CommentService {


    Comments getComments(Integer adId);

    CommentDTO createComment(CreateOrUpdateComment comment, Integer adId);

    void delete(Integer adId, Integer commentId);

    CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentRequest);
}
