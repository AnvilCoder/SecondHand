package ru.ac.secondhand.utils;

import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.entity.enums.Role;

import java.util.Random;

public class TestUtils {

    public static Integer AD_ID = 1;
    public static Integer USER_ID = 1;
    public static Integer IMAGE_ID = 1;
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static Integer PRICE = 1;

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

    public static CreateOrUpdateAd getCreateOrUpdateAd() {
        return new CreateOrUpdateAd(
                TITLE,
                PRICE,
                DESCRIPTION
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
}
