package ru.ac.secondhand.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.IncorrectPasswordException;
import ru.ac.secondhand.exception.UserAlreadyExistException;
import ru.ac.secondhand.mapper.UserMapper;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthServiceImpl authService;

    private String username;
    private String password;
    private String encodedPassword;
    private RegisterDTO registerDTO;
    private User user;

    @BeforeEach
    void setUp() {
        username = "user";
        password = "password";
        encodedPassword = "encodedPassword";

        registerDTO = new RegisterDTO();
        registerDTO.setUsername(username);
        registerDTO.setPassword(password);

        user = TestUtils.getUserEntity();

    }

    @Test
    public void login_SuccessfulAuthentication_ShouldReturnTrue() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getPassword()).thenReturn(encodedPassword);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        when(encoder.matches(password, encodedPassword)).thenReturn(true);

        boolean isAuthenticated = authService.login(username, password);

        assertTrue(isAuthenticated);

        verify(userDetailsService).loadUserByUsername(username);
        verify(encoder).matches(password, encodedPassword);
    }

    @Test
    public void login_IncorrectPassword_ShouldThrowException() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getPassword()).thenReturn(encodedPassword);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        when(encoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authService.login(username, password));

        verify(userDetailsService).loadUserByUsername(username);
        verify(encoder).matches(password, encodedPassword);
    }

    @Test
    public void register_NewUser_ShouldReturnTrue() {
        when(userRepository.findByUsername(registerDTO.getUsername())).thenReturn(Optional.empty());
        when(userMapper.registerDTOToUser(registerDTO)).thenReturn(user);
        when(encoder.encode(password)).thenReturn(encodedPassword);

        boolean isRegistered = authService.register(registerDTO);

        assertTrue(isRegistered);
        verify(encoder).encode(password);
        verify(userRepository).save(user);

    }

    @Test
    public void register_ExistingUser_ShouldThrowException() {
        when(userRepository.findByUsername(registerDTO.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistException.class, () -> authService.register(registerDTO));

        verify(userRepository).findByUsername(registerDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }
}
