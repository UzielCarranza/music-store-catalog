package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/track")
public class TrackController {
    @Autowired
    private TrackService trackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Track createTrack(@RequestBody @Valid Track track) {
        return trackService.createTrack(track);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Track getById(@PathVariable long id) {
        return trackService.getTrackById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Track> getAllTracks() {
        return trackService.getAllTracks();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@RequestBody @Valid Track track) {
        trackService.updateTrackById(track);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable("id") long id) {
        trackService.deleteTrackById(id);
    }

}
