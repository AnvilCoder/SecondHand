package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.exception.IncorrectPasswordException;
import ru.ac.secondhand.exception.UserAlreadyExistException;
import ru.ac.secondhand.mapper.UserMapper;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.AuthService;
import ru.ac.secondhand.utils.MethodLog;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    /**
     * Аутентификация пользователя
     *
     * @param userName - логин пользователя
     * @param password - пароль пользователя
     * @return true, если аутентификация прошла успешно
     */
    @Override
    public boolean login(String userName, String password) {
        log.info("Method {}, {}", MethodLog.getMethodName(), userName);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            log.info(String.format("Incorrect password for user %s", userName));
            throw new IncorrectPasswordException(String.format("Incorrect password for user %s", userName));
        }
        log.info(String.format("User login [%s]", userName));
        return true;
    }

    /**
     * Регистрация нового пользователя
     *
     * @param registerDTO -  {@link RegisterDTO}
     * @return true, если регистрация прошла успешно
     */
    @Override
    public boolean register(RegisterDTO registerDTO) {
        log.info("Method {}, {}", MethodLog.getMethodName(), registerDTO);
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            log.info(String.format("User already exist [%s]", registerDTO.getUsername()));
            throw new UserAlreadyExistException(String.format("User already exist [%s]", registerDTO.getUsername()));
        }
        registerDTO.setPassword(encoder.encode(registerDTO.getPassword()));
        userRepository.save(userMapper.registerDTOToUser(registerDTO));
        log.info(String.format("User register [%s]", registerDTO.getUsername()));
        return true;
    }

}
