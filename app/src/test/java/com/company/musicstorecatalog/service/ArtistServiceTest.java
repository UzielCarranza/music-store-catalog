package com.company.musicstorecatalog.service;


import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
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
public class ArtistServiceTest {

    private ArtistRepository artistRepositoryMock;

    private ArtistService service;


    private Artist artistSetUp;
    private Artist actualArtist;
    private Artist editedArtist;

    private List<Artist> artistList;

    @Before
    public void setUp() throws Exception {

        setUpArtistRepository();

        service = new ArtistService(artistRepositoryMock);


    }

    @Test
    public void shouldReturnArtistById() throws Exception {

//        ACT
        actualArtist = service.getArtistById(artistSetUp.getId());

        assertEquals(actualArtist, artistSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualArtist = service.getArtistById(10);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldReturnNewArtist() throws Exception {

//        ACT
        // get it back out of the database
        Artist artistPersistentOnDatabase = service.getArtistById(artistSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistSetUp, artistPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenArtistIsNull() throws Exception {

//        ARRANGE
        artistSetUp = null;

//        ACT
        actualArtist = service.createArtist(artistSetUp);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldReturnAllArtist() throws Exception {

//        ACT
        // get it back out of the database
        List<Artist> artistsPersistentOnDatabase = service.getAllArtists();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistList, artistsPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfAlbums() throws Exception {

//        ARRANGE

        artistList = new ArrayList<>();

        doReturn(artistList).when(artistRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<Artist> artistPersistentOnDatabase = service.getAllArtists();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistList, artistPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingAnAlbumThatIsNull() throws Exception {

//        ARRANGE
        artistSetUp = null;
//        ACT
        service.updateArtistById(artistSetUp);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldUpdateAnArtist() throws Exception {

//        ACT
        service.updateArtistById(artistSetUp);

//        ACT
        // get it back out of the database
        Artist artistPersistentOnDatabase = service.getArtistById(editedArtist.getId());

        assertEquals(artistSetUp, artistPersistentOnDatabase);
    }

    public void setUpArtistRepository() {
        artistRepositoryMock = mock(ArtistRepository.class);
        artistSetUp = new Artist();
        artistSetUp.setName("name test");
        artistSetUp.setTwitter("test@twitter");
        artistSetUp.setInstagram("test@instagram");
        artistSetUp.setId(1);

        editedArtist = new Artist();
        editedArtist.setName("edited test");
        editedArtist.setTwitter("test@twitter");
        editedArtist.setInstagram("test@instagram");
        editedArtist.setId(1);

        doReturn(artistSetUp).when(artistRepositoryMock).save(artistSetUp);
        doReturn(editedArtist).when(artistRepositoryMock).save(editedArtist);
        doReturn(Optional.of(artistSetUp)).when(artistRepositoryMock).findById(1L);

        //        GET ALL
        Artist artist1 = new Artist();

        artist1.setName("test 2");
        artist1.setTwitter("test2@twitter");
        artist1.setInstagram("test2t@instagram");
        artist1.setId(2);

        artistList = new ArrayList<>();
        artistList.add(artistSetUp);
        artistList.add(artist1);
        doReturn(artistList).when(artistRepositoryMock).findAll();
    }
}
