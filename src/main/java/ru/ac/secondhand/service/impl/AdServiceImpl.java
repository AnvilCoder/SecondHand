package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.Ad;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.service.AdService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;

    @Override
    public List<Ads> getAll() {
        return null;
    }

    @Override
    public ExtendedAd getAdInfo(Integer id) {
        return null;
    }

    @Override
    public List<Ads> getUsersAds() {
        return null;
    }

    @Override
    public Ad createAd(CreateOrUpdateAd ad) {
        return null;
    }

    @Override
    public Ad uptdateAd(Integer id, CreateOrUpdateAd ad) {
        return null;
    }

    @Override
    public String updateAdImage(Integer id, MultipartFile image) {
        return null;
    }

    @Override
    public void deleteAd(Integer id) {

    }
}
