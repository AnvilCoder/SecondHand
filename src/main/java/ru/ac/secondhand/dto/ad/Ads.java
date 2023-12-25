package ru.ac.secondhand.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс-контейнер для группы DTO объявлений.
 * <p>
 * Класс {@code Ads} предназначен для удобной упаковки и передачи нескольких DTO объявлений,
 * {@code AdDTO}, а также для предоставления дополнительной информации, такой как общее количество
 * объявлений в этом наборе.
 * </p>
 * <p>
 * Этот класс полезен для сценариев, где требуется передать список объявлений, например, при пагинации
 * или в ответах API, где необходимо вместе со списком предоставить дополнительные метаданные.
 * </p>
 *
 * @author fifimova
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ads {

    private Integer count;

    private List<AdDTO> results;
}
