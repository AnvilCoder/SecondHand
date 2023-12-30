package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.entity.User;

/**
 * Сервис для операций, связанных с пользователем.
 *
 * <p>Этот сервис обрабатывает пользователя, управление паролями и извлечение и обновление данных пользователя.</p>
 *
 */
public interface UserService {

    /**
     * Находит пользователя.
     *
     * @return User пользователь.
     * @throws UserNotFoundException если пользователь не найден.
     */
    User findUser();

    /**
     * Устанавливает новый пароль для текущего пользователя.
     *
     * @param newPassword Новый пароль для установки.
     * @throws IncorrectPasswordException если текущий пароль не совпадает.
     */
    void setPassword(NewPassword newPassword);

    /**
     * Извлекает данные текущего пользователя в виде объекта передачи данных (DTO).
     *
     * @return UserDTO DTO, содержащий данные пользователя.
     */
    UserDTO getUserData();

    /**
     * Обновляет данные текущего пользователя на основе предоставленного DTO.
     *
     * @param updateUserDTO DTO с обновленными данными пользователя.
     * @return UpdateUserDTO Обновленный DTO.
     */
    UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO);

    /**
     * Обновляет аватар для текущего пользователя.
     *
     * @param image Новое изображение для установки в качестве аватара.
     */
    void updateAvatar(MultipartFile file);
}
