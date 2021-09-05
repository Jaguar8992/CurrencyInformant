package main.controller;

import main.service.CurrencyService;
import main.service.GiphyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    private final CurrencyService currencyService;
    private final GiphyService giphyService;

    @Autowired
    public DefaultController(CurrencyService service, GiphyService giphyService) {
        this.currencyService = service;
        this.giphyService = giphyService;
    }

    /**
     * Контроллер генерирует лист доступных валют
     * @return
     */
    @GetMapping("/currencies")
    public ResponseEntity <?> getCurrencies (){
        return currencyService.getCodeList();
    }

    /**
     * Получение gif
     * @param code Код валюты которую пользователь выбирает из списка
     * @return
     */
    @GetMapping("/gif")
    public ResponseEntity<?> getGif (@RequestParam String code){

        ResponseEntity <?> response = currencyService.getTagKey(code);
        if (response.getStatusCodeValue() == 200){
            return giphyService.getGif((int) response.getBody());
        }

        return ResponseEntity.badRequest().body(null);
    }
}
