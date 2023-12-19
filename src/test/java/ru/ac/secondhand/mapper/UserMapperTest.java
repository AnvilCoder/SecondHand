package ru.ac.secondhand.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.utils.TestUtils;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void shouldMapRegisterDTOtoUserEntity() {
        RegisterDTO registerDTO = TestUtils.getRegisterDTO();
        User user = mapper.registerDTOToUser(registerDTO);

        //смапленные поля
        Assertions.assertThat(user.getUsername()).isEqualTo(registerDTO.getUsername());
        Assertions.assertThat(user.getPassword()).isEqualTo(registerDTO.getPassword());
        Assertions.assertThat(user.getFirstName()).isEqualTo(registerDTO.getFirstName());
        Assertions.assertThat(user.getLastName()).isEqualTo(registerDTO.getLastName());
        Assertions.assertThat(user.getPhone()).isEqualTo(registerDTO.getPhone());
        Assertions.assertThat(user.getRole()).isEqualTo(registerDTO.getRole());

        //пустые поля
        Assertions.assertThat(user.getId()).isNull();
        Assertions.assertThat(user.getImage()).isNull();
        Assertions.assertThat(user.getAds()).isNull();
        Assertions.assertThat(user.getComments()).isNull();
    }

    @Test
    void shouldMapUserToUserDTO() {
        User user = TestUtils.getUserEntity();
        UserDTO userDTO = mapper.toUserDTO(user);

        Assertions.assertThat(userDTO.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userDTO.getImage()).isEqualTo(String.format("/users/image/%d", user.getImage().getId()));
        Assertions.assertThat(userDTO.getEmail()).isEqualTo(user.getUsername());
        Assertions.assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        Assertions.assertThat(userDTO.getPhone()).isEqualTo(user.getPhone());
        Assertions.assertThat(userDTO.getRole()).isEqualTo(user.getRole().toString());
    }

    @Test
    void shouldUpdateUserDTOToUser() {
        User userToUpdate = TestUtils.getUserEntity();
        User userInitialFields = TestUtils.getUserEntity();
        UpdateUserDTO updateUserDTO = TestUtils.getUpdateUserDTO();
        mapper.updateUserDTOToUser(updateUserDTO, userToUpdate);


        //обновленные поля
        Assertions.assertThat(userToUpdate.getFirstName()).isEqualTo(updateUserDTO.getFirstName());
        Assertions.assertThat(userToUpdate.getLastName()).isEqualTo(updateUserDTO.getLastName());
        Assertions.assertThat(userToUpdate.getPhone()).isEqualTo(updateUserDTO.getPhone());

        //поля со старыми значениями
        Assertions.assertThat(userToUpdate.getId()).isEqualTo(userInitialFields.getId());
        Assertions.assertThat(userToUpdate.getImage()).isEqualTo(userInitialFields.getImage());
        Assertions.assertThat(userToUpdate.getUsername()).isEqualTo(userInitialFields.getUsername());
        Assertions.assertThat(userToUpdate.getPassword()).isEqualTo(userInitialFields.getPassword());
        Assertions.assertThat(userToUpdate.getRole()).isEqualTo(userInitialFields.getRole());
        Assertions.assertThat(userToUpdate.getAds()).isEqualTo(userInitialFields.getAds());
        Assertions.assertThat(userToUpdate.getComments()).isEqualTo(userInitialFields.getComments());
    }
}