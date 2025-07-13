package ru.otus.hw.restClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.hw.services.CBRFallback;

@FeignClient(value = "cbr", url = "https://www.cbr-xml-daily.ru/",  fallback = CBRFallback.class)
public interface CBRClient {

    @RequestMapping(method = RequestMethod.GET, value = "/daily_json.js")
    String getExchangeRates();
}