package main;


import main.client.FeignCurrencyClient;
import main.client.FeignGiphyClient;
import main.model.RateOfExchange;
import main.service.CurrencyService;
import main.service.GiphyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@RunWith(SpringRunner.class)
@DisplayName("Services test")
public class ServicesTest {

    @MockBean
    private FeignGiphyClient giphyClient;
    @MockBean
    private FeignCurrencyClient currencyClient;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private GiphyService giphyService;
    @Value("${base}")
    private String base;
    @Value("${giphy.tag.rich}")
    private String rich;
    @Value("${giphy.tag.broke}")
    private String broke;
    private final String testCurrencyCode = "USD";

    private RateOfExchange current;
    private RateOfExchange prev;

    @Before
    public void init() {
        this.current = new RateOfExchange();
        current.setTimestamp(1630858680);
        Map <String, Double> currentRates = new HashMap<>();
        currentRates.put(base, 75.50);
        currentRates.put(testCurrencyCode, 1.0);
        current.setRates(currentRates);

        this.prev = new RateOfExchange();
        prev.setTimestamp(1630774080);
        Map <String, Double> prevRates = new HashMap<>();
        prevRates.put(base, 75.20);
        prevRates.put(testCurrencyCode, 1.0);
        prev.setRates(prevRates);
    }

    @Test
    @DisplayName("Get currency list")
    public void getList(){
        Mockito.when(currencyClient.getCurrent(anyString()))
                .thenReturn(this.current);
        Mockito.when(currencyClient.getHistorical(anyString(), anyString()))
                .thenReturn(this.prev);
        Set<String> currencyList = (Set<String>)currencyService.getCodeList().getBody();
        assertThat(currencyList, containsInAnyOrder(base, testCurrencyCode));
    }

    @Test
    @DisplayName("The value of the current one is greater")
    public void currentIsGreater(){
        Mockito.when(currencyClient.getCurrent(anyString()))
                .thenReturn(this.current);
        Mockito.when(currencyClient.getHistorical(anyString(), anyString()))
                .thenReturn(this.prev);
        int comparison = (int) currencyService.getTagKey(testCurrencyCode).getBody();
        assertEquals(comparison, 1);
    }

    @Test
    @DisplayName("The value of the previous one is greater")
    public void previousIsGreater(){
        Mockito.when(currencyClient.getCurrent(anyString()))
                .thenReturn(this.prev);
        Mockito.when(currencyClient.getHistorical(anyString(), anyString()))
                .thenReturn(this.current);
        int comparison = (int) currencyService.getTagKey(testCurrencyCode).getBody();
        assertEquals(comparison, -1);
    }

    @Test
    @DisplayName("Previous response is null")
    public void previousIsNull(){
        current = null;
        Mockito.when(currencyClient.getCurrent(anyString()))
                .thenReturn(this.current);
        Mockito.when(currencyClient.getHistorical(anyString(), anyString()))
                .thenReturn(this.prev);
        int code = currencyService.getTagKey(testCurrencyCode).getStatusCodeValue();
        assertEquals(code, 400);
    }

    @Test
    @DisplayName("Tag is rich")
    public void rich() {
        Mockito.when(giphyClient.getGif(anyString(), anyString()))
                .thenReturn("}");
        String response = (String) giphyService.getGif(1).getBody();
        int lastIndex = response.lastIndexOf("\"");
        int index = response.indexOf(":");
        String tag = response.substring(index + 2, lastIndex);
        assertEquals(tag, rich);
    }

    @Test
    @DisplayName("Tag is broke")
    public void broke() {
        Mockito.when(giphyClient.getGif(anyString(), anyString()))
                .thenReturn("}");
        String response = (String) giphyService.getGif(-1).getBody();
        int lastIndex = response.lastIndexOf("\"");
        int index = response.indexOf(":");
        String tag = response.substring(index + 2, lastIndex);
        assertEquals(tag, broke);
    }
}
