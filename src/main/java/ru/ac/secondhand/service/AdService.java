package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.Ad;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;

import java.util.List;

public interface AdService {

    List<Ads> getAll();

    ExtendedAd getAdInfo(Integer id);

    List<Ads> getUsersAds();

    Ad createAd(CreateOrUpdateAd ad);

    Ad uptdateAd(Integer id, CreateOrUpdateAd ad);

    String updateAdImage(Integer id, MultipartFile image);

    void deleteAd(Integer id);
}
