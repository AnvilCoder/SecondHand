package ru.ac.secondhand.dto.ad;

import lombok.Data;

@Data
public class Ad { // response

    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

}
