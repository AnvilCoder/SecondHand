package ru.ac.secondhand.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.entity.enums.Role;

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
        image.setImage(BYTE_ARRAY);
        return image;
    }

    public static MultipartFile getMultipartFile() {
        byte[] content = "test image content".getBytes();
        return new MockMultipartFile("file", "test.jpg", "image/jpeg", content);
    }
}
