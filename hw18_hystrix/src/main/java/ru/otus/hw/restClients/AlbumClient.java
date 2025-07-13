package ru.otus.hw.restClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.models.Album;

import java.util.List;

@FeignClient(name = "albumClient", url = "https://jsonplaceholder.typicode.com/albums")
public interface AlbumClient {

    @GetMapping(value = "/")
    List<Album> getAlbums();

    @GetMapping(value = "/{id}")
    Album getAlbumById(@PathVariable(value = "id") Integer id);
}