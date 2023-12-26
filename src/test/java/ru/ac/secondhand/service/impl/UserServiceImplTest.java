package ru.ac.secondhand.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.IncorrectPasswordException;
import ru.ac.secondhand.mapper.UserMapper;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.exception.UserNotFoundException;
import ru.ac.secondhand.utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private PasswordEncoder encoder;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.spy(userService);

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("username");
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void findUser_UserExists_ReturnsUser() {
        User expectedUser = TestUtils.getUserEntity();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(expectedUser));

        User result = userService.findUser();

        assertEquals(expectedUser, result);
    }

    @Test
    public void findUser_UserDoesNotExist_ThrowsException() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUser());
    }

    @Test
    public void setPassword_CorrectOldPassword_UpdatesPassword() {
        User mockUser = TestUtils.getUserEntity();
        Mockito.doReturn(mockUser).when(userService).findUser();

        NewPassword newPassword = new NewPassword("password", "newPassword");

        when(encoder.matches(newPassword.getCurrentPassword(), mockUser.getPassword())).thenReturn(true);
        when(encoder.encode(newPassword.getNewPassword())).thenReturn("encodedPassword");

        userService.setPassword(newPassword);

        verify(userService).findUser();
        verify(userRepository).save(any(User.class));
        assertEquals("encodedPassword", mockUser.getPassword());
    }

    @Test
    public void setPassword_IncorrectOldPassword_ThrowsException() {
        User mockUser = TestUtils.getUserEntity();
        Mockito.doReturn(mockUser).when(userService).findUser();

        NewPassword newPassword = new NewPassword("password", "newPassword");

        when(encoder.matches(newPassword.getCurrentPassword(), mockUser.getPassword())).thenReturn(false);

        assertThrows(IncorrectPasswordException.class,
                () -> userService.setPassword(newPassword));
    }

    @Test
    public void getUserDataTest() {
        User mockUser = new User();
        UserDTO mockUserDTO = new UserDTO();

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO result = userService.getUserData();

        assertNotNull(result);
        assertEquals(mockUserDTO, result);
    }

    @Test
    void updateUser_UpdatesAndReturnsUserDTO() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(); // Настройте DTO
        User mockUser = TestUtils.getUserEntity();

        // Мокирование метода findUser
        Mockito.doReturn(mockUser).when(userService).findUser();

        // Вызов метода
        UpdateUserDTO result = userService.updateUser(updateUserDTO);

        // Проверки
        verify(userService).findUser();
        verify(userMapper).updateUserDTOToUser(updateUserDTO, mockUser);
        verify(userRepository).save(mockUser);
        assertEquals(updateUserDTO, result);
    }

    @Test
    public void updateAvatarTest() {
        MultipartFile image = mock(MultipartFile.class);
        User mockUser = new User();
        mockUser.setId(1);
        Image oldImage = new Image();
        oldImage.setId(1);
        mockUser.setImage(oldImage);
        Image newImage = new Image();
        newImage.setId(2);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(imageService.saveImage(any(MultipartFile.class))).thenReturn(newImage);

        userService.updateAvatar(image);

        verify(imageService).deleteImage(oldImage.getId());

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(newImage.getId(), savedUser.getImage().getId());
    }
}