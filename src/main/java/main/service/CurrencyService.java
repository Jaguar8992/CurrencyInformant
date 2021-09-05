package main.service;

import main.client.FeignCurrencyClient;
import main.model.RateOfExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    public ResponseEntity<?> getCodeList (){
        return ResponseEntity.ok(currencyClient.getCurrent(apiKey).getRates().keySet());
    }

    /**
     * Обращается к сервису курса валют и проверяет разницу между актуальным курсом на текущий день и предшествующим ему
     * @param currencyCode Код валюты выбранный пользователем
     */

    public ResponseEntity<?> getTagKey(String currencyCode){

        RateOfExchange current = currencyClient.getCurrent(apiKey);
        Double currentRate;
        Double prevRate;
        try {
            currentRate = getTheCurrentRateInBaseCurrency(base, currencyCode, current);
            prevRate = getPrevRate(current.getTimestamp() * 1000, currentRate, currencyCode);
        } catch (NullPointerException ex){
            return ResponseEntity.badRequest().body(ex);
        }
        return ResponseEntity.ok(currentRate.compareTo(prevRate));
    }

    /**
     * Получение значения текущей валюты за предыдущий день.
     * Метод учитывает, что в выходные и праздничные дни курсы валют не обновляются
     */

    private Double getPrevRate (long timestamp, double currantRate, String currencyCode) throws NullPointerException{
        Date date = new Date(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String prevDate = format.format(calendar.getTime());

        RateOfExchange prev = currencyClient.getHistorical(prevDate, apiKey);
        Double prevRate = getTheCurrentRateInBaseCurrency(base, currencyCode, prev);

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

    private Double getTheCurrentRateInBaseCurrency (String base, String currencyCode, RateOfExchange rate) throws NullPointerException{
        Double baseRate = rate.getRates().get(base);
        Double rateByCode = rate.getRates().get(currencyCode);

        return baseRate / rateByCode;
    }
}
