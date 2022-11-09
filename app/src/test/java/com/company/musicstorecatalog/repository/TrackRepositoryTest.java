package com.company.musicstorecatalog.repository;


import com.company.musicstorecatalog.model.Track;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TrackRepositoryTest {


    @Autowired
    private TrackRepository trackRepository;


    private Track track;
    private Track editedTrack;

    @Before
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        setUpTrackRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteTrack() {

        // get it back out of the database
        Track trackPersistentOnDatabase = trackRepository.findById(track.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(track, trackPersistentOnDatabase);

//        update
        trackPersistentOnDatabase.setTitle("title edited");
        trackRepository.save(trackPersistentOnDatabase);
        assertEquals(trackPersistentOnDatabase, editedTrack);

        // delete it
        trackRepository.deleteById(track.getId());

//        // go try to get it again
        Optional<Track> artist3 = trackRepository.findById(track.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpTrackRepository() {
        track = new Track();
        track.setTitle("title");
        track.setRunTime(10);
        track.setAlbumId(1);
        trackRepository.save(track);

        editedTrack = new Track();
        editedTrack.setTitle("title edited");
        editedTrack.setRunTime(10);
        editedTrack.setAlbumId(1);
        editedTrack.setId(1);

    }
}
