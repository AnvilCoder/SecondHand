package ru.ac.secondhand.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUser;
import ru.ac.secondhand.dto.user.User;
import ru.ac.secondhand.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void setPassword(NewPassword newPassword) {
        return;
    }

    @Override
    public User getUserData() {
        return new User();
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        return new UpdateUser();
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        return;
    }
}
