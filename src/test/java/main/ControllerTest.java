package main;


import main.service.CurrencyService;
import main.service.GiphyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DisplayName("Testing controller")
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private GiphyService giphyService;

    @Test
    @DisplayName("Controller @NotNull")
    public void currencyServiceNotNull() {
        assertThat(currencyService).isNotNull();
    }

    @Test
    @DisplayName("Controller @NotNull")
    public void giphyServiceNotNull() throws Exception {
        assertThat(currencyService).isNotNull();
    }

    @Test
    @DisplayName("Get code list")
    public void getCodeList() throws Exception {
        this.mockMvc.perform(get("/currencies"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get gif")
    public void getGif() throws Exception {
        this.mockMvc.perform(get("/gif?code=USD"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get exception")
    public void getException() throws Exception {
        this.mockMvc.perform(get("/gif?code=FAKE"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}


