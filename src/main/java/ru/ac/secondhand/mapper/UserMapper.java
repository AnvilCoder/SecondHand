package ru.ac.secondhand.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ac.secondhand.dto.user.RegisterDTO;
import ru.ac.secondhand.dto.user.UpdateUserDTO;
import ru.ac.secondhand.dto.user.UserDTO;
import ru.ac.secondhand.entity.User;
/**
 * Интерфейс-маппер для преобразования между объектами пользователь и их DTO представлениями.
 * <p>
 * Этот интерфейс определён с использованием MapStruct для автоматического маппинга
 * данных между сущностями комментариев ({@code User}) и различными DTO ({@code UpdateUserDTO},
 * {@code UserDTO}, {@code RegisterDTOToUser}). Он обеспечивает гибкость и безопасность типов
 * при конвертации данных в приложении.
 * </p>
 * <p>
 * Интерфейс содержит методы для преобразования сущности объявления в DTO,
 * в том числе методы для создания расширенных и сокращённых представлений объявления.
 * </p>
 *
 * @author x3imal
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User registerDTOToUser(RegisterDTO registerDTO);

    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/image/\" + user.getImage().getId() : null)")
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
