package ru.ac.secondhand.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.utils.TestUtils;

import java.util.Base64;
import java.util.List;

class AdMapperTest {

    private AdMapper mapper = Mappers.getMapper(AdMapper.class);


    @Test
    void shouldMapCreateOrUpdateAdToAdEntity() {
        CreateOrUpdateAd createOrUpdateAd = TestUtils.getCreateOrUpdateAd();
        Ad adEntity = mapper.toAdEntity(createOrUpdateAd);

        Assertions.assertThat(adEntity.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        Assertions.assertThat(adEntity.getPrice()).isEqualTo(createOrUpdateAd.getPrice());
        Assertions.assertThat(adEntity.getDescription()).isEqualTo(createOrUpdateAd.getDescription());

    }

    @Test
    void shouldMapAdEntityToExtendedAd() {
        Ad ad = TestUtils.getAdEntity();
        ExtendedAd extendedAd = mapper.toExtendedAd(ad);

        Assertions.assertThat(extendedAd.getPk()).isEqualTo(ad.getId());
        Assertions.assertThat(extendedAd.getAuthorFirstName()).isEqualTo(ad.getUser().getFirstName());
        Assertions.assertThat(extendedAd.getAuthorLastName()).isEqualTo(ad.getUser().getLastName());
        Assertions.assertThat(extendedAd.getDescription()).isEqualTo(ad.getDescription());
        Assertions.assertThat(extendedAd.getEmail()).isEqualTo(ad.getUser().getUsername());
        Assertions.assertThat(ad.getImage().getImage()).asBase64Encoded().isEqualTo(extendedAd.getImage());
        Assertions.assertThat(extendedAd.getPhone()).isEqualTo(ad.getUser().getPhone());
        Assertions.assertThat(extendedAd.getPrice()).isEqualTo(ad.getPrice());
        Assertions.assertThat(extendedAd.getTitle()).isEqualTo(ad.getTitle());

    }

    @Test
    void shouldMapAdEntityToAdDTO() {
        Ad ad = TestUtils.getAdEntity();
        ru.ac.secondhand.dto.ad.Ad adDTO = mapper.toAdDTO(ad);

        Assertions.assertThat(ad.getUser().getId()).isEqualTo(adDTO.getAuthor());
        Assertions.assertThat((ad.getImage().getImage())).asBase64Encoded().isEqualTo(adDTO.getImage());
        Assertions.assertThat(ad.getId()).isEqualTo(adDTO.getPk());
        Assertions.assertThat(ad.getPrice()).isEqualTo(adDTO.getPrice());
        Assertions.assertThat(ad.getTitle()).isEqualTo(adDTO.getTitle());
    }

    @Test
    void shouldMapListOfAdEntitiesToAds() {
        Ad ad = TestUtils.getAdEntity();
        List<Ad> ads = List.of(ad);
        Ads adsDTO = mapper.toAds(ads);

        Assertions.assertThat(adsDTO).isNotNull();
        Assertions.assertThat(adsDTO.getCount()).isEqualTo(ads.size());
        Assertions.assertThat(adsDTO.getResults().size()).isEqualTo(ads.size());
    }

    @Test
    public void shouldConvertImageToBase64String() {
        Image image = TestUtils.getImage();

        String expected = Base64.getEncoder().encodeToString(image.getImage());
        String actual = mapper.imageToString(image);

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void shouldReturnNullForNullImage() {
        Assertions.assertThat(mapper.imageToString(null)).isNull();
    }
}