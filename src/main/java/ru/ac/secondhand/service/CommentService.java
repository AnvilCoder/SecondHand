package ru.ac.secondhand.service;

import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;

/**
 * Интерфейс CommentService определяет контракт для сервиса управления комментариями.
 */
public interface CommentService {

    /**
     * Получает все комментарии к конкретному объявлению.
     * @param adId Идентификатор объявления, для которого нужно получить комментарии.
     * @return Объект Comments, содержащий список комментариев и их количество.
     */
    Comments getComments(Integer adId);

    /**
     * Создает новый комментарий к объявлению.
     * @param comment Данные для создания нового комментария.
     * @param adId Идентификатор объявления, к которому будет добавлен комментарий.
     * @return Созданный комментарий в виде DTO.
     */
    CommentDTO createComment(CreateOrUpdateComment comment, Integer adId);

    /**
     * Удаляет комментарий из объявления.
     * @param adId Идентификатор объявления, из которого будет удален комментарий.
     * @param commentId Идентификатор удаляемого комментария.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     */
    void delete(Integer adId, Integer commentId);

    /**
     * Обновляет существующий комментарий к объявлению.
     * @param adId Идентификатор объявления, к которому принадлежит комментарий.
     * @param commentId Идентификатор комментария, который нужно обновить.
     * @param commentRequest Обновленные данные комментария.
     * @return Обновленный комментарий в виде DTO.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     */
    CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentRequest);
}

