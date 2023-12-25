package ru.ac.secondhand.dto.user;

import lombok.Data;
import javax.validation.constraints.Size;

/**
 * Класс для представления данных входа пользователя.
 * <p>
 * Класс {@code Login} используется для передачи данных входа пользователя, включая
 * логин (username) и пароль (password). Эти данные используются для аутентификации
 * пользователя в системе.
 * </p>
 */
@Data
public class Login {

    @Size(min = 4, max = 32)
    private String username;

    @Size(min = 8, max = 16)
    private String password;

}
