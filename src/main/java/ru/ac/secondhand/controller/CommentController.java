package ru.ac.secondhand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.ac.secondhand.dto.comment.Comment;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Комментарии", description = "Интерфейс для управления комментариями.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401",
                description = "UNAUTHORIZED: пользователь не авторизован",
                content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class))),
        @ApiResponse(responseCode = "500",
                description = "INTERNAL_SERVER_ERROR: Ошибка сервера при обработке запроса",
                content = @Content(schema = @Schema(implementation = HttpServerErrorException.InternalServerError.class)))})
public class CommentController {


    @Operation(summary = "Получить комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий найден.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable("adId") Integer adId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить новый комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable("adId") Integer adId,
                                        @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "403", description = "Нехватает прав для этой команды."),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить/редактировать комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "403", description = "Нехватает прав для этой команды."),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("adId") Integer adId, @PathVariable("commentId") Integer commentId,
                                           @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить все комментарии к объявлению.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Comment.class)))),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено.")
    })
    @GetMapping("")
    public ResponseEntity<?> getAllComments(@PathVariable("adId") Integer adId) {
        return ResponseEntity.ok().build();
    }
}
