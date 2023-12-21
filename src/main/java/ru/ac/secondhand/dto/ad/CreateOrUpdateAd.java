package ru.ac.secondhand.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * Класс Data Transfer Object (DTO) для создания или обновления объявления.
 * <p>
 * Класс {@code CreateOrUpdateAd} используется для передачи данных при создании или обновлении объявлений между слоями приложения.
 * </p>
 * <p>
 * Этот класс включает в себя такие основные атрибуты объявления, как заголовок, цена и описание.
 * Он предназначен для упрощения процессов создания и изменения объявлений, обеспечивая при этом достаточный уровень детализации и валидации входных данных.
 * </p>
 *
 * @author fifimova
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateAd {

    @Size(min = 4, max = 32)
    private String title;

    @PositiveOrZero
    private Integer price;

    @Size(min = 8, max = 64)
    private String description;
}
