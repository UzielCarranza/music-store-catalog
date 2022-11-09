package com.company.musicstorecatalog.repository;


import com.company.musicstorecatalog.model.Artist;
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
public class ArtistRepositoryTest {


    @Autowired
    private ArtistRepository artistRepository;


    private Artist artist;
    private Artist editedArtist;

    @Before
    public void setUp() throws Exception {
        artistRepository.deleteAll();
        setUpArtistRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteArtist() {

        // get it back out of the database
        Artist artistPersistentOnDatabase = artistRepository.findById(artist.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artist, artistPersistentOnDatabase);

//        update
        artistPersistentOnDatabase.setName("name edited");
        artistRepository.save(artistPersistentOnDatabase);
        assertEquals(artistPersistentOnDatabase, editedArtist);

        // delete it
        artistRepository.deleteById(artist.getId());

//        // go try to get it again
        Optional<Artist> artist3 = artistRepository.findById(artist.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpArtistRepository() {
        artist = new Artist();
        artist.setName("test");
        artist.setTwitter("test@twitter");
        artist.setInstagram("test@instagram");


        editedArtist = new Artist();
        editedArtist.setName("name edited");
        editedArtist.setTwitter("test@twitter");
        editedArtist.setInstagram("test@instagram");
        editedArtist.setId(1);

        artistRepository.save(artist);
    }

}
