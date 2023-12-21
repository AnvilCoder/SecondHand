package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.exception.ImageSaveException;
import ru.ac.secondhand.repository.ImageRepository;
import ru.ac.secondhand.service.ImageService;

import java.io.File;
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
            String directoryPath = System.getProperty("user.dir") + "/images/";;
            String filename = imageFile.getOriginalFilename();
            String filePath = directoryPath + filename;

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(filePath);
            imageFile.transferTo(file);

            image.setImagePath(filePath);
            image = imageRepository.save(image);
            log.info("Image saved with path [{}]", filePath);
            return image;
        } catch (IOException e) {
            log.error("Error saving image: {}", e.getMessage());
            throw new ImageSaveException("Failed to save image", e);
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
