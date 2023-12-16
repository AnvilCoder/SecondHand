package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.Ad;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findAdsByUserId(Integer id);
}
