package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.exception.ImageSaveException;
import ru.ac.secondhand.exception.InvalidFileException;
import ru.ac.secondhand.repository.ImageRepository;
import ru.ac.secondhand.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private static final long MAX_SIZE = 3 * 1024 * 1024;

    /**
     * Сохранение нового изображения
     *
     * @param imageFile
     * @return Image
     */
    @Override
    @Transactional
    public Image saveImage(MultipartFile imageFile) {
        log.info("Attempting to save an image.");
        if (!isValidSize(imageFile)) {
            log.warn("Invalid file size. Maximum allowed size is 3MB.");
            throw new InvalidFileException("File size is invalid. Maximum allowed size is 3MB.");
        }
        if (!isValidType(imageFile)) {
            log.warn("Invalid file type. Only JPEG and PNG are allowed.");
            throw new InvalidFileException("File type is invalid. Only JPEG and PNG are allowed.");
        }
        if (!isValidName(Objects.requireNonNull(imageFile.getOriginalFilename()))) {
            log.warn("Invalid file name. It contains illegal characters.");
            throw new InvalidFileException("File name is invalid. It contains illegal characters.");
        }

        try {
            String directoryPath = System.getProperty("user.dir") + "/images/";
            String filename = generateUniqueFilename(imageFile.getOriginalFilename());
            String filePath = directoryPath + filename;

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();
                if (!isCreated) {
                    log.error("Could not create the directory for file uploads.");
                    throw new IOException("Could not create the directory for file uploads.");
                }
                log.info("Created directory for file uploads: {}", directoryPath);
            }

            File file = new File(filePath);
            imageFile.transferTo(file);

            Image image = new Image();
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
     *
     * @param imageId
     */
    @Override
    public void deleteImage(Integer imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
        }
        log.info("Image deleted [{}]", imageId);
    }

    public String generateUniqueFilename(String originalFilename) {
        log.debug("Generated unique filename: {}", originalFilename);
        return UUID.randomUUID() + "_" + originalFilename;
    }

    public boolean isValidSize(MultipartFile file) {
        boolean isValid = file.getSize() <= MAX_SIZE;
        if (!isValid) {
            log.warn("File size {} is invalid. Maximum allowed size is {} bytes.", file.getSize(), MAX_SIZE);
        } else {
            log.debug("File size {} is within the valid range.", file.getSize());
        }
        return isValid;
    }

    public boolean isValidType(MultipartFile file) {
        String contentType = file.getContentType();
        boolean isValid = contentType.equals("image/jpeg") || contentType.equals("image/png");
        if (!isValid) {
            log.warn("File type '{}' is invalid. Only JPEG and PNG are allowed.", contentType);
        } else {
            log.debug("File type '{}' is valid.", contentType);
        }
        return isValid;
    }

    public boolean isValidName(String filename) {
        boolean isValid = !filename.contains("..") && !filename.matches(".*[<>\"].*");
        if (!isValid) {
            log.warn("Filename '{}' is invalid. It contains illegal characters.", filename);
        } else {
            log.debug("Filename '{}' is valid.", filename);
        }
        return isValid;
    }

}
