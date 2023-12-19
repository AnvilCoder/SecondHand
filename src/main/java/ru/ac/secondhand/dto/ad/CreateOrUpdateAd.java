package ru.ac.secondhand.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateAd { // post

    @Size(min = 4, max = 32)
    private String title;

    @PositiveOrZero
    private Integer price;

    @Size(min = 8, max = 64)
    private String description;
}
