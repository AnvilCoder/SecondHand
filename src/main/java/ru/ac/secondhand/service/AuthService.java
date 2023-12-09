package ru.ac.secondhand.service;

import ru.ac.secondhand.dto.user.Register;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);
}
