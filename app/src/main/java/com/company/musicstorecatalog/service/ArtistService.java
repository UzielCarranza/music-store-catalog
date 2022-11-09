package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class ArtistService {
    private ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist createArtist(Artist artist) {
        //        code gather from SMU Challenge 5
        if (artist == null) throw new IllegalArgumentException("No Artist is passed! Artist object is null!");

        return artistRepository.save(artist);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public void updateArtistById(Artist artist) {
        //        code gather from SMU Challenge 5
        if (artist == null) {
            throw new IllegalArgumentException("No Artist data is passed! Artist object is null!");
        }
        Artist artistToBeEdited = artistRepository.findById(artist.getId()).orElseThrow(() -> new IllegalArgumentException("No Artist found!"));

        artistToBeEdited.setInstagram(artist.getInstagram());
        artistToBeEdited.setTwitter(artist.getTwitter());
        artistToBeEdited.setName(artist.getName());
        artistRepository.save(artistToBeEdited);
    }

    public Artist getArtistById(long id) {

        return artistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Artist found with the id: " + id));
    }

    public void deleteArtistById(long id) {
        artistRepository.deleteById(id);
    }
}
