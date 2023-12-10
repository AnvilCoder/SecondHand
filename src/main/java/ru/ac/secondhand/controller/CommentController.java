package ru.ac.secondhand.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Комментарии", description = "Интерфейс для управления комментариями.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401",
                description = "UNAUTHORIZED: пользователь не авторизован"),
        @ApiResponse(responseCode = "500",
                description = "INTERNAL_SERVER_ERROR: Ошибка сервера при обработке запроса")})
public class CommentController {


    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable("adId") Integer adId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addComment(@PathVariable("adId") Integer adId,
                                        @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("adId") Integer id, @PathVariable("commentId") Integer commentId,
                                           @RequestBody CreateOrUpdateComment commentRequest) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().build();
    }
 }
