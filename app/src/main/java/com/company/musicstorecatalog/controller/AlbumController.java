package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album createAlbum(@RequestBody @Valid Album album) {
        return albumService.createAlbum(album);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album getById(@PathVariable long id) {
        return albumService.getAlbumById(id);
    }



    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@RequestBody @Valid Album album) {
        albumService.updateAlbumById(album);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable("id") long id) {
        albumService.deleteAlbumById(id);
    }


}
