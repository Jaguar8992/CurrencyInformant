package main.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Клиент для получения gif с сайта https://giphy.com/
 */
@FeignClient(name = "giphy-client", url = "${giphy.general.url}")
public interface FeignGiphyClient {

    /**
     * Получаем gif по тэгу
     */
    @GetMapping
    String getGif (@RequestParam("api_key") String apiKey, @RequestParam("tag") String tag);

}
