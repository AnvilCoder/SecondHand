package ru.ac.secondhand.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User registerDTOToUser(RegisterDTO registerDTO);

    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/images/\" + user.getImage().getId() : null)")
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateUserDTOToUser(UpdateUserDTO updateUserDTO, @MappingTarget User user);
}
