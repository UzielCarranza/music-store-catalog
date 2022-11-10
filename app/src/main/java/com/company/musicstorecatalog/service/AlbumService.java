package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.AlbumRepository;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Component
public class AlbumService {
    private AlbumRepository albumRepository;
    private TrackRepository trackRepository;

    public AlbumService(AlbumRepository albumRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }



    public Album getAlbumById(long id) {
        return albumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Album found with the id: " + id));
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Album createAlbum(Album album) {
//        code gather from SMU Challenge 5
        if (album == null) throw new IllegalArgumentException("No Album is passed! Album object is null!");

        return albumRepository.save(album);
    }


    public void updateAlbumById(Album album) {
//        code gather from SMU Challenge 5
        if (album == null) {
            throw new IllegalArgumentException("No Album data is passed! Album object is null!");
        }
        Album editedAlbum = albumRepository.findById(album.getId()).orElseThrow(() -> new IllegalArgumentException("No Album found!"));

        editedAlbum.setArtistId(album.getArtistId());
        editedAlbum.setLabelId(album.getLabelId());
        editedAlbum.setTitle(album.getTitle());
        editedAlbum.setReleaseDate(album.getReleaseDate());
        editedAlbum.setListPrice(album.getListPrice());
        albumRepository.save(editedAlbum);
    }

    public void deleteAlbumById(long id) {
        List<Track> allTracksByAlbumId = trackRepository.findAllByAlbumId(id);
        allTracksByAlbumId.stream().forEach(track -> trackRepository.deleteAllById(Collections.singleton(track.getId())));
        albumRepository.deleteById(id);

    }

    public Album builderAlbum(Album album) {
        Album newAlbum = new Album();
        newAlbum.setTitle(album.getTitle());
        newAlbum.setArtistId(album.getArtistId());
        newAlbum.setReleaseDate(album.getReleaseDate());
        newAlbum.setLabelId(album.getLabelId());
        newAlbum.setListPrice(album.getListPrice());
        return newAlbum;
    }
}