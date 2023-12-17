package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;

public interface ImageService {
    Image saveImage(MultipartFile imageFile);

    void deleteImage(Integer imageId);
}
