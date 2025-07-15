package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Album;
import ru.otus.hw.restClients.ExternalAPICaller;

@RestController
@RequiredArgsConstructor
public class AlbumRestController {

    private final ExternalAPICaller externalAPICaller;

    @GetMapping(value = "/api/albums")
    public Album[] getAlbums() {
        return externalAPICaller.getAlbums();
    }

    @GetMapping(value = "/api/albums/{id}")
    public Album getAlbumByIdRetry(@PathVariable(value = "id") Integer id) {
        return externalAPICaller.getAlbumById(id);
    }
}
