package ru.ac.secondhand.dto.comment;

import lombok.Data;

import java.util.List;

@Data
public class Comments {

    private final Integer count;
    private final List<Comment> comments;
}
