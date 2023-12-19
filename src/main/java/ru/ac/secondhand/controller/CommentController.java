package ru.ac.secondhand.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads")
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


    @Operation(summary = "Получить комментарии объявления.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии найдены.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Comments.class))),
            @ApiResponse(responseCode = "404", description = "Комментарии не найдены.")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable("id") Integer adId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить новый комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable("id") Integer adId,
                                        @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "403", description = "Нехватает прав для этой команды."),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить/редактировать комментарий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "403", description = "Нехватает прав для этой команды."),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден.")
    })
    @PutMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("adId") Integer adId, @PathVariable("commentId") Integer commentId,
                                           @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }
}
