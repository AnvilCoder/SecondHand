package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.repository.CommentRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.CommentService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdService adService;



    @Override
    public Comments getComments(Integer adId) {
        ExtendedAd ad = adService.getAdInfo(adId);
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
