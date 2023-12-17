package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.mapper.UserMapper;
import ru.ac.secondhand.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void setPassword(NewPassword newPassword) {
        return;
    }

    @Override
    public UserDTO getUserData() {
        return new UserDTO();
    }

    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO) {
        return new UpdateUserDTO();
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        return;
    }
}
