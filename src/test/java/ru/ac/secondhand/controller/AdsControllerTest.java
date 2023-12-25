package ru.ac.secondhand.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.repository.UserRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.impl.UserDetailsServiceImpl;
import ru.ac.secondhand.utils.TestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(UserDetailsServiceImpl.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class AdsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdService adService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getAllIsOKWithoutAuthorize() throws Exception {
        BDDMockito.given(adService.getAll()).willReturn(TestUtils.getAds());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(2))); // Проверяем, что в корне JSON два ключа
    }

    @Test
    void getAllIsOKWithoutAuthorizeShouldReturnEmptyAds() throws Exception {
        BDDMockito.given(adService.getAll()).willReturn(new Ads(0, Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/ads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(0)));
    }

    @Test
    @WithMockUser
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
    void getAdInfoIsUnauthorized() throws Exception {
        Integer adId = TestUtils.AD_ID;

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isUnauthorized());

        Mockito.verify(adService, Mockito.never()).getAdInfo(Mockito.anyInt());
    }

    @Test
    @WithMockUser
    void getAdInfoNotFoundAd() throws Exception {
        Integer adId = TestUtils.AD_ID;
        BDDMockito.willThrow(new AdNotFoundException("Ad not found for id: " + adId)).given(adService).getAdInfo(adId);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Ad not found for id: " + adId)));
    }

    @Test
    @WithMockUser
    void getUsersAdsIsOk() throws Exception {
        BDDMockito.given(adService.getUsersAds()).willReturn(TestUtils.getAds());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    @WithMockUser
    void getUsersAdsIsOKShouldReturnEmptyAds() throws Exception {
        BDDMockito.given(adService.getUsersAds()).willReturn(new Ads(0, Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count", Matchers.is(0)))
                .andExpect(jsonPath("$.results", Matchers.hasSize(0)));
    }

    @Test
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
    void createAdHasStatusUnauthorized() throws Exception {
        MockMultipartFile image = TestUtils.createImageFile();
        MockMultipartFile properties = TestUtils.createPropertiesFile();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                        .file(image)
                        .file(properties))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "username@gmail.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void updateUsersAdReturnOK() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        Mockito.when(userRepository.findByUsername("username@gmail.com")).thenReturn(Optional.of(TestUtils.getUserEntity()));
        Mockito.when(adService.isOwner(any(String.class), ArgumentMatchers.anyInt())).thenReturn(true);
        BDDMockito.given(adService.updateAd(adId, updateInfo)).willReturn(TestUtils.getAdDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl")
    void updateAdNotOwnerShouldReturnForbidden() throws Exception {
        Integer adId = TestUtils.AD_ID;
        CreateOrUpdateAd updateInfo = TestUtils.getCreateOrUpdateAd();

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(TestUtils.getUserEntity()));

        Mockito.when(adService.isOwner(any(String.class), ArgumentMatchers.anyInt())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(updateInfo)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
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
    void updateAdImage() {
    }

    @Test
    void deleteAd() {
    }
}