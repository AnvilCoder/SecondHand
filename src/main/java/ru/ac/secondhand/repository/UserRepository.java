package ru.ac.secondhand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ac.secondhand.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
