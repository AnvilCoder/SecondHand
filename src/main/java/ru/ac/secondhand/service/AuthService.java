package ru.ac.secondhand.service;

import ru.ac.secondhand.dto.user.RegisterDTO;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterDTO registerDTO);
}
