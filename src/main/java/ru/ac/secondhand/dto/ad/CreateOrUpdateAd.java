package ru.ac.secondhand.dto.ad;

import lombok.Data;

@Data
public class CreateOrUpdateAd {


    private String title;

    private Integer price;

    private String description;
}
