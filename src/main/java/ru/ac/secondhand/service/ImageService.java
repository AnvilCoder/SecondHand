package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;

import java.util.Optional;

public interface ImageService {
    Image saveImage(MultipartFile imageFile);

    void deleteImage(Integer imageId);

    Optional<Image> getImage(Integer imagerId);
}
