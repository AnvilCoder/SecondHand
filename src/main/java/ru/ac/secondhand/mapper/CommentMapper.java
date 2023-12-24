package ru.ac.secondhand.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.Comments;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.entity.Comment;
import ru.ac.secondhand.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default Comments toComments(List<Comment> comments) {
        List<CommentDTO> commentDTOS = comments.stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
        Comments commentsDTO = new Comments();
        commentsDTO.setResults(commentDTOS);
        commentsDTO.setCount(commentDTOS.size());
        return commentsDTO;
    }

    @Mapping(target = "author", source = "comment.user.id")
    @Mapping(target = "authorImage", expression = "java(mapUserImageToUrl(comment.getUser()))")
    @Mapping(target = "authorFirstName", source = "comment.user.firstName")
    @Mapping(target = "createdAt", expression = "java(mapLocalDateTimeToLong(comment.getCreatedAt()))")
    @Mapping(target = "pk", source = "comment.id")
    CommentDTO toCommentDTO(Comment comment);

    default Long mapLocalDateTimeToLong(LocalDateTime dateTime) { // выражение слишком длинное выходит, поэтому дефолтный метод
        return dateTime == null ? null : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    default String mapUserImageToUrl(User user) { // та же фигня
        if (user != null && user.getImage() != null) {
            return "/users/image/" + user.getImage().getId();
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment toComment(CreateOrUpdateComment comment);
}
