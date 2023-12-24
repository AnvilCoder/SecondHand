package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.IncorrectPasswordException;
import ru.ac.secondhand.exception.UserNotFoundException;
import ru.ac.secondhand.mapper.UserMapper;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.service.UserService;
import ru.ac.secondhand.utils.MethodLog;

@Service
@Slf4j
@RequiredArgsConstructor
//@NoArgsConstructor(force = true) Димка чини
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder encoder;

    @Override
    public User findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found [%s]", username)));
        return user;
    }

    /**
     * Изменение пароля пользователя
     *
     * @param newPassword
     */
    @Override
    public void setPassword(NewPassword newPassword) {
        log.debug("Method {}", MethodLog.getMethodName());

        User user = findUser();

        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            log.warn("Incorrect old password, user {}", user.getId());
            throw new IncorrectPasswordException(String.format("Incorrect old password, user %d", user.getId()));
        }

        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        log.info("Password updated for user {}", user.getId());
    }

    /**
     * Получение данных пользователя
     *
     * @return UserDTO
     */
    @Override
    public UserDTO getUserData() {
        log.debug("Method {}", MethodLog.getMethodName());

        User user = findUser();
        return userMapper.toUserDTO(user);
    }

    /**
     * Обновление данных пользователя - имени, отчества и номера телефона
     *
     * @param updateUserDTO
     * @return UpdateUserDTO
     */
    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO) {
        log.info("Method {}", MethodLog.getMethodName());

        User user = findUser();
        userMapper.updateUserDTOToUser(updateUserDTO, user);
        userRepository.save(user);

        return updateUserDTO;
    }

    /**
     * Обновление аватара пользователя
     *
     * @param image
     */
    @Override
    public void updateAvatar(MultipartFile image) {
        log.info("Method {}", MethodLog.getMethodName());

        User user = findUser();

        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage().getId());
        }

        Image newImage = imageService.saveImage(image);
        user.setImage(newImage);
        userRepository.save(user);
    }
}
