package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;

public interface UserService {

    void setPassword(NewPassword newPassword);

    UserDTO getUserData();

    UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO);

    void updateAvatar(MultipartFile file);
}
