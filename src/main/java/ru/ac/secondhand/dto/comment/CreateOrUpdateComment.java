package ru.ac.secondhand.dto.comment;


import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateComment {

    @Size(min = 8, max = 64)
    private final String text;
}