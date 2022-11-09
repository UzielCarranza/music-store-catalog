package com.company.musicstorecatalog.repository;


import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.model.Track;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TrackRepository trackRepository;

    private Album album1;
    private Album editedAlbum;
    private Artist artistSetUp;
    private Label labelSetUp;
    private Track trackSetUp;

    @Before
    public void setUp() throws Exception {
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        labelRepository.deleteAll();
        trackRepository.deleteAll();
        setUpArtistRepository();
        setUpLabelRepository();
        setUpTrackRepository();
        setUpAlbumRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteAlbum() {

        // get it back out of the database
        Album albumPersistentOnDatabase = albumRepository.findById(album1.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(album1, albumPersistentOnDatabase);

//        update
        albumPersistentOnDatabase.setTitle("title edited");
        albumRepository.save(albumPersistentOnDatabase);
        assertEquals(albumPersistentOnDatabase, editedAlbum);

        // delete it
        albumRepository.deleteById(album1.getId());

//        // go try to get it again
        Optional<Album> album3 = albumRepository.findById(album1.getId());
//
//        // confirm that it's gone
        assertEquals(false, album3.isPresent());
    }


    public void setUpArtistRepository() {
        artistSetUp = new Artist();
        artistSetUp.setName("test");
        artistSetUp.setTwitter("test@twitter");
        artistSetUp.setInstagram("test@instagram");
        artistRepository.save(artistSetUp);
    }

    public void setUpLabelRepository() {
        labelSetUp = new Label();
        labelSetUp.setWebsite("somewebsite.com");
        labelSetUp.setName("test");
        labelRepository.save(labelSetUp);
    }

    public void setUpTrackRepository() {
        trackSetUp = new Track();
        trackSetUp.setTitle("title test");
        trackSetUp.setRunTime(10);
        trackSetUp.setAlbumId(1);

        trackRepository.save(trackSetUp);
    }

    public void setUpAlbumRepository() {
        album1 = new Album();
        album1.setTitle("title test");
        album1.setListPrice(10.99);
        album1.setArtistId(artistSetUp.getId());
        album1.setLabelId(labelSetUp.getId());
        album1.setReleaseDate(LocalDate.parse("2022-02-02"));


        editedAlbum = new Album();
        editedAlbum.setTitle("title edited");
        editedAlbum.setListPrice(10.99);
        editedAlbum.setArtistId(artistSetUp.getId());
        editedAlbum.setLabelId(labelSetUp.getId());
        editedAlbum.setReleaseDate(LocalDate.parse("2022-02-02"));
        editedAlbum.setId(1);
        albumRepository.save(album1);
    }
}
