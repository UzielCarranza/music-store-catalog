package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TrackService {
    private TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track createTrack(Track track) {
        //        code gather from SMU Challenge 5
        if (track == null) throw new IllegalArgumentException("No Track is passed! Track object is null!");

        return trackRepository.save(track);
    }

    public void deleteTrackById(long id) {
        trackRepository.deleteById(id);
    }

    public void updateTrackById(Track track) {
        //        code gather from SMU Challenge 5
        if (track == null) {
            throw new IllegalArgumentException("No Track data is passed! Track object is null!");
        }
        Track trackToBeEdited = trackRepository.findById(track.getId()).orElseThrow(() -> new IllegalArgumentException("No Track found!"));

        trackToBeEdited.setAlbumId(track.getAlbumId());
        trackToBeEdited.setRunTime(track.getRunTime());
        trackToBeEdited.setTitle(track.getTitle());
        trackRepository.save(trackToBeEdited);
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public Track getTrackById(long id) {
        return trackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Track found with the id: " + id));
    }
}
