package ru.ac.secondhand.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.dto.comment.CommentDTO;
import ru.ac.secondhand.dto.comment.CreateOrUpdateComment;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Comment;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.entity.enums.Role;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {

    public static Integer AD_ID = 1;
    public static Integer USER_ID = 1;
    public static Integer IMAGE_ID = 1;
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static Integer PRICE = 1;
    public static String ADD_IMAGE_PATH = "/ads/image";
    public static Integer COMMENT_ID = 1;
    public static String COMMENT_TEXT = "Test comment text";
    public static LocalDateTime CREATED_AT = LocalDateTime.now();
    public static Integer AUTHOR_ID = 1;
    public static String AUTHOR_FIRST_NAME = "John";
    public static String AUTHOR_IMAGE = "/path/to/image.jpg";

    private static final byte[] BYTE_ARRAY;

    static {
        BYTE_ARRAY = new byte[20]; // Пример размера, можно изменить
        new Random().nextBytes(BYTE_ARRAY);
    }

    public static Ad getAdEntity() {
        Ad ad = new Ad();
        ad.setId(AD_ID);
        ad.setTitle(TITLE);
        ad.setDescription(DESCRIPTION);
        ad.setPrice(PRICE);
        ad.setUser(getUserEntity());
        ad.setImage(getImage());
        return ad;
    }

    public static AdDTO getAdDTO() {
        return new AdDTO(
                USER_ID,
                ADD_IMAGE_PATH,
                AD_ID,
                PRICE,
                TITLE
        );
    }

    public static Ads getAds() {
        return new Ads(
                1,
                List.of(getAdDTO())
        );
    }

    public static CreateOrUpdateAd getCreateOrUpdateAd() {
        return new CreateOrUpdateAd(
                TITLE,
                PRICE,
                DESCRIPTION
        );
    }

    public static ExtendedAd getExtendedAd() {
        return new ExtendedAd(
                AD_ID,
                "first",
                "last",
                DESCRIPTION,
                "username@gmail.com",
                ADD_IMAGE_PATH,
                "79998886655",
                PRICE,
                TITLE
        );
    }

    public static User getUserEntity() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername("username@gmail.com");
        user.setPassword("password");
        user.setFirstName("first");
        user.setLastName("last");
        user.setPhone("79998886655");
        user.setRole(Role.USER);
        user.setImage(getImage());
        return user;
    }

    public static RegisterDTO getRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("username@gmail.com");
        registerDTO.setPassword("password");
        registerDTO.setFirstName("first");
        registerDTO.setLastName("last");
        registerDTO.setPhone("79998886655");
        registerDTO.setRole(Role.USER);
        return registerDTO;
    }

    public static UpdateUserDTO getUpdateUserDTO() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("updateFirst");
        updateUserDTO.setLastName("updateLast");
        updateUserDTO.setPhone("12345678900");
        return updateUserDTO;
    }

    public static Image getImage() {
        Image image = new Image();
        image.setId(IMAGE_ID);
        image.setImage(BYTE_ARRAY);
        return image;
    }

    public static MultipartFile getMultipartFile() {
        byte[] content = "test image content".getBytes();
        return new MockMultipartFile("file", "test.jpg", "image/jpeg", content);
    }

    public static Comment getCommentEntity() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText(COMMENT_TEXT);
        comment.setCreatedAt(CREATED_AT);
        comment.setAd(getAdEntity());
        comment.setUser(getUserEntity());
        return comment;
    }

    public static CommentDTO getCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPk(COMMENT_ID);
        commentDTO.setText(COMMENT_TEXT);
        commentDTO.setCreatedAt(CREATED_AT.toEpochSecond(ZoneOffset.UTC));
        commentDTO.setAuthor(AUTHOR_ID);
        commentDTO.setAuthorImage(AUTHOR_IMAGE);
        commentDTO.setAuthorFirstName(AUTHOR_FIRST_NAME);
        return commentDTO;
    }

    public static CreateOrUpdateComment getCreateOrUpdateComment() {
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText(COMMENT_TEXT);
        return createOrUpdateComment;
    }

    public static List<CommentDTO> getCommentDTOList() {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        commentDTOs.add(getCommentDTO());
        return commentDTOs;
    }

    public static List<Comment> getCommentList() {
        List<Comment> comments = new ArrayList<>();
        comments.add(getCommentEntity());
        return comments;
    }
}
