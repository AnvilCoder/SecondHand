package ru.ac.secondhand.dto.ad;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateAd { // post

    @Size(min = 4, max = 32)
    private String title;

    @PositiveOrZero
    private Integer price;

    @Size(min = 8, max = 64)
    private String description;
}
