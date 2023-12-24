package ru.ac.secondhand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.ImageService;

@RestController
@RequestMapping("ads")
@RequiredArgsConstructor
@Validated
@Tag(name = "Объявления", description = "Интерфейс для управления объявлениями о продаже")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401",
                description = "UNAUTHORIZED: пользователь не авторизован"),
        @ApiResponse(responseCode = "500",
                description = "INTERNAL_SERVER_ERROR: Ошибка сервера при обработке запроса")})
public class AdsController {

    private final AdService adService;
    private final ImageService imageService;

    @Operation(summary = "Получить список всех объявлений")
    @ApiResponse(
            responseCode = "200",
            description = "OK: возвращает список пользователей",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Ads.class)))
    )
    @GetMapping
    public ResponseEntity<?> getAll() {
        Ads ads = adService.getAll();
        return ResponseEntity.ok(ads);
    }

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: возвращает инфо об объявлении",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExtendedAd.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление не найдено"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdInfo(@PathVariable("id") Integer id) {
        ExtendedAd ad = adService.getAdInfo(id);
        return ResponseEntity.ok(ad);
    }

    @Operation(summary = "Получить список объявлений пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "OK: возвращает список пользователей",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Ads.class)))
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUsersAds() {
        Ads ads = adService.getUsersAds();
        return ResponseEntity.ok(ads);
    }

    @Operation(summary = "Создать объявление")
    @ApiResponse(
            responseCode = "201",
            description = "CREATED: объявление создано",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdDTO.class))
    )
    @PostMapping
    public ResponseEntity<?> createAdd(@RequestBody CreateOrUpdateAd ad) {
        AdDTO createdAd = adService.createAd(ad);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    @Operation(summary = "Изменить объявление")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK: объявление изменено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: роль пользователя не предоставляет доступ к данному api"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление не найдено"
            )
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isOwner(authentication.name, #id)")
    public ResponseEntity<?> updateAd(@PathVariable Integer id,
                                      @RequestBody CreateOrUpdateAd ad) {
        AdDTO updatedAd = adService.updateAd(id, ad);
        return ResponseEntity.ok(updatedAd);
    }


    @Operation(summary = "Обновление изображения объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "FORBIDDEN: роль пользователя не предоставляет доступ к данному api"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND: объявление не найдено"
                    )
            })
    @PatchMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isOwner(authentication.name, #id)")
    public ResponseEntity<?> updateAdImage(@PathVariable("id") Integer id,
                                           @RequestParam("image") MultipartFile image) {
        String imageURL = adService.updateAdImage(id, image); //TODO: написать нормальную загрузку
        return ResponseEntity.ok(imageURL);
    }

    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "NO_CONTENT: объявление удалено"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "FORBIDDEN: роль пользователя не предоставляет доступ к данному api"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT_FOUND: объявление не найдено"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isOwner(authentication.name, #id)")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) {
        byte[] imageData = imageService.getImage(id);
        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}