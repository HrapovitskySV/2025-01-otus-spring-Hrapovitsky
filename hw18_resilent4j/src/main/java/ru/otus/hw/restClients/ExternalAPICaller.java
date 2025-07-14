package ru.otus.hw.restClients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.hw.models.Album;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ExternalAPICaller {

    private final RestTemplate restTemplate;

    public Album[] getAlbums() {
        return restTemplate.getForObject("/albums/", Album[].class);
    }

    public Album getAlbumById(Integer id) {
        return restTemplate.getForObject("/albums/"+id, Album.class);
    }

}