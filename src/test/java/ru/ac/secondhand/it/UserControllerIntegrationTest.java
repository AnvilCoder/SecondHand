package ru.ac.secondhand.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.secutity.WithMockCustomUser;
import ru.ac.secondhand.utils.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = TestUtils.getUserEntity();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Test
    @WithMockCustomUser
    void getUserData_Ok() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("username@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("first"))
                .andExpect(jsonPath("$.lastName").value("last"))
                .andExpect(jsonPath("$.phone").value("79998886655"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(username = "unknownUserName", roles = "USER")
    void getUserData_notFound() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserData_Unauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser
    public void setPassword_Ok() throws Exception {
        NewPassword newPassword = TestUtils.getNewPassword();
        String json = TestUtils.asJsonString(newPassword);

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    public void setPassword_Forbidden() throws Exception {
        NewPassword newPassword = TestUtils.getNewPassword();
        newPassword.setCurrentPassword("someIncorrectPassword");
        String json = TestUtils.asJsonString(newPassword);

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser
    public void updateUser_Ok() throws Exception {
        UpdateUserDTO updateUserDTO = TestUtils.getUpdateUserDTO();
        String json = TestUtils.asJsonString(updateUserDTO);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}