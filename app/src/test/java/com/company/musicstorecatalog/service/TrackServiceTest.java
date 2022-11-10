package com.company.musicstorecatalog.service;

import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class TrackServiceTest {
    private TrackRepository trackRepositoryMock;

    private TrackService service;


    private Track trackSetUp;
    private Track actualTrack;
    private Track editedTrack;

    private List<Track> tracksList;

    @Before
    public void setUp() throws Exception {

        setUpTrackRepository();

        service = new TrackService(trackRepositoryMock);


    }

    @Test
    public void shouldReturnTrackById() throws Exception {

//        ACT
        actualTrack = service.getTrackById(trackSetUp.getId());

        assertEquals(actualTrack, trackSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualTrack = service.getTrackById(10);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldReturnNewTrack() throws Exception {

//        ACT
        // get it back out of the database
        Track trackPersistentOnDatabase = service.getTrackById(trackSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(trackSetUp, trackPersistentOnDatabase);

    }

    @Test
    public void shouldReturnNewTrackOnPostRequest() throws Exception {

//        ACT
        // get it back out of the database
        Track trackPersistentOnDatabase = service.createTrack(trackSetUp);

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(trackSetUp, trackPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTrackIsNull() throws Exception {

//        ARRANGE
        trackSetUp = null;

//        ACT
        actualTrack = service.createTrack(trackSetUp);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldReturnAllTracks() throws Exception {

//        ACT
        // get it back out of the database
        List<Track> tracksPersistentOnDatabase = service.getAllTracks();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(tracksList, tracksPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfTracks() throws Exception {

//        ARRANGE

        tracksList = new ArrayList<>();

        doReturn(tracksList).when(trackRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<Track> trackPersistentOnDatabase = service.getAllTracks();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(tracksList, trackPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingTrackThatIsNull() throws Exception {

//        ARRANGE
        trackSetUp = null;
//        ACT
        service.updateTrackById(trackSetUp);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldUpdateATrack() throws Exception {

//        ACT
        service.updateTrackById(trackSetUp);

//        ACT
        // get it back out of the database
        Track trackPersistentOnDatabase = service.getTrackById(editedTrack.getId());

        assertEquals(trackSetUp, trackPersistentOnDatabase);
    }

    public void setUpTrackRepository() {
        trackRepositoryMock = mock(TrackRepository.class);
        trackSetUp = new Track();
        trackSetUp.setTitle("test");
        trackSetUp.setAlbumId(1);
        trackSetUp.setRunTime(10);
        trackSetUp.setId(1);

        editedTrack = new Track();
        editedTrack.setTitle("edited title");
        editedTrack.setAlbumId(1);
        editedTrack.setRunTime(10);
        editedTrack.setId(1);

        doReturn(trackSetUp).when(trackRepositoryMock).save(trackSetUp);
        doReturn(editedTrack).when(trackRepositoryMock).save(editedTrack);
        doReturn(Optional.of(trackSetUp)).when(trackRepositoryMock).findById(1L);

        //        GET ALL
        Track track1 = new Track();

        track1.setTitle("title 2");
        track1.setAlbumId(1);
        track1.setRunTime(20);
        track1.setId(2);

        tracksList = new ArrayList<>();
        tracksList.add(trackSetUp);
        tracksList.add(track1);
        doReturn(tracksList).when(trackRepositoryMock).findAll();
    }
}
