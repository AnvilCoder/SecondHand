package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;

public interface AdService {

    Ads getAll();

    ExtendedAd getAdInfo(Integer id);

    Ads getUsersAds();

    AdDTO createAd(CreateOrUpdateAd ad);

    AdDTO updateAd(Integer id, CreateOrUpdateAd ad);

    String updateAdImage(Integer id, MultipartFile image);

    void deleteAd(Integer id);
}
