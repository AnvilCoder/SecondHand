package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Comment;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.exception.CommentNotFoundException;
import ru.ac.secondhand.mapper.CommentMapper;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.repository.CommentRepository;
import ru.ac.secondhand.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper mapper;


    private Ad getAdById(Integer adId) {
        log.info("Fetching Ad with id: {}", adId);
        return adRepository.findById(adId).orElseThrow(() -> {
            log.warn("Ad not found for id: {}", adId);
            return new AdNotFoundException("Ad not found for id: " + adId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getComments(Integer adId) {
        log.info("Fetching comments for adId: {}", adId);
        Ad ad = getAdById(adId);
        List<Comment> comments = commentRepository.findByAdId(adId);
        return mapper.toComments(comments);
    }

    @Override
    public CommentDTO createComment(CreateOrUpdateComment comment, Integer adId) {
        log.info("Attempting to create a new comment for Ad [{}]", adId);
        Ad ad = getAdById(adId);

        Comment newComment = new Comment();
        newComment.setText(comment.getText());
        newComment.setAd(ad);
        newComment.setUser(ad.getUser());
        newComment.setCreatedAt(LocalDateTime.now());

        Comment saveComment = commentRepository.save(newComment);
        log.info("Comment [{}] successfully created for Ad [{}]", saveComment.getId(), adId);
        return mapper.toCommentDTO(saveComment);
    }

    @Override
    public void delete(Integer adId, Integer commentId) {
        log.info("Trying to delete a comment for id: {}", adId);
        getAdById(adId);
        Comment deleteComment = commentRepository.findByAdIdAndId(adId, commentId)
                .orElseThrow(() -> {
                    log.warn("Comment [{}] not found for update", commentId);
                    return new CommentNotFoundException(String.format("Comment [%d] not found for ad [%d]", commentId, adId));
                });
        commentRepository.delete(deleteComment);
        log.info("Comments with id: {} successfully deleted for ad with id: {}", commentId, adId);
    }

    @Override
    public CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentRequest) {
        log.info("Starting update of comment [{}] for ad [{}]", commentId, adId);
        Ad ad = getAdById(adId);
        Comment updateComment = commentRepository.findById(commentId)
                .orElseThrow(()-> {
                    log.warn("Comment [{}] not found for update", commentId);
                    return new CommentNotFoundException(String.format("Comment [%d] not found ", commentId));
                });
        updateComment.setText(commentRequest.getText());
        updateComment.setAd(ad);
        updateComment.setUser(ad.getUser());
        updateComment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(updateComment);
        log.info("Comment [{}] for ad [{}] successfully updated", commentId, adId);

        return mapper.toCommentDTO(updateComment);
    }
}
