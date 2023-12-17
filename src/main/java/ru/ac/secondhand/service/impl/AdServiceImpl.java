package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.mapper.AdMapper;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper mapper;
    private final ImageService imageService;

    private static String AD_NOT_FOUND_MSG = "Ad with [id=%s] not found";

    @Override
    @Transactional(readOnly = true)
    public Ads getAll() {
        List<Ad> ads = adRepository.findAll();
        return mapper.toAds(ads);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAdInfo(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        return mapper.toExtendedAd(ad);
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getUsersAds() {
        User user = userService.findUser();
        List<Ad> ads = adRepository.findAdsByUserId(user.getId());
        return mapper.toAds(ads);
    }

    @Override
    public AdDTO createAd(CreateOrUpdateAd adDTO) {
        User user = userService.findUser();
        Ad ad = mapper.toAdEntity(adDTO);
        ad.setUser(user);
        adRepository.save(ad);
        log.info("Ad {} {} saved", ad.getId(), ad.getTitle());
        return mapper.toAdDTO(ad);
    }

    @Override
    public AdDTO updateAd(Integer id, CreateOrUpdateAd adDTO) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        ad.setTitle(adDTO.getTitle());
        ad.setPrice(adDTO.getPrice());
        ad.setDescription(adDTO.getDescription());
        adRepository.save(ad);
        log.info("Ad {} {} saved", ad.getId(), ad.getTitle());
        return mapper.toAdDTO(ad);
    }

    @Override
    public String updateAdImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        if (ad.getImage() != null) {
            imageService.deleteImage(ad.getImage().getId());
        }

        Image newImage = imageService.saveImage(image);
        Integer imageId = newImage.getId();
        ad.setImage(newImage);
        adRepository.save(ad);

        return "/ads/image/" + imageId;
    }

    @Override
    public void deleteAd(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        log.info("Ad {} {} deleted", ad.getId(), ad.getTitle());
        adRepository.delete(ad);
    }
}
