package ru.ac.secondhand.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.mapper.AdMapper;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.service.UserService;
import ru.ac.secondhand.utils.TestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    AdRepository adRepository;
    @Mock
    UserService userService;
    @Mock
    AdMapper mapper;
    @Mock
    ImageService imageService;

    @InjectMocks
    AdServiceImpl adService;

    @Test
    void shouldReturnAds() {
        List<Ad> ads = List.of(TestUtils.getAdEntity());

        Mockito.when(adRepository.findAll()).thenReturn(ads);
        Mockito.when(mapper.toAds(ads)).thenReturn(TestUtils.getAds());

        Ads adsDTO = adService.getAll();
        Assertions.assertThat(adsDTO.getCount()).isEqualTo(ads.size());
        Assertions.assertThat(adsDTO.getResults().size()).isEqualTo(ads.size());
        Mockito.verify(adRepository).findAll();
    }

    @Test
    void getAllReturnEmptyListDontThrowException() {
        List<Ad> ads = Collections.emptyList();
        Ads adsExp = new Ads(0, Collections.emptyList());

        Mockito.when(adRepository.findAll()).thenReturn(ads);
        Mockito.when(mapper.toAds(ads)).thenReturn(adsExp);

        Assertions.assertThatNoException().isThrownBy(() -> adService.getAll());
        Mockito.verify(adRepository).findAll();
    }

    @Test
    void getAdInfoShouldReturnExtendedAd() {
        Ad ad = TestUtils.getAdEntity();
        Integer adId = ad.getId();
        ExtendedAd adDTO = TestUtils.getExtendedAd();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        Mockito.when(mapper.toExtendedAd(ad)).thenReturn(adDTO);

        ExtendedAd result = adService.getAdInfo(adId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasNoNullFieldsOrProperties();

        Mockito.verify(adRepository).findById(adId);
        Mockito.verify(mapper).toExtendedAd(ad);
    }

    @Test
    void getAdInfoShouldThrowException() {
        Integer adId = TestUtils.AD_ID;
        Mockito.when(adRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> adService.getAdInfo(adId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Ad with [id=%s] not found", adId));
    }

    @Test
    void getUsersAdsShouldReturnAds() {
        List<Ad> ads = List.of(TestUtils.getAdEntity());
        User user = TestUtils.getUserEntity();
        Integer userId = user.getId();

        Mockito.when(userService.findUser()).thenReturn(user);
        Mockito.when(adRepository.findAdsByUserId(userId)).thenReturn(ads);
        Mockito.when(mapper.toAds(ads)).thenReturn(TestUtils.getAds());

        Ads result = adService.getUsersAds();
        Assertions.assertThat(result.getCount()).isEqualTo(ads.size());
        Assertions.assertThat(result.getResults().size()).isEqualTo(ads.size());

        Mockito.verify(userService).findUser();
        Mockito.verify(adRepository).findAdsByUserId(userId);
        Mockito.verify(mapper).toAds(ads);
    }

    @Test
    void getUsersAdsShouldReturnEmptyListWithoutThrowingException() {
        List<Ad> ads = Collections.emptyList();
        Ads adsExp = new Ads(0, Collections.emptyList());
        User user = TestUtils.getUserEntity();
        Integer userId = user.getId();

        Mockito.when(userService.findUser()).thenReturn(user);
        Mockito.when(adRepository.findAdsByUserId(userId)).thenReturn(ads);
        Mockito.when(mapper.toAds(ads)).thenReturn(adsExp);

        Assertions.assertThatNoException().isThrownBy(() -> adService.getUsersAds());
        Mockito.verify(adRepository).findAdsByUserId(userId);
    }

    @Test
    void createAdShouldReturnAdDTO() {
        CreateOrUpdateAd createOrUpdateAd = TestUtils.getCreateOrUpdateAd();
        Ad ad = TestUtils.getAdEntity();
        User user = TestUtils.getUserEntity();
        Integer userId = user.getId();

        Mockito.when(userService.findUser()).thenReturn(user);
        Mockito.when(mapper.toAdEntity(createOrUpdateAd)).thenReturn(ad);
        Mockito.when(mapper.toAdDTO(ad)).thenReturn(TestUtils.getAdDTO());

        AdDTO result = adService.createAd(createOrUpdateAd);
        Assertions.assertThat(result).hasNoNullFieldsOrProperties();
        Assertions.assertThat(result.getAuthor()).isEqualTo(userId);
        Mockito.verify(adRepository).save(ad);
    }

    @Test
    void updateAdShouldReturnUpdatedAdDTO() {
        CreateOrUpdateAd createOrUpdateAd = TestUtils.getCreateOrUpdateAd();
        Ad ad = TestUtils.getAdEntity();
        Integer adId = ad.getId();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        Mockito.when(mapper.toAdDTO(ad)).thenReturn(TestUtils.getAdDTO());

        AdDTO result = adService.updateAd(adId, createOrUpdateAd);
        Assertions.assertThat(result.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        Assertions.assertThat(result.getPrice()).isEqualTo(createOrUpdateAd.getPrice());
        Assertions.assertThat(result).hasNoNullFieldsOrProperties();
        Mockito.verify(adRepository).save(ad);
    }

    @Test
    void updateAdShouldThrowException() {
        Integer adId = TestUtils.AD_ID;
        Mockito.when(adRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> adService.updateAd(adId, TestUtils.getCreateOrUpdateAd()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Ad with [id=%s] not found", adId));
    }

    @Test
    void updateAdImageShouldReturnImagePath() {
        Ad adWithImage = TestUtils.getAdEntity();
        Integer adId = adWithImage.getId();
        MultipartFile image = TestUtils.getMultipartFile();
        Image newImage = TestUtils.getImage();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.of(adWithImage));
        Mockito.when(imageService.saveImage(image)).thenReturn(newImage);
        if (adWithImage.getImage() != null) {
            Mockito.doNothing().when(imageService).deleteImage(adWithImage.getImage().getId());
        }

        String result = adService.updateAdImage(adId, image);

        Assertions.assertThat(result).isEqualTo("/ads/image/" + newImage.getId());
        Mockito.verify(adRepository).save(adWithImage);
        Mockito.verify(imageService).saveImage(image);
        if (adWithImage.getImage() != null) {
            Mockito.verify(imageService).deleteImage(adWithImage.getImage().getId());
        }
    }

    @Test
    void updateAdImageShouldThrowAdNotFoundException() {
        Integer adId = TestUtils.AD_ID;
        MultipartFile image = TestUtils.getMultipartFile();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> adService.updateAdImage(adId, image))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Ad with [id=%s] not found", adId));

    }

    @Test
    void updateAdImageShouldThrowExceptionWhenImageSaveFails() {
        Ad adWithImage = TestUtils.getAdEntity();
        Integer adId = adWithImage.getId();
        MultipartFile image = TestUtils.getMultipartFile();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.of(adWithImage));
        Mockito.when(imageService.saveImage(image)).thenThrow(new RuntimeException("Image save failed"));

        Assertions.assertThatThrownBy(() -> adService.updateAdImage(adId, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Image save failed");
    }

    @Test
    void deleteAdSuccessfulDeletion() {
        Ad ad = TestUtils.getAdEntity();
        Integer adId = ad.getId();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        Assertions.assertThatNoException().isThrownBy(() -> adService.deleteAd(adId));
        Mockito.verify(adRepository).delete(ad);
    }

    @Test
    void deleteAdThrowsNotFoundException() {
        Ad ad = TestUtils.getAdEntity();
        Integer adId = ad.getId();

        Mockito.when(adRepository.findById(adId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> adService.deleteAd(adId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Ad with [id=%s] not found", adId));
    }
}