package ru.otus.hw.restClients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.hw.models.Album;


@Component
@RequiredArgsConstructor
public class ExternalAPICaller {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "CircuitBreakerService")
    @Retry(name = "retryApi")
    public Album[] getAlbums() {
        return restTemplate.getForObject("/albums/", Album[].class);
    }

    @CircuitBreaker(name = "CircuitBreakerService")
    @Retry(name = "retryApi")
    public Album getAlbumById(Integer id) {
        return restTemplate.getForObject("/albums/" + id, Album.class);
    }

}