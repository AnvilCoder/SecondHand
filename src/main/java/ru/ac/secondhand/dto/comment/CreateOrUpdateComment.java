package ru.ac.secondhand.dto.comment;


import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Класс для создания или обновления комментария.
 * <p>
 * Класс {@code CreateOrUpdateComment} используется для передачи данных
 * при создании или обновлении комментария. Он включает в себя поле "text",
 * представляющее текст комментария.
 * </p>
 */
@Data
public class CreateOrUpdateComment {

    @Size(min = 8, max = 64)
    private String text;
}
