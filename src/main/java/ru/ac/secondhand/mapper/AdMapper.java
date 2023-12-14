package ru.ac.secondhand.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Ad toAdEntity(CreateOrUpdateAd ad);

    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    @Mapping(target = "pk", source = "ad.id")
    @Mapping(target = "authorFirstName", source = "ad.user.firstName")
    @Mapping(target = "authorLastName", source = "ad.user.lastName")
    @Mapping(target = "email", source = "ad.user.username")
    @Mapping(target = "phone", source = "ad.user.phone")
    ExtendedAd toExtendedAd(Ad ad);

    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    @Mapping(target = "author", source = "ad.user.id")
    @Mapping(target = "pk", source = "ad.id")
    ru.ac.secondhand.dto.ad.Ad toAdDTO(Ad ad);

    default Ads toAds(List<Ad> ads) {
        List<ru.ac.secondhand.dto.ad.Ad> adDTOs = ads.stream()
                .map(this::toAdDTO)
                .collect(Collectors.toList());
        Ads adsDTO = new Ads();
        adsDTO.setResults(adDTOs);
        adsDTO.setCount(adDTOs.size());
        return adsDTO;
    }

    @Named("imageToString")
    default String imageToString(Image image) {
        if (image == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(image.getImage());
    }
}
