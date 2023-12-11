package ru.ac.secondhand.dto.comment;

import lombok.Data;

@Data
public class Comment {

    private final Integer authorId;
    private final String authorImage;
    private final String authorFirstName;
    private final Long createdAt;
    private final Integer pk;
    private final String text;
}
