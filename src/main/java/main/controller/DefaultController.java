package main.controller;

import main.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    private final CurrencyService service;

    @Autowired
    public DefaultController(CurrencyService service) {
        this.service = service;
    }

    /**
     * Контроллер генерирует лист доступных валют
     * @return
     */
    @GetMapping("/currencies")
    public ResponseEntity <?> getCurrencies (){
        return ResponseEntity.ok(service.getCodeList());
    }

    /**
     * Получение gif
     * @param code Код валюты которую пользователь выбирает из списка
     * @return
     */
    @GetMapping("/gif")
    public ResponseEntity<String> getGif (@RequestParam String code){
        return ResponseEntity.ok(service.getResponse(code));
    }
}
