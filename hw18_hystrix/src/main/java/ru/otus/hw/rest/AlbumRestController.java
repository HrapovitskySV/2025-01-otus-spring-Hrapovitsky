package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Album;
import ru.otus.hw.restClients.AlbumClient;
import ru.otus.hw.restClients.CBRClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlbumRestController {
    private final AlbumClient albumClient;

    @GetMapping(value = "/api/albums")
    public List<Album> getAlbums() {
        var r = albumClient.getAlbums();
        return r;
    }

    @GetMapping(value = "/api/albums/{id}")
    public Album getAlbumById(@PathVariable(value = "id") Integer id) {
        return albumClient.getAlbumById(id);
    }
}
