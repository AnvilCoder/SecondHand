package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;

public interface AdService {

    Ads getAll();

    ExtendedAd getAdInfo(Integer id);

    Ads getUsersAds();

    AdDTO createAd(CreateOrUpdateAd ad, MultipartFile image);

    AdDTO updateAd(Integer id, CreateOrUpdateAd ad);

    void updateAdImage(Integer id, MultipartFile image);

    void deleteAd(Integer id);

    Ad getAdById(Integer adId);

    boolean isOwner(String username, Integer id);
}
