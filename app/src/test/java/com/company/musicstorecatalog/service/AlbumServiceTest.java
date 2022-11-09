package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.AlbumRepository;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AlbumServiceTest {


    private AlbumRepository albumRepositoryMock;

    private TrackRepository trackRepositoryMock;

    private AlbumService service;

    private Album albumSetUp;
    private Album actualAlbum;
    private Album editedAlbum;

    private List<Album> albumList;


    @Before
    public void setUp() throws Exception {

        setUpTrackRepository();
        setUpAlbumRepository();

        service = new AlbumService(albumRepositoryMock, trackRepositoryMock);


    }

    @Test
    public void shouldReturnAlbumById() throws Exception {

//        ACT
        actualAlbum = service.getAlbumById(albumSetUp.getId());

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualAlbum = service.getAlbumById(10);

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test
    public void shouldReturnNewAlbum() throws Exception {

//        ACT
        // get it back out of the database
        Album albumPersistentOnDatabase = service.getAlbumById(albumSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumSetUp, albumPersistentOnDatabase);

    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAlbumIsNull() throws Exception {

//        ARRANGE
        albumSetUp = null;

//        ACT
        actualAlbum = service.createAlbum(albumSetUp);

        assertEquals(actualAlbum, albumSetUp);
    }
    @Test
    public void shouldReturnAllAlbums() throws Exception {

//        ACT
        // get it back out of the database
        List<Album> albumsPersistentOnDatabase = service.getAllAlbums();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumList, albumsPersistentOnDatabase);

    }
    @Test
    public void shouldReturnEmptyListOfAlbums() throws Exception {

//        ARRANGE

        albumList = new ArrayList<>();

        doReturn(albumList).when(albumRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<Album> albumsPersistentOnDatabase = service.getAllAlbums();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumList, albumsPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingAnAlbumThatIsNull() throws Exception {

//        ARRANGE
        albumSetUp = null;
//        ACT
        service.updateAlbumById(albumSetUp);

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test
    public void shouldUpdateAnAlbum() throws Exception {

//        ACT
        service.updateAlbumById(albumSetUp);

//        ACT
        // get it back out of the database
        Album albumPersistentOnDatabase = service.getAlbumById(editedAlbum.getId());

        assertEquals(albumSetUp, albumPersistentOnDatabase);
    }

    public void setUpTrackRepository() {
        trackRepositoryMock = mock(TrackRepository.class);
        Track trackSetUp = new Track();
        trackSetUp.setTitle("title test");
        trackSetUp.setRunTime(10);
        trackSetUp.setAlbumId(1);

        doReturn(trackSetUp).when(trackRepositoryMock).save(trackSetUp);
    }

    public void setUpAlbumRepository() {
        albumRepositoryMock = mock(AlbumRepository.class);
        albumSetUp = new Album();
        albumSetUp.setTitle("title test");
        albumSetUp.setListPrice(10.99);
        albumSetUp.setArtistId(1);
        albumSetUp.setLabelId(1);
        albumSetUp.setReleaseDate(LocalDate.parse("2022-02-02"));
        albumSetUp.setId(1);


        editedAlbum = new Album();
        editedAlbum.setTitle("title edited");
        editedAlbum.setListPrice(10.99);
        editedAlbum.setArtistId(1);
        editedAlbum.setLabelId(1);
        editedAlbum.setReleaseDate(LocalDate.parse("2022-02-02"));
        editedAlbum.setId(1);

        doReturn(editedAlbum).when(albumRepositoryMock).save(editedAlbum);
        doReturn(albumSetUp).when(albumRepositoryMock).save(albumSetUp);
        doReturn(Optional.of(albumSetUp)).when(albumRepositoryMock).findById(1L);

//        GET ALL
        Album album1 = new Album();

        album1.setTitle("title test 2 ");
        album1.setListPrice(20.99);
        album1.setArtistId(1);
        album1.setLabelId(1);
        album1.setReleaseDate(LocalDate.parse("2022-03-03"));
        album1.setId(2);

        albumList = new ArrayList<>();
        albumList.add(albumSetUp);
        albumList.add(album1);
        doReturn(albumList).when(albumRepositoryMock).findAll();
    }
}
