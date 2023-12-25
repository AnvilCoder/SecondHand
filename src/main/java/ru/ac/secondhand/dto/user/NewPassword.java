package ru.ac.secondhand.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * Класс для представления нового пароля пользователя.
 * <p>
 * Класс {@code NewPassword} используется для передачи данных о смене пароля пользователя,
 * включая текущий пароль (currentPassword) и новый пароль (newPassword).
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPassword {

    @Size(min = 8, max = 16)
    private String currentPassword;

    @Size(min = 8, max = 16)
    private String newPassword;
}