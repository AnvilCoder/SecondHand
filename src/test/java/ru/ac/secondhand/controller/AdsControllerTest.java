package ru.ac.secondhand.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.utils.TestUtils;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdsController.class)
public class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdService adService;

    @MockBean
    private ImageService imageService;

    @Test
    public void givenAuthUser_whenGetAll_thenStatus200() throws Exception {
        when(adService.getAll()).thenReturn(TestUtils.getAds());

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());

    }
}
