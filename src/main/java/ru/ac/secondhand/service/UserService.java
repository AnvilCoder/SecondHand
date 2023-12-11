package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.User;
import ru.ac.secondhand.dto.user.UpdateUser;

public interface UserService {

    void setPassword(NewPassword newPassword);

    User getUserData();

    UpdateUser updateUser(UpdateUser updateUser);

    void updateAvatar(MultipartFile file);
}
