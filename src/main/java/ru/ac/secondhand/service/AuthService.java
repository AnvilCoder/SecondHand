package ru.ac.secondhand.service;

import ru.ac.secondhand.dto.user.RegisterDTO;
/**
 * Сервис для аутентификации и регистрации пользователей.
 *
 * Этот сервис содержит два основных метода: login и register.
 */
public interface AuthService {

    /**
     * Попытка аутентификации пользователя по имени пользователя и паролю.
     *
     * @param userName Имя пользователя для аутентификации
     * @param password Пароль пользователя
     * @return true, если аутентификация прошла успешно, иначе false
     * @throws IncorrectPasswordException Если введен неверный пароль
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя на основе данных из объекта RegisterDTO.
     *
     * @param registerDTO Объект RegisterDTO с данными нового пользователя
     * @return true, если регистрация прошла успешно, иначе false
     * @throws UserAlreadyExistException Если пользователь с таким именем уже существует
     */
    boolean register(RegisterDTO registerDTO);
}
