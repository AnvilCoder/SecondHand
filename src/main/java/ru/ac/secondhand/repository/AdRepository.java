package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.Ad;

public interface AdRepository extends JpaRepository<Ad, Integer> {
}
