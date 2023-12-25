package ru.ac.secondhand.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.entity.Comment;
import ru.ac.secondhand.utils.TestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentMapperTest {

    private CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void toComments_Success() {
        Comment comment1 = TestUtils.createStandardComment();
        Comment comment2 = TestUtils.createStandardComment();
        List<Comment> comments = Arrays.asList(comment1, comment2);

        Comments commentsDTO = mapper.toComments(comments);

        assertEquals(2, commentsDTO.getCount());
        assertEquals(2, commentsDTO.getResults().size());
    }

    @Test
    void toCommentDTO_Success() {
        Long expectedTimestamp = System.currentTimeMillis();
        Comment comment = TestUtils.createCastomComment();
        CommentDTO commentDTO = mapper.toCommentDTO(comment);

        assertEquals(1, commentDTO.getPk());
        assertEquals("John Doe", commentDTO.getAuthorFirstName());
        assertEquals("/image/1", commentDTO.getAuthorImage());

        Long actualTimestamp = commentDTO.getCreatedAt();
        long tolerance = 100;

        assertTrue(Math.abs(expectedTimestamp - actualTimestamp) <= tolerance);
    }

    @Test
    void toCommentDTO_NullUser_Success() {
        Comment comment = TestUtils.createStandardComment();
        comment.setUser(null);

        CommentDTO commentDTO = mapper.toCommentDTO(comment);

        assertNull(commentDTO.getAuthorFirstName());
        assertNull(commentDTO.getAuthorImage());
    }
}
