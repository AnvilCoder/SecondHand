package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
