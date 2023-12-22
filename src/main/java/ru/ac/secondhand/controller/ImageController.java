package ru.ac.secondhand.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.service.ImageService;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController { //TODO добавил для теста, нужно будет удалить потом.

    private final ImageService imageService;

    @PostMapping("/add")
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image) {
        Image imageUrl = imageService.saveImage(image);
        if (imageUrl != null) {
            return ResponseEntity.ok("Image added successfully: " + imageUrl);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add image");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageById(@PathVariable Integer imageId) {
        byte[] image = imageService.getImage(imageId);
        if (image != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
