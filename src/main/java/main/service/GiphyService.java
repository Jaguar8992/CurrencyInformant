package main.service;

import feign.FeignException;
import main.client.FeignGiphyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GiphyService {

    private final FeignGiphyClient giphyClient;
    @Value("${giphy.api.id}")
    private String apiKey;
    @Value("${giphy.tag.rich}")
    private String rich;
    @Value("${giphy.tag.broke}")
    private String broke;

    @Autowired
    public GiphyService(FeignGiphyClient giphyClient) {
        this.giphyClient = giphyClient;
    }

    /**
     * Получает gif, ее тип зависит от того вырос курс ли валюты за последний день или же упал
     * @return Возвращает ответ с сервиса https://giphy.com/, а также тэг
     */
    public ResponseEntity<?> getGif (int tagKey) {
        String tag;
        String gifData;

        if (tagKey > 0){
            tag = rich;
        } else {
            tag = broke;
        }

        try {
            gifData = giphyClient.getGif(apiKey, tag);
        }  catch (FeignException ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        int index = gifData.lastIndexOf("}");
        /**
         * Добавляем ссылку на тэг по которому был найден gif
        * */
        return ResponseEntity.ok(gifData.substring(0, index) + ",\"tag\":\"" + tag + "\"}");
    }
}
