package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.repository.ImageRepository;
import ru.ac.secondhand.service.ImageService;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    /**
     * Сохранение нового изображения
     * @param imageFile
     * @return Image
     */
    @Override
    @Transactional
    public Image saveImage(MultipartFile imageFile) {
        Image image = new Image();
        try {
            image.setImage(imageFile.getBytes());
            image = imageRepository.save(image);
            log.info("Image saved [{}]", image.getId());
            return image;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаление изображения
     * @param imageId
     */
    @Override
    public void deleteImage(Integer imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
        }
        log.info("Image deleted [{}]", imageId);
    }
}
