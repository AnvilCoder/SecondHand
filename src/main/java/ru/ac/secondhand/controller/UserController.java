package ru.ac.secondhand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.user.NewPassword;
import ru.ac.secondhand.dto.user.UpdateUser;
import ru.ac.secondhand.dto.user.User;
import ru.ac.secondhand.service.UserService;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Пользователи", description = "Управление данными пользователей")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED: пользователь не авторизован"),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN: нет доступа"),
        @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR: ошибка сервера при обработке запроса")})
public class UserController {

    private final UserService userService;

    @Operation(summary = "Обновление пароля")
    @ApiResponse(
            responseCode = "200", description = "OK: пароль изменен")
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {
        userService.setPassword(newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(
            responseCode = "200", description = "OK: данные пользователя найдены",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class))
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUserData() {
        User user = userService.getUserData();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(
            responseCode = "200", description = "OK: данные пользователя обновлены",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateUser.class))
    )
    @PatchMapping("/me")
    public ResponseEntity<?> updateUserData(UpdateUser updateUser) {
        UpdateUser user = userService.updateUser(updateUser);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(
            responseCode = "200", description = "OK: аватар обновлен",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdateUser.class))
    )
    @PatchMapping("/me/image")
    public ResponseEntity<?> updateUserAvatar(@RequestPart MultipartFile image) {
        userService.updateAvatar(image);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
