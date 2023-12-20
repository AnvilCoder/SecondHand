package ru.ac.secondhand.dto.ad;

import lombok.Data;

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
 *
 * @author fifimova
 */
@Data
public class AdDTO {

    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

}
