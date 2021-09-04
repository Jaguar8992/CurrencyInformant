package main.service;

import main.client.FeignCurrencyClient;
import main.model.RateOfExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
public class CurrencyService {

    private final FeignCurrencyClient currencyClient;
    private final GiphyService giphyService;
    @Value("${changerates.api.id}")
    private String apiKey;
    @Value("${base}")
    private String base;
    private DateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public CurrencyService(FeignCurrencyClient currencyClient, GiphyService giphyService) {
        this.currencyClient = currencyClient;
        this.giphyService = giphyService;
    }

    /**
     * Получение списка валют
     */

    public Set<String> getCodeList (){
        return currencyClient.getCurrent(apiKey).getRates().keySet();
    }

    /**
     * Обращается к сервису курса валют и проверяет разницу между актуальным курсом на текущий день и предшествующим ему
     * @param currencyCode Код валюты выбранный пользователем
     */

    public String getResponse (String currencyCode){

        RateOfExchange current = currencyClient.getCurrent(apiKey);
        Double currentRate = getTheCurrentRateInBaseCurrency(base, currencyCode, current);
        Double prevRate = getPrevRate(current.getTimestamp() * 1000, currentRate, currencyCode);

        int tagKey = currentRate.compareTo(prevRate);

        return giphyService.getGif(tagKey);
    }

    /**
     * Получение значения текущей валюты за предыдущий день.
     * Метод учитывает, что в выходные и праздничные дни курсы валют не обновляются
     */

    private double getPrevRate (long timestamp, double currantRate, String currencyCode){
        Date date = new Date(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String prevDate = format.format(calendar.getTime());

        RateOfExchange prev = currencyClient.getHistorical(prevDate, apiKey);
        double prevRate = getTheCurrentRateInBaseCurrency(base, currencyCode, prev);

        if (prevRate == currantRate){
            return getPrevRate(calendar.getTime().getTime(), currantRate, currencyCode);
        }
        return prevRate;
    }

    /**
     * Получение значения курса валюты указанной пользователем в отношении базовой валюты
     * @param base Базовая валюта - указывается в настройках
     * @param currencyCode Код валюты, которую указал пользователь
     * @param rate Объект содержащий ответ полученный с сайта https://openexchangerates.org/
     */

    private double getTheCurrentRateInBaseCurrency (String base, String currencyCode, RateOfExchange rate){
        double baseRate = rate.getRates().get(base);
        double rateByCode = rate.getRates().get(currencyCode);

        return rateByCode / baseRate;
    }
}
