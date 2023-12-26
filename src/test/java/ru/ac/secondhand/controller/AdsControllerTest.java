package ru.ac.secondhand.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.secutity.WithMockCustomUser;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.utils.TestUtils;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class AdsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdService adService;

    @Test
    @WithAnonymousUser
    void getAllIsOKWithoutAuthorize() throws Exception {
        BDDMockito.given(adService.getAll()).willReturn(TestUtils.getAds());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(2))); // Проверяем, что в корне JSON два ключа
    }

    @Test
    @WithAnonymousUser
    void getAllIsOKWithoutAuthorizeShouldReturnEmptyAds() throws Exception {
        BDDMockito.given(adService.getAll()).willReturn(new Ads(0, Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/ads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(0)));
    }

    @Test
    @WithMockCustomUser
    void getAdInfoIsOkReturnExtendedAd() throws Exception {
        Integer adId = TestUtils.AD_ID;
        BDDMockito.given(adService.getAdInfo(adId)).willReturn(TestUtils.getExtendedAd());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pk", is(adId)))
                .andExpect(jsonPath("$.*", hasSize(9)));
    }

    @Test
    @WithAnonymousUser
    void getAdInfoIsUnauthorized() throws Exception {
        Integer adId = TestUtils.AD_ID;

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isUnauthorized());

        Mockito.verify(adService, Mockito.never()).getAdInfo(anyInt());
    }

    @Test
    @WithMockCustomUser
    void getAdInfoNotFoundAd() throws Exception {
        Integer adId = TestUtils.AD_ID;
        BDDMockito.willThrow(new AdNotFoundException("Ad not found for id: " + adId)).given(adService).getAdInfo(adId);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Ad not found for id: " + adId)));
    }

    @Test
    @WithMockCustomUser
    void getUsersAdsIsOk() throws Exception {
        BDDMockito.given(adService.getUsersAds()).willReturn(TestUtils.getAds());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    @WithMockCustomUser
    void getUsersAdsIsOKShouldReturnEmptyAds() throws Exception {
        BDDMockito.given(adService.getUsersAds()).willReturn(new Ads(0, Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count", Matchers.is(0)))
                .andExpect(jsonPath("$.results", Matchers.hasSize(0)));
    }

    @Test
    @WithAnonymousUser
    void getUsersAdsShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(adService, Mockito.never()).getUsersAds();
    }

    @Test
    @WithMockUser
    void createAdHasStatusCreated() throws Exception {
        // Создаем MockMultipartFile для properties и image, все из-за RequestPart!!
        MockMultipartFile image = TestUtils.createImageFile();
        MockMultipartFile properties = TestUtils.createPropertiesFile();

        BDDMockito.given(adService.createAd(any(CreateOrUpdateAd.class), any(MultipartFile.class)))
                .willReturn(TestUtils.getAdDTO());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                        .file(image)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk", Matchers.is(notNullValue())));
    }

    @Test
    @WithAnonymousUser
    void createAdHasStatusUnauthorized() throws Exception {
        MockMultipartFile image = TestUtils.createImageFile();
        MockMultipartFile properties = TestUtils.createPropertiesFile();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                        .file(image)
                        .file(properties))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser
    void updateUsersAdReturnOK() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);
        BDDMockito.given(adService.updateAd(adId, updateInfo)).willReturn(TestUtils.getAdDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void updateAdNotOwnerShouldReturnForbidden() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isForbidden()); //TODO: не работает возвращает 200
    }

    @Test
    @WithMockCustomUser(roles = {"ROLE_ADMIN"})
    void updateUsersAdByAdminReturnOK() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        BDDMockito.given(adService.updateAd(adId, updateInfo)).willReturn(TestUtils.getAdDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void updateUsersAdReturnNotFound() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);
        BDDMockito.willThrow(new AdNotFoundException("Ad not found for id: " + adId)).given(adService).updateAd(adId, updateInfo);

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    void updateAdImageAsOwnerReturnOK() throws Exception {
        Integer adId = TestUtils.AD_ID;
        MockMultipartFile imageFile = TestUtils.createImageFile();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads/{id}/image", adId)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void updateAdImageNotOwnerShouldReturnForbidden() throws Exception {
        Integer adId = TestUtils.AD_ID;
        MockMultipartFile imageFile = TestUtils.createImageFile();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads/{id}/image", adId)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isForbidden()); //TODO: тоже 200
    }

    @Test
    @WithMockCustomUser(roles = {"ROLE_ADMIN"})
    void updateAdImageByAdminReturnOK() throws Exception {
        Integer adId = TestUtils.AD_ID;
        MockMultipartFile imageFile = TestUtils.createImageFile();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads/{id}/image", adId)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    void updateAdImageReturnNotFound() throws Exception {
        Integer adId = TestUtils.AD_ID;
        MockMultipartFile imageFile = TestUtils.createImageFile();

        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);
        BDDMockito.willThrow(new AdNotFoundException("Ad not found for id: " + adId)).given(adService).updateAdImage(adId, imageFile);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads/{id}/image", adId)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser
    void deleteAdAsOwnerOrAdminReturnsNoContent() throws Exception {
        Integer adId = TestUtils.AD_ID;
        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", adId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser
    void deleteAdAsNonOwnerReturnsForbidden() throws Exception {
        Integer adId = TestUtils.AD_ID;
        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", adId))
                .andExpect(status().isForbidden());// TODO: тоже 200 :(
    }

    @Test
    @WithMockCustomUser(roles = {"ROLE_ADMIN"})
    void deleteAdAsAdminReturnsNoContent() throws Exception {
        Integer adId = TestUtils.AD_ID;

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", adId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockCustomUser
    void deleteNonExistentAdReturnsNotFound() throws Exception {
        Integer adId = TestUtils.AD_ID;
        Mockito.when(adService.isOwner(any(String.class), anyInt())).thenReturn(true);
        BDDMockito.willThrow(new AdNotFoundException("Ad not found for id: " + adId)).given(adService).deleteAd(adId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", adId))
                .andExpect(status().isNotFound());
    }
}