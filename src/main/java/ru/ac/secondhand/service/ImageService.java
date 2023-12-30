package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;

import java.util.Optional;
/**
 * Сервис для работы с изображениями, включая сохранение, удаление и получение изображений,
 * а так же валидацию название/размера/типа изображения.
 */
public interface ImageService {

    /**
     * Сохраняет изображение в файловой системе и информацию о нем в базе данных.
     *
     * @param imageFile Мультипарт-файл с изображением для сохранения
     * @return Объект Image с информацией о сохраненном изображении
     * @throws InvalidFileException Если файл не соответствует требованиям по размеру, типу или имени
     * @throws ImageSaveException  Если произошла ошибка при сохранении изображения
     */
    Image saveImage(MultipartFile imageFile);

    /**
     * Удаляет изображение по его идентификатору, если оно существует в базе данных.
     *
     * @param imageId Идентификатор изображения, которое нужно удалить
     */
    void deleteImage(Integer imageId);

    /**
     * Получает байтовое представление изображения по его идентификатору.
     *
     * @param imageId Идентификатор изображения, которое нужно получить
     * @return Байтовое представление изображения
     * @throws ImageNotFoundException Если изображение с заданным идентификатором не найдено
     */
    byte[] getImage(Integer imagerId);
}
