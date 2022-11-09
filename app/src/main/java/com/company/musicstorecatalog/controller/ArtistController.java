package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(@RequestBody @Valid Artist artist) {
        return artistService.createArtist(artist);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artist getById(@PathVariable long id) {
        return artistService.getArtistById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtist(@RequestBody @Valid Artist artist) {
        artistService.updateArtistById(artist);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable("id") long id) {
        artistService.deleteArtistById(id);
    }
}
