package ru.ac.secondhand.dto.comment;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object (DTO) для представления объявления.
 * <p>
 * Класс {@code AdDTO} используется для передачи данных объявлений между слоями приложения,
 * сокращая количество передаваемой информации и скрывая детали реализации сущности {@code Ad}.
 * </p>
 * <p>
 * Этот класс включает основные данные объявления, такие как идентификатор пользователя-автора,
 * URL изображения, уникальный идентификатор объявления (pk), цену и заголовок.
 * Он предназначен для упрощения обработки и представления информации о объявлениях в пользовательском интерфейсе.
 * </p>
 */
@Setter
@Getter
@NoArgsConstructor
public class Comments {

    private Integer count;
    private List<CommentDTO> results;
}
