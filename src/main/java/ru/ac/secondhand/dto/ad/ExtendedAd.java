package ru.ac.secondhand.dto.ad;

import lombok.Data;

/**
 * Класс, представляющий расширенные данные объявления.
 * <p>
 * Этот класс {@code ExtendedAd} используется для хранения детальной информации об объявлениях.
 * Он включает в себя всю необходимую информацию, такую как данные автора, описание, контактную информацию,
 * изображение, цену и заголовок объявления.
 * Предназначен для использования в сценариях, где требуется полная информация об объявлении.
 * </p>
 *
 * @author fifimova
 */
@Data
public class ExtendedAd {

    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;
}
