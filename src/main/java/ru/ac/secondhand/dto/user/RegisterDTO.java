package ru.ac.secondhand.dto.user;

import lombok.Data;
import ru.ac.secondhand.entity.enums.Role;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Принимаемые данные для регистрации нового пользователя
 */
@Data
public class RegisterDTO {

    @Size(min = 4, max = 32)
    private String username;

    @Size(min = 8, max = 16)
    private String password;

    @Size(min = 2, max = 16)
    private String firstName;

    @Size(min = 2, max = 16)
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    private Role role;
}
