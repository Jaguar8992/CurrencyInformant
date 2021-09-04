package main.client;

import main.model.RateOfExchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Клиент для загрузки курса валют с сайта https://openexchangerates.org/
 */
@FeignClient(name = "currency-client", url = "${changerates.general.url}")
public interface FeignCurrencyClient {

    /**
     * Получаем курсы валют по дате
     */
    @GetMapping("/historical/{date}.json")
    RateOfExchange getHistorical(@PathVariable String date, @RequestParam("app_id") String apiId);

    /**
     * Получаем текущие курсы валют
     */
    @GetMapping("/latest.json")
    RateOfExchange getCurrent(@RequestParam("app_id") String apiId);


}
