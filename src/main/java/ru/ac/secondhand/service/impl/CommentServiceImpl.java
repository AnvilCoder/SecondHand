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
import ru.ac.secondhand.exception.CommentNotFoundException;
import ru.ac.secondhand.mapper.CommentMapper;
import ru.ac.secondhand.repository.CommentRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.CommentService;
import ru.ac.secondhand.utils.MethodLog;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdService adService;
    private final CommentMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Comments getComments(Integer adId) {
        log.info("Fetching comments for adId: {}", adId);
        Ad ad = adService.getAdById(adId);
        List<Comment> comments = commentRepository.findByAdId(adId);
        return mapper.toComments(comments);
    }

    @Override
    public CommentDTO createComment(CreateOrUpdateComment comment, Integer adId) {
        log.info("Attempting to create a new comment for Ad [{}]", adId);
        Ad ad = adService.getAdById(adId);

        Comment newComment = mapper.toComment(comment);
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
        adService.getAdById(adId);
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
        Ad ad = adService.getAdById(adId);
        Comment updateComment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Comment [{}] not found for update", commentId);
                    return new CommentNotFoundException(String.format("Comment [%d] not found ", commentId));
                });
        updateComment.setText(commentRequest.getText());
        commentRepository.save(updateComment);
        log.info("Comment [{}] for ad [{}] successfully updated", commentId, adId);

        return mapper.toCommentDTO(updateComment);
    }

    /**
     * Проверка, что юзер является владельцем комментария,
     * для предоставления ему право на удаление/обновление
     *
     * @param username - имя пользователя
     * @param id - id комментария
     */
    public boolean isOwner(String username, Integer id) {
        log.info("Method {}, user {}, comment {}", MethodLog.getMethodName(), username, id);

        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> {
                 log.warn("Comment [{}] not found", id);
                return new CommentNotFoundException(String.format("Comment [%d] not found ", id));
            });
        if (!comment.getUser().getUsername().equals(username)) {
            log.warn("Trying to access foreign comment {} by user {}", id, username);
            return false;
        }
        return true;
    }
}
