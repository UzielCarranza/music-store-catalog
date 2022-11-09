package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Label;
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
public class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;


    private Label label;
    private Label editedLabel;

    @Before
    public void setUp() throws Exception {
        labelRepository.deleteAll();
        setUpLabelRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteLabel() {

        // get it back out of the database
        Label labelPersistentOnDatabase = labelRepository.findById(label.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(label, labelPersistentOnDatabase);

//        update
        labelPersistentOnDatabase.setName("name edited");
        labelRepository.save(labelPersistentOnDatabase);
        assertEquals(labelPersistentOnDatabase, editedLabel);

        // delete it
        labelRepository.deleteById(label.getId());

//        // go try to get it again
        Optional<Label> artist3 = labelRepository.findById(label.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpLabelRepository() {
        label = new Label();
        label.setWebsite("somewebsite.com");
        label.setName("test");
        labelRepository.save(label);

        editedLabel = new Label();
        editedLabel.setWebsite("somewebsite.com");
        editedLabel.setName("name edited");
        editedLabel.setId(1);

    }
}
