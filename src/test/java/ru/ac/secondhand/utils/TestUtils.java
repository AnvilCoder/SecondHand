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

    public static final String STANDARD_USERNAME = "testUser";
    public static final String OTHER_USERNAME = "anotherUser";
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
    public static final Integer MAX_SIZE = 3 * 1024 * 1024;
    public static final String VALID_FILENAME = "valid_filename.jpg";
    public static final String INVALID_FILENAME_1 = "../secret/config.txt";
    public static final String INVALID_FILENAME_2 = "invalid<name>.txt";
    public static final String INVALID_FILENAME_3 = "invalid>name>.txt";
    public static final String INVALID_FILENAME_4 = "invalid\"name.txt";
    public static final String FILE_CONTENT = "image data";
    public static final String TEXT_FILE_TYPE = "text/plain";
    public static final String JPEG_FILE_TYPE = "image/jpeg";
    public static final String PNG_FILE_TYPE = "image/png";
    public static final String GIF_FILE_TYPE = "image/gif";
    public static final String FILE_VALID_SIZE = "File size within valid range should return true";
    public static final String UUID_START_MSG = "Filename should start with a UUID.";
    public static final String ORIGINAL_FILENAME_END_MSG = "Filename should end with the original filename.";
    public static final String INVALID_FILE_SIZE_MSG = "File size above valid range should return false";
    public static final String VALID_JPEG_TYPE_MSG = "JPEG file type should be valid";
    public static final String VALID_PNG_TYPE_MSG = "PNG file type should be valid";
    public static final String INVALID_GIF_TYPE_MSG = "GIF file type should be invalid";
    public static final String VALID_FILENAME_TRUE_MSG = "Valid filename should return true";
    public static final String MATCH_EXPECTED_DATA_MSG = "The retrieved image data should match the expected data.";
    public static final String IMAGE_NOT_FOUND_EXCEPTION_MSG = "Should throw ImageNotFoundException when image does not exist.";
    public static final String FILE_NAME = "filename.txt";
    public static final String SAVED_IMAGE_NOT_NULL_MSG = "Saved image should not be null";
    public static final String LARGE_IMAGE_SIZE_EXCEPTION_MSG = "Should throw InvalidFileException for large image size";
    public static final String INVALID_IMAGE_TYPE_EXCEPTION_MSG = "Should throw InvalidFileException for invalid image type";
    public static final String INVALID_IMAGE_NAME_EXCEPTION_MSG = "Should throw InvalidFileException for invalid image name";


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
        return User.builder().
                id(USER_ID).
                username("username@gmail.com").
                password("password").
                firstName("first").
                lastName("last").
                phone("79998886655").
                role(Role.USER).
                image(getImage()).build();
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
        image.setImagePath(ADD_IMAGE_PATH);
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

    public static User createStandardUser() {
        User user = new User();
        user.setUsername(STANDARD_USERNAME);
        return user;
    }

    public static Ad createStandardAd() {
        Ad ad = new Ad();
        ad.setId(AD_ID);
        return ad;
    }

    public static Comment createStandardComment() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        return comment;
    }

    public static Comment createCastomComment() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setCreatedAt(LocalDateTime.now());

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("John Doe");

        Image image = new Image();
        image.setId(IMAGE_ID);
        user.setImage(image);

        comment.setUser(user);
        return comment;
    }

}
