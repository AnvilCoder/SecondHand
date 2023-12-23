package ru.ac.secondhand.dto.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Comments {

    private Integer count;
    private List<CommentDTO> results;
}
