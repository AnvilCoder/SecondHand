package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.Ad;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.mapper.AdMapper;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.AdService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper mapper;

    private static String AD_NOT_FOUND_MSG = "Ad with [id=%s] not found";

    @Override
    public Ads getAll() {
        List<ru.ac.secondhand.entity.Ad> ads = adRepository.findAll();
        return mapper.toAds(ads);
    }

    @Override
    public ExtendedAd getAdInfo(Integer id) {
        ru.ac.secondhand.entity.Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        return mapper.toExtendedAd(ad);
    }

    @Override
    public Ads getUsersAds() {
        User user = getUser();
        List<ru.ac.secondhand.entity.Ad> ads = adRepository.findAdsByUserId(user.getId());
        return mapper.toAds(ads);
    }

    @Override
    public Ad createAd(CreateOrUpdateAd adDTO) {
        User user = getUser();
        ru.ac.secondhand.entity.Ad ad = mapper.toAdEntity(adDTO);
        ad.setUser(user);
        adRepository.save(ad);
        log.info("Ad {} {} saved", ad.getId(), ad.getTitle());
        return mapper.toAdDTO(ad);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd adDTO) {
        ru.ac.secondhand.entity.Ad ad = adRepository.findById(id).orElseThrow(() -> {
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
        //TODO
        return null;
    }

    @Override
    public void deleteAd(Integer id) {
        ru.ac.secondhand.entity.Ad ad = adRepository.findById(id).orElseThrow(() -> {
            log.warn(AD_NOT_FOUND_MSG, id);
            return new AdNotFoundException(String.format(AD_NOT_FOUND_MSG, id));
        });
        log.info("Ad {} {} deleted",ad.getId(), ad.getTitle());
        adRepository.delete(ad);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}
