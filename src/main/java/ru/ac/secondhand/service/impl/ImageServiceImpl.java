package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.exception.ImageNotFoundException;
import ru.ac.secondhand.exception.ImageSaveException;
import ru.ac.secondhand.exception.InvalidFileException;
import ru.ac.secondhand.repository.ImageRepository;
import ru.ac.secondhand.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    final String LOG_ATTEMPT_MSG = "Attempting to save an image.";
    final String LOG_INVALID_SIZE_MSG = "Invalid file size. Maximum allowed size is 3MB.";
    final String LOG_INVALID_TYPE_MSG = "Invalid file type. Only JPEG and PNG are allowed.";
    final String LOG_INVALID_NAME_MSG = "Invalid file name. It contains illegal characters.";
    final String LOG_CREATED_DIRECTORY_MSG = "Created directory for file uploads: {}";
    final String LOG_ERROR_CREATING_DIRECTORY_MSG = "Could not create the directory for file uploads.";
    final String LOG_IMAGE_SAVED_MSG = "Image saved with path [{}]";
    final String LOG_ERROR_SAVING_IMAGE_MSG = "Error saving image: {}";

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
        log.info(LOG_ATTEMPT_MSG);

        if (!isValidSize(imageFile)) {
            log.warn(LOG_INVALID_SIZE_MSG);
            throw new InvalidFileException("File size is invalid. Maximum allowed size is 3MB.");
        }

        if (!isValidType(imageFile)) {
            log.warn(LOG_INVALID_TYPE_MSG);
            throw new InvalidFileException("File type is invalid. Only JPEG and PNG are allowed.");
        }

        String originalFilename = Objects.requireNonNull(imageFile.getOriginalFilename());
        if (!isValidName(originalFilename)) {
            log.warn(LOG_INVALID_NAME_MSG);
            throw new InvalidFileException("File name is invalid. It contains illegal characters.");
        }

        try {
            String directoryPath = System.getProperty("user.dir") + "/images/";
            String filename = generateUniqueFilename(originalFilename);
            String filePath = directoryPath + filename;

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();
                if (!isCreated) {
                    log.error(LOG_ERROR_CREATING_DIRECTORY_MSG);
                    throw new IOException(LOG_ERROR_CREATING_DIRECTORY_MSG);
                }
                log.info(LOG_CREATED_DIRECTORY_MSG, directoryPath);
            }

            File file = new File(filePath);
            imageFile.transferTo(file);

            Image image = new Image();
            image.setImagePath(filePath);
            image = imageRepository.save(image);

            log.info(LOG_IMAGE_SAVED_MSG, filePath);
            return image;
        } catch (IOException e) {
            log.error(LOG_ERROR_SAVING_IMAGE_MSG, e.getMessage());
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

    @Override
    public byte[] getImage(Integer imageId) {
        Optional<Image> imageOpt = imageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            String filePath = image.getImagePath();
            Path path = Paths.get(filePath);
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new ImageNotFoundException("Not found images {}");
        }
    }

    /**
     * Генерирует уникальное имя файла на основе исходного имени.
     *
     * @param originalFilename Исходное имя файла
     * @return Уникальное имя файла
     */
    public String generateUniqueFilename(String originalFilename) {
        log.debug("Generated unique filename: {}", originalFilename);
        return UUID.randomUUID() + "_" + originalFilename;
    }

    /**
     * Проверяет, соответствует ли размер файла допустимым ограничениям.
     *
     * @param file Файл для проверки
     * @return true, если размер файла соответствует допустимым ограничениям, иначе false
     */
    public boolean isValidSize(MultipartFile file) {
        boolean isValid = file.getSize() <= MAX_SIZE;
        if (!isValid) {
            log.warn("File size {} is invalid. Maximum allowed size is {} bytes.", file.getSize(), MAX_SIZE);
        } else {
            log.debug("File size {} is within the valid range.", file.getSize());
        }
        return isValid;
    }

    /**
     * Проверяет, соответствует ли тип файла одному из допустимых типов (JPEG или PNG).
     *
     * @param file Файл для проверки
     * @return true, если тип файла соответствует допустимым типам, иначе false
     */
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

    /**
     * Проверяет, не содержит ли имя файла запрещенных символов.
     *
     * @param filename Имя файла для проверки
     * @return true, если имя файла не содержит запрещенных символов, иначе false
     */
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
