package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.restClients.CBRClient;

@RestController
@RequiredArgsConstructor
public class ExchangeRatesRestController {
    private final CBRClient cbcClient;

    @GetMapping(value = "/api/exchangerates")
    public String getExchangeRates() {
        return cbcClient.getExchangeRates();
    }
}
