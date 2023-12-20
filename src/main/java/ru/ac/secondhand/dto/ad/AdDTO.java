package ru.ac.secondhand.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO { // response

    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

}
