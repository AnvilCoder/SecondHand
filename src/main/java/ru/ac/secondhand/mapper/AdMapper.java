package ru.ac.secondhand.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между объектами объявлений и их DTO представлениями.
 * <p>
 * Этот интерфейс определён с использованием MapStruct для автоматического маппинга
 * данных между сущностями объявлений ({@code Ad}) и различными DTO ({@code CreateOrUpdateAd},
 * {@code ExtendedAd}, {@code AdDTO}, {@code Ads}). Он обеспечивает гибкость и безопасность типов
 * при конвертации данных в приложении.
 * </p>
 * <p>
 * Интерфейс содержит методы для преобразования сущности объявления в DTO,
 * в том числе методы для создания расширенных и сокращённых представлений объявления.
 * Также предусмотрен метод {@code toAds} для преобразования списка объявлений в их DTO представления.
 * </p>
 *
 * @author fifimova
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Ad toAdEntity(CreateOrUpdateAd ad);

    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/ads/image/\" + ad.getImage().getId() : null)")
    @Mapping(target = "pk", source = "ad.id")
    @Mapping(target = "authorFirstName", source = "ad.user.firstName")
    @Mapping(target = "authorLastName", source = "ad.user.lastName")
    @Mapping(target = "email", source = "ad.user.username")
    @Mapping(target = "phone", source = "ad.user.phone")
    ExtendedAd toExtendedAd(Ad ad);

    @Mapping(target = "image", expression = "java(ad.getImage() != null ? \"/ads/image/\" + ad.getImage().getId() : null)")
    @Mapping(target = "author", source = "ad.user.id")
    @Mapping(target = "pk", source = "ad.id")
    AdDTO toAdDTO(Ad ad);

    default Ads toAds(List<Ad> ads) {
        List<AdDTO> adDTOs = ads.stream()
                .map(this::toAdDTO)
                .collect(Collectors.toList());
        Ads adsDTO = new Ads();
        adsDTO.setResults(adDTOs);
        adsDTO.setCount(adDTOs.size());
        return adsDTO;
    }
}